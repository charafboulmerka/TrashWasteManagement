package com.f08.wasteexpress.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.astritveliu.boom.utils.BoomUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.f08.wasteexpress.Utils.mUtils
import com.f08.wasteexpress.Models.Trash
import com.f08.wasteexpress.R
import com.f08.wasteexpress.TrashDetails
import com.f08.wasteexpress.Utils.getConvertedDate
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import kotlin.collections.ArrayList

class trashAdapter : RecyclerView.Adapter<trashAdapter.mHolder> {

    var mLocalList= ArrayList<Trash>()
    var mCtx: Context?=null
    private var currentUser:String

    constructor(mCtx: Context, mList: ArrayList<Trash>){
        mLocalList= mList
        this.mCtx=mCtx
        currentUser= mUtils(mCtx).getDeviceIdOrUID()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trash_card,parent,false)
        return mHolder(view)
    }

    override fun getItemCount(): Int {
        return mLocalList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class mHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        override fun onClick(v: View?) {
            Log.e("BUTTON","CLICKED")
        }
        var mCard = view.findViewById<CardView>(R.id.trash_card)
        var mTrashTitle = view.findViewById<TextView>(R.id.card_title)
        var mTrashDescription = view.findViewById<TextView>(R.id.card_description)
        var mTrashWilaya = view.findViewById<TextView>(R.id.card_wilaya)
        var mTrashPrice = view.findViewById<TextView>(R.id.card_price)
        var mTrashQty = view.findViewById<TextView>(R.id.card_qty)
        var mTrashUnite = view.findViewById<TextView>(R.id.card_unite)

        var mTrashDate = view.findViewById<TextView>(R.id.card_date)
        var mTrashImage = view.findViewById<ImageView>(R.id.trash_image)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: mHolder, position: Int) {
        val item = mLocalList[position]
        BoomUtils.boomAll(holder.mCard)
        holder.mCard.setOnClickListener {
            mCtx!!.startActivity(Intent(mCtx, TrashDetails::class.java).putExtra("trash", Gson().toJson(item)))
        }

        val wilayas = mCtx!!.resources.getStringArray(R.array.wilayas)
        val types = mCtx!!.resources.getStringArray(R.array.dechet_types)

        holder.mTrashTitle.text = item.title
        holder.mTrashDescription.text = item.description
        holder.mTrashWilaya.text = types[item.trash_type!!]

        //holder.mTrashWilaya.text = wilayas[item.wilaya!!].split("-")[1]
        holder.mTrashPrice.text = "${item.price_per_kg} DA "
        holder.mTrashQty.text = item.qty
        holder.mTrashUnite.text = item.unite
        holder.mTrashDate.text = getConvertedDate.obtenirTempsEcoule(item.publishedDateMs!!.toLong(),mCtx!!)
        if (item.images != null){
            val requestOptions = RequestOptions().transform(RoundedCorners(70))

            Glide.with(mCtx!!)
                .load(item.images!![0])
                .apply(requestOptions)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        holder.mTrashImage.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle any cleanup if necessary
                    }
                })

        }else{
            holder.mTrashImage.setImageResource(R.drawable.logo)
        }

        /*holder.mDreamDate.text = "نشر في : " +item.publishedDate
        holder.mDreamViews.text = item.viewd.toString() + " مشاهدة  "*/

        /*if (item.orderType=="private"){
            /** PRIVATE DREAM CAN BEEN SEEN BY THE DREAM OWNER & ADMIN & EDITOR **/
            holder.mDreamIcon.setBackgroundResource(R.drawable.icon_dream)
            if (currentUser==item.userId || currentUser==mCtx!!.getString(R.string.admin_uid)
                || mUtils(mCtx!!).getUserRole()==mCtx!!.getString(R.string.role_editor)){
                holder.mCard.visibility = View.VISIBLE
            }else{
                holder.mCard.visibility = View.GONE
            }
        }else{
            holder.mDreamIcon.setBackgroundResource(R.drawable.icon_dream)
        }
        if (item.viewd==989898){
            holder.mDreamViews.visibility=View.GONE
        }

        /** ONLY THE OWNER OF THE DREAM AND THE ADMIN CAN DELETE THE DREAM **/
        if (currentUser==item.userId || currentUser==mCtx!!.getString(R.string.admin_uid)){
            holder.mDreamDelete.visibility=View.VISIBLE
            holder.mDreamDelete.setOnClickListener {
                confirmationDelete(item,position)
            }
        }else{
            holder.mDreamDelete.visibility = View.GONE
        }*/
    }

    /*fun addView(item:DreamRead){
        val mRefFav = FirebaseDatabase.getInstance().reference.child("dreams").child(item.id!!).child("views")
        mRefFav.child(currentUser).setValue(true)
    }

    fun confirmationDelete(item:DreamRead,position: Int){
        val builder = AlertDialog.Builder(mCtx)
        builder.setMessage("هل انت متأكد من حذفك لهذا الحلم؟")

        builder.setPositiveButton("حذف") { dialog, which ->
            deleteDream(item,position)
        }

        builder.setNegativeButton("إلغاء") { dialog, which ->

        }

        builder.show()
    }

    fun deleteDream(item:DreamRead,position: Int){
        mUtils(mCtx!!).runLoading(arrayListOf(),null)
        FirebaseDatabase.getInstance().reference.child("dreams").child(item.id!!).child("status").setValue(3)
            .addOnCompleteListener { task->
                mUtils(mCtx!!).stopLoading(arrayListOf(),null)
                if (task.isSuccessful){
                    mLocalList.removeAt(position)
                    notifyItemRemoved(position)
                    MotionToast.createColorToast(
                        mCtx!! as Activity,
                        "تمت العملية بنجاح",
                        "تم حذف حلمك من التطبيق بنجاح",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null)
                }else{
                    MotionToast.createColorToast(
                        mCtx!! as Activity,
                        "فشلت العملية",
                        "فشلت عملية حذف حلمك الرجاء تفقد إتصالك بالأنترنت",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null)
                }

            }
    }*/
}