package com.f08.wasteexpress

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.astritveliu.boom.utils.BoomUtils
import com.f08.wasteexpress.Models.Trash
import com.f08.wasteexpress.Utils.mUtils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_step2.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Step2Activity : AppCompatActivity() {
     var main_type : Int = 0
     var sub_type = 0
    var total = 0.0
    lateinit var mUtils: mUtils
    private lateinit var listView :ArrayList<View>
    private lateinit var mRef: DatabaseReference

    private lateinit var storageRef: StorageReference;
    val images: ArrayList<Image>? = null
    val REQUEST_LOCATION = 199
    val df2 = SimpleDateFormat("dd|MM|yyyy")
    var todayDate = ""
    var finalPath = ""
    var finalType = ""
    val imagePaths = ArrayList<String>()
    val mDownloadUrls = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2)
        mUtils = mUtils(this)
        main_type = mUtils.getTrashTypeData()
        listView= arrayListOf<View>()
        header_title.text = "Nouvelle offre"

        mRef = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance()
            .getReferenceFromUrl("gs://wasteexpress-52293.appspot.com")
            .child("images")


        btn_addImg.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName("Waste Express")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(3)
                .setLimitMessage("Vous ne pouvez sélectionner que trois images")
                .setSelectedImages(images)
                .setRequestCode(100)
                .start()
        }


        val dechet_types = resources.getStringArray(R.array.dechet_types)
        val dechet_type1_subs = resources.getStringArray(R.array.dechet_type1_subs)
        val dechet_type2_subs = resources.getStringArray(R.array.dechet_type2_subs)
        val dechet_type3_subs = resources.getStringArray(R.array.dechet_type3_subs)
        val dechet_type4_subs = resources.getStringArray(R.array.dechet_type4_subs)
        val dechet_type5_subs = resources.getStringArray(R.array.dechet_type5_subs)

        trash_buying_amount.text =  "Prix d'achat d'aujourd'hui : ${mUtils.getConfig().buying_price_per_kg} DA / KG"


        trash_main_type.text = "Veuillez sélectionner le sous-type de ${dechet_types[main_type]} :"
        trash_quantity_unite.selectItemByIndex(0)

        BoomUtils.boomAll(btn_step2,BtnDeleteImages,btn_addImg,btn_step2)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        } else {
            // Permission already granted, proceed with file access
            //accessFile()
        }

        trash_quantity_unite.setOnSpinnerItemSelectedListener<String> { _, _, _, _ ->
            updateTotal(trash_quantity.text.toString())
        }

        trash_quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateTotal(s.toString())
            }
        })

        if(main_type == 1){
            sp_sub_types.setItems(dechet_type1_subs.asList())
        }else if (main_type == 2){
            sp_sub_types.setItems(dechet_type2_subs.asList())
        }else if (main_type == 3){
            sp_sub_types.setItems(dechet_type3_subs.asList())
        }else if (main_type == 4){
            sp_sub_types.setItems(dechet_type4_subs.asList())
        }else if (main_type == 5){
            sp_sub_types.setItems(dechet_type5_subs.asList())

        }


        btn_step2.setOnClickListener {
            uploadTrashToServer()
        }

    }

    fun uploadTrashToServer(){
        if (trash_title.text.isEmpty()) {
            trash_title.setError("Veuillez entrer un titre et réessayer")
        } else if (trash_description.text.isEmpty()) {
            trash_description.setError("Veuillez entrer une description et réessayer")
        } else if (trash_quantity.text.isEmpty() && trash_quantity.text.toString() != "0") {
            trash_quantity.setError("Veuillez entrer une quantité de déchets et réessayer")
        } else if (sp_sub_types.selectedIndex == -1) {
            MotionToast.createColorToast(this,
                "Erreur",
                "Vous devez sélectionner le type de déchet",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null)
        }else if (sp_sub_wilayas.selectedIndex == -1) {
            MotionToast.createColorToast(this,
                "Erreur",
                "Vous devez sélectionner la région",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                null)
        }else if (!imagePaths.isEmpty()) {
            upImageToServer()
        } else {
            pushTrash()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file access
                //accessFile()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun updateTotal(quantityStr: String) {
        if (quantityStr.isNotEmpty()) {
            val quantity = quantityStr.toDouble()
            var total_dzd_format = calculateTotal(quantity)
            trash_selling_amount_total.text = "Prix de vente : $total_dzd_format"
            trash_selling_amount_total.visibility = View.VISIBLE
        } else {
            trash_selling_amount_total.visibility = View.GONE
        }
    }

    fun calculateTotal(qty: Double): String {
        val price = mUtils.getConfig().buying_price_per_kg!!.toDouble()
        var unite = trash_quantity_unite.selectedIndex
        val totalPrice = if (unite == 0) { // KG
            qty * price
        } else { // TONNES
            qty * 1000 * price
        }
        total = totalPrice
        // Format the total price to DZD format
        val format = NumberFormat.getCurrencyInstance(Locale("fr", "DZ"))
        format.currency = Currency.getInstance("DZD")
        return format.format(totalPrice)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // The last parameter value of shouldHandleResult() is the value we pass to setRequestCode().
        // If we do not call setRequestCode(), we can ignore the last parameter.

        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            // Do stuff with images' paths or ids. For example:
            val images: ArrayList<Image> = ImagePicker.getImages(data)

            tv_selectedImg.visibility = View.VISIBLE
            BtnDeleteImages.visibility = View.VISIBLE
            BtnDeleteImages.setOnClickListener {
                imagePaths.clear()
                tv_selectedImg.text = ""
                tv_selectedImg.visibility = View.GONE
                BtnDeleteImages.visibility = View.GONE

                btn_addImg.text = "Ajouter des images"

            }
            tv_selectedImg.text = "${images.size} images sélectionnées"

            btn_addImg.text = "Changer les images"

            // Prepare paths for upload
            imagePaths.clear()
            for (image in images) {
                imagePaths.add(image.path)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun upImageToServer() {
        lifecycleScope.launch {
            try {
                mUtils.showLoading()
                val df = SimpleDateFormat("ddMMyyHHmmSS", Locale.getDefault())
                val uploads = mutableListOf<Task<Uri>>()

                for (path in imagePaths) {
                    val timestamp = df.format(Date())
                    val imgRef = storageRef.child("$todayDate/image_$timestamp.jpg")

                    val compressedPictureFile = Compressor.compress(this@Step2Activity, File(path))
                    val picFinalPath = compressedPictureFile.path
                    val chosenFile = Uri.fromFile(File(picFinalPath))

                    val up = imgRef.putFile(chosenFile)
                    uploads.add(
                        up.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let { throw it }
                            }
                            return@Continuation imgRef.downloadUrl
                        })
                    )
                }

                Tasks.whenAllComplete(uploads).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrls = uploads.mapNotNull { it.result?.toString() }
                        mDownloadUrls.addAll(downloadUrls)
                        pushTrash()
                    } else {
                        task.exception?.let {
                            Toast.makeText(this@Step2Activity, it.toString(), Toast.LENGTH_LONG).show()
                            mUtils.hideLoading()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* fun upDataToServer(picUrl: String) {
         var mKey = mRef.push()
         mKey.setValue(
             addModel(
                 mKey.key.toString(),
                 getFinalCoords()["latitude"]!!,
                 getFinalCoords()["longitude"]!!,
                 edit_address.text.toString(),
                 edit_note.text.toString(),
                 finalType,
                 picUrl,
                 todayDate,
                 System.currentTimeMillis().toString(),
                 "AinSmara",
                 "false"
             )
         )
             .addOnCompleteListener { task ->
                 // ButtonsState(true)
                 if (task.isSuccessful) {
                     startLoadingLayout("success")
                 } else {
                     startLoadingLayout("failed")
                 }
             }

     }
 */










    fun pushTrash(){
        //mUtils(this).runLoading(listView)
        mUtils.showLoading()
        val unites = resources.getStringArray(R.array.unites)

        val userID = mUtils.getUser()!!.id
        val trash_type = mUtils.getTrashTypeData()
        val trash_sub_type = sp_sub_types.selectedIndex
        val trash_title = trash_title.text.toString()
        val trash_description = trash_description.text.toString()
        val trash_qty = trash_quantity.text.toString()
        val trash_unite = unites[trash_quantity_unite.selectedIndex]
        val trash_today_price = mUtils.getConfig().buying_price_per_kg
        val personal_info = mUtils.getPersonalnfoData()
        val today_date = mUtils.getTodayDate()
        val today_ms = System.currentTimeMillis()
        val wilaya = sp_sub_wilayas.selectedIndex



        val refPush = mRef.child("trashs").push()


        val mTrash = Trash(refPush.key!!,userID!!,personal_info,trash_title,trash_description,
            trash_qty,trash_type,trash_sub_type,trash_unite,trash_today_price!!
            ,total.toString(), today_date!!, today_ms.toString(),1,wilaya,mDownloadUrls)

        refPush.setValue(mTrash).addOnCompleteListener { task->
            if (task.isSuccessful){
                mUtils.hideLoading()
                //mUtils(this).stopLoading(listView)
                MotionToast.createColorToast(this,
                    "Opération réussie",
                    "Votre offre a été créée avec succès",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                mUtils.hideLoading()
                MotionToast.createColorToast(this,
                    "Erreur",
                    task.exception!!.message.toString(),
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
            }
        }
    }







}