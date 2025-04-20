package com.f08.wasteexpress

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.f08.wasteexpress.Models.Config
import com.f08.wasteexpress.Utils.mUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MySplash : AppCompatActivity() {

    private lateinit var imgLogo : ImageView
    var hasAnimationStarted:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_splash)
        imgLogo = findViewById(R.id.mSplash)
    }




    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        try{
            if (hasFocus && !hasAnimationStarted) {
                hasAnimationStarted = true
                val metrics = resources.displayMetrics
                val translationY = ObjectAnimator.ofFloat(
                    imgLogo,
                    "y",
                    metrics.heightPixels / 2 - imgLogo.height.toFloat() / 2
                ) // metrics.heightPixels or root.getHeight()
                translationY.duration = 1000
                translationY.start()
                translationY.doOnEnd {
                    getConfig()
                }
            }
        }catch (e:Exception){}

    }


    fun getConfig(){
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().reference.child("config")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val config = snapshot.getValue(Config::class.java)
                    mUtils(this@MySplash).saveConfig(config!!)
                    if (mUtils(this@MySplash).getUser() != null){
                        mUtils(this@MySplash).goAnotherActivity(MainActivity())
                    }else{
                        mUtils(this@MySplash).goAnotherActivity(LoginActivity())
                    }
                    /*if (config.force_exit==0){
                        Utils(this@MySplash).goAnotherActivity(MainActivity())
                    }else{
                        Utils(this@MySplash).AlertExit(config.exit_text!!,config.destination_url!!)
                    }*/
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }


}
