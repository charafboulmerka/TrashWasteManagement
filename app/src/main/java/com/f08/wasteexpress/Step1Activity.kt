package com.f08.wasteexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.astritveliu.boom.utils.BoomUtils
import com.f08.wasteexpress.Utils.mUtils
import kotlinx.android.synthetic.main.activity_step1.*
import kotlinx.android.synthetic.main.header.*
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class Step1Activity : AppCompatActivity() {
    var orderType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step1)
        mUtils(this)
        BoomUtils.boomAll(btn_step1)
        header_title.text = "Nouvelle offre"

        btn_step1.setOnClickListener {
            if (orderType==0){
                MotionToast.createColorToast(
                    this,
                    "Erreur",
                    "Vous devez sélectionner un type de déchet et réessayer",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )

            }else{
                mUtils(this).saveTrashType(orderType)
                startActivity(Intent(this,Step2Activity::class.java))
            }

        }
    }

    fun clickCard(view: View) {
        // Reset all radio buttons
        resetRadioButtons()

        // Set the appropriate order type and radio button based on the view clicked
        when (view.id) {
            R.id.card_order_type1, R.id.radio_btn_type1 -> setOrderType(1)
            R.id.card_order_type2, R.id.radio_btn_type2 -> setOrderType(2)
            R.id.card_order_type3, R.id.radio_btn_type3 -> setOrderType(3)
            R.id.card_order_type4, R.id.radio_btn_type4 -> setOrderType(4)
            R.id.card_order_type5, R.id.radio_btn_type5 -> setOrderType(5)

        }
    }

     fun resetRadioButtons() {
        radio_btn_type1.isChecked = false
        radio_btn_type2.isChecked = false
        radio_btn_type3.isChecked = false
        radio_btn_type4.isChecked = false
        radio_btn_type5.isChecked = false

     }

     @JvmName("setOrderType1")
     fun setOrderType(type: Int) {
        orderType = type
        when (type) {
            1 -> radio_btn_type1.isChecked = true
            2 -> radio_btn_type2.isChecked = true
            3 -> radio_btn_type3.isChecked = true
            4 -> radio_btn_type4.isChecked = true
            5 -> radio_btn_type5.isChecked = true
        }
    }

}

