package com.f08.wasteexpress

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.astritveliu.boom.utils.BoomUtils
import com.f08.wasteexpress.Models.Trash
import com.f08.wasteexpress.Utils.mUtils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.content_navigation.*
import kotlinx.android.synthetic.main.header.*

class AdminActivity : AppCompatActivity() {
    private lateinit var mRef: DatabaseReference
    lateinit var mUtils: mUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        mRef = FirebaseDatabase.getInstance().reference
        mUtils = mUtils(this)
        header_title.text = "Panneau d'administration"
        BoomUtils.boomAll(trash_edt_buying_price,trash_edt_selling_fee,btn_show_orders)
        mRef.child("users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val nUserCount = p0.children.count()
                analytics_users.setCurrentProgress(nUserCount.toDouble())

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        mRef.child("trashs").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val mTrashCount = p0.children.count()
                analytics_offers.setCurrentProgress(mTrashCount.toDouble())

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        mRef.child("orders").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val mOrdersCount = p0.children.count()
                analytics_orders.setCurrentProgress(mOrdersCount.toDouble())

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        analytics_orders_completed.setCurrentProgress(0.0)

        trash_config_buying_price.setText("Prix d'achat : ${mUtils.getConfig().buying_price_per_kg}DA / KG")
        trash_config_selling_fee.setText("Commission : ${mUtils.getConfig().selling_fee_per_kg}DA / KG")


    }



}