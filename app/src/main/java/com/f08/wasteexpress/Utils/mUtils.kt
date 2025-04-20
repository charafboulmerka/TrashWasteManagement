package com.f08.wasteexpress.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import com.astritveliu.boom.Boom
import com.f08.wasteexpress.Models.Config
import com.f08.wasteexpress.Models.Personalnfo
import com.f08.wasteexpress.Models.User
import com.f08.wasteexpress.R
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.header.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class mUtils {
    var mCtx:Context?=null
    lateinit var mAuth: FirebaseAuth
    private lateinit var mSharedPreferences: SharedPreferences

    constructor(mCtx:Context){
        mSharedPreferences = mCtx.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()
        this.mCtx=mCtx
        val mAct = mCtx as Activity
        if (mAct.header_btn_back != null) {
            Boom(mAct.header_btn_back)
            mAct.header_btn_back.setOnClickListener { mAct.finish() }
        }


    }

    @SuppressLint("HardwareIds")
    fun getDeviceIdOrUID():String{
        if (mAuth.currentUser!=null){
            return mAuth.uid!!
        }else{
        val device_id = Settings.Secure.getString(mCtx!!.contentResolver, Settings.Secure.ANDROID_ID)
        return device_id
        }
    }




    fun getPersonalnfoData():Personalnfo{
        val userData = Personalnfo()
        userData.country=mSharedPreferences.getString("country","")
        userData.social_status=mSharedPreferences.getString("social_status","")
        userData.age=mSharedPreferences.getString("age","")
        userData.gender=mSharedPreferences.getString("gender","")
        userData.job_status=mSharedPreferences.getString("job_status","")
        return userData
    }




    fun saveTrashType(trash_type:Int){
        val editShared = mSharedPreferences.edit()
        editShared.putInt("trash_type",trash_type)
        editShared.apply()
    }

    fun getTrashTypeData():Int{
        val order_type = mSharedPreferences.getInt("trash_type",0)
        return order_type!!
    }



    fun clearAllData(){
        val editShared = mSharedPreferences.edit().clear()
        editShared.apply()
    }

    fun getTodayDate(): String? {
        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        return df.format(Date())
    }


    fun runLoading(listView:ArrayList<View>,layout:View?=null){
       val mP = (mCtx as Activity).findViewById<View>(R.id.mProgress)
        mP.visibility = View.VISIBLE
        if (layout!=null){
            layout!!.alpha = 0.7f
        }
        for (i in listView){
        i.isEnabled = false
        }
    }

    fun stopLoading(listView:ArrayList<View>,layout:View?=null){
        val mP = (mCtx as Activity).findViewById<View>(R.id.mProgress)
        mP.visibility = View.GONE
        if (layout!=null){
            layout!!.alpha = 1f
        }
        for (i in listView){
            i.isEnabled = true
        }
    }

    fun shareApp() {
        val url = "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        var shareBody = ""
        shareBody = "أشارك معك أفضل تطبيق لتفسير الأحلام في وقت قياسي" +
                "\n حمله عن طريق الرابط التالي "+
                "\n \n $url"
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareSub = "مشاركة مع الأصدقاء"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        mCtx!!.startActivity(Intent.createChooser(sharingIntent, "النشر على "))
    }

    fun rateApp(){
        val url = "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        mCtx!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun saveUser(user: User){
        val editShared = mSharedPreferences.edit()
        editShared.putString("user", Gson().toJson(user))
        editShared.apply()
    }

    fun getUser(): User? {
        val userJson = mSharedPreferences.getString("user", "")
        return if (userJson.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(userJson, object : TypeToken<User>() {}.type)
        }
    }


    fun saveConfig(config: Config){
        val editShared = mSharedPreferences.edit()
        editShared.putString("config", Gson().toJson(config))
        editShared.apply()
    }
    fun getConfig():Config{
        return Gson().fromJson(mSharedPreferences.getString("config",""), object : TypeToken<Config>() {}.type) as Config
    }



    fun goAnotherActivity(distinationActivity: Activity){
        val mAct = mCtx as Activity
        val intent = Intent(mCtx, distinationActivity::class.java)
        mAct.startActivity(intent)
        mAct.finish()
    }


    fun showLoading(listViews:ArrayList<View>?=null){
        try {
            val mAct = mCtx as Activity
            //SHOW LOADING CIRCLE
            val layoutLoading = mAct.findViewById<View>(R.id.layout_loading)
            //MAKE LOADING CIRCLE VISIBLE
            layoutLoading.visibility = View.VISIBLE
            val progressBar = mAct.findViewById<View>(R.id.spin_kit) as ProgressBar
            val doubleBounce: Sprite = Circle()
            progressBar.indeterminateDrawable = doubleBounce


            //Disable all the views (buttons / inputs)
            for (i in listViews!!){
                val v = i as View
                v.isEnabled = false
            }
        }catch (e:Exception){}

    }

    fun hideLoading(listViews:ArrayList<View>?=null){
        try {
            val mAct = mCtx as Activity

            //HIDE LOADING CIRCLE
            val layoutLoading = mAct.findViewById<View>(R.id.layout_loading)
            //MAKE LOADING CIRCLE INVISIBLE
            layoutLoading.visibility = View.GONE
            val progressBar = mAct.findViewById<View>(R.id.spin_kit) as ProgressBar
            val doubleBounce: Sprite = Circle()
            progressBar.indeterminateDrawable = doubleBounce

            //ENABLE all the views (kima kano)
            for (i in listViews!!){
                val v = i as View
                v.isEnabled = true
            }
        }catch (e:Exception){}
    }




}