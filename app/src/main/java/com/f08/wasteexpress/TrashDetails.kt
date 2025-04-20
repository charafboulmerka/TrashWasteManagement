package com.f08.wasteexpress

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.f08.wasteexpress.Models.Config
import com.f08.wasteexpress.Models.Trash
import com.f08.wasteexpress.Utils.mUtils
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_trash_details.*
import java.text.DecimalFormat


class TrashDetails : AppCompatActivity() {

    lateinit var mUtils :mUtils
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash_details)
        mUtils = mUtils(this)
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)

        val types = resources.getStringArray(R.array.dechet_types)
        val imageList = ArrayList<SlideModel>() // Create image list


        val it = intent.extras
        val trashDetails = Gson().fromJson(it!!.getString("trash",""), object : TypeToken<Trash>() {}.type) as Trash
        tv_trash_title.text = trashDetails.title
        tv_trash_description.text = trashDetails.description
        tv_trash_type.text = types[trashDetails.trash_type!!]
        tv_trash_weight.text = "${trashDetails.qty} ${trashDetails.unite!!.toUpperCase()}"
        val SellingPrice = mUtils.getConfig().selling_fee_per_kg!!.toDouble() + mUtils.getConfig().buying_price_per_kg!!.toDouble()
        tv_trash_selling_price.text = "${SellingPrice}DA / KG"


        tv_trash_price.text = "${getTotalSellingPrice(trashDetails)} DA"

        if (trashDetails.images != null){
            for(imageUrl in trashDetails.images!!){
                imageList.add(SlideModel(imageUrl,trashDetails.title))
            }
        }else{
            imageList.add(SlideModel(R.drawable.logo,trashDetails.title))
        }
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

    }

    fun getTotalSellingPrice(trashDetails:Trash):String{
        val dec = DecimalFormat("#,###.00")

        var qty = trashDetails.qty!!.toDouble()
        val selling_fee = mUtils.getConfig().selling_fee_per_kg!!.toDouble()
        val buying_price = mUtils.getConfig().buying_price_per_kg!!.toDouble()
        val selling_price = selling_fee + buying_price
        val unite = trashDetails.unite
        if (unite != "KG"){
         qty = trashDetails.qty!!.toDouble() * 1000
        }
        val totalSellingPrice = qty * selling_price
        return dec.format(totalSellingPrice)
    }



}