package com.f08.wasteexpress

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astritveliu.boom.utils.BoomUtils
import com.f08.wasteexpress.Adapters.trashAdapter
import com.f08.wasteexpress.Models.Trash
import com.f08.wasteexpress.Models.User
import com.f08.wasteexpress.Utils.mUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.*
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var ViewlayoutManager: RecyclerView.LayoutManager


    private var mList = ArrayList<Trash>()
    private lateinit var mRef: DatabaseReference

    private lateinit var viewsList:ArrayList<View>

    private lateinit var mMenu : Menu

    lateinit var mUtils: mUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = null
        toolbar.setTitleTextColor(resources.getColor(R.color.black))

        viewsList= arrayListOf()
        mRef = FirebaseDatabase.getInstance().reference.child("trashs")
        mUtils = mUtils(this)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = true

        drawer.addDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(false)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        mBottomNavigation.setOnItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {
                    R.id.bottomn_nav_home -> {

                    }

                    R.id.bottom_nav_fav -> {
                    //startActivity(Intent(this@MainActivity,FavActivity::class.java))
                    }

                    R.id.bottom_nav_mydreams -> {
                        //startActivity(Intent(this@MainActivity,MyDreams::class.java))
                    }
                }
                return false
            }

        })

        BoomUtils.boomAll(fab,btnAddP,btnTrashFilter)

        fab.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            }
            else{
                drawer.openDrawer(GravityCompat.START)
            }
        }


        btnAddP.setOnClickListener {
            startActivity(Intent(this, Step1Activity::class.java))
        }



        ViewlayoutManager = LinearLayoutManager(this)
        viewAdapter = trashAdapter(this, mList)

        recyclerView = findViewById<RecyclerView>(R.id.mRec).apply {
            setHasFixedSize(true)
            ViewlayoutManager.isAutoMeasureEnabled = false
            layoutManager = ViewlayoutManager
            adapter = viewAdapter
        }

        setUserName()
        getTrashs()

    }


    fun setUserName(){
        if (FirebaseAuth.getInstance().currentUser!=null){
            FirebaseDatabase.getInstance().reference.child("users").child(mUtils(this).getDeviceIdOrUID())
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val mUser = p0.getValue(User::class.java)!!
                            mUtils.saveUser(mUser)
                            val navigationView = findViewById<NavigationView>(R.id.nav_view)
                            val menu = navigationView.menu
                            val nav_login_view = menu.findItem(R.id.nav_login)
                            val nav_add_admin_view = menu.findItem(R.id.nav_admin)

                            username.text = "${mUser.first_name} ${mUser.last_name}"


                            nav_login_view.title = "Déconnexion"

                            nav_login_view.setIcon(R.drawable.ic_logout)
                            nav_add_admin_view.isVisible = mUser.email == "admin@gmail.com"

                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }

                })

        }
    }

    fun getTrashs() {
        mList.clear()
        mUtils(this).runLoading(viewsList,main_layout)
        mRef.orderByChild("publishedDateMs").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()){
                    tv_no_data.visibility = View.VISIBLE

                    
                    mUtils(this@MainActivity).stopLoading(viewsList,main_layout)
                }
                for (trash in p0.children) {
                    val mTrash = trash.getValue(Trash::class.java)!!
                    mList.add(mTrash)
                    mUtils(this@MainActivity).stopLoading(viewsList,main_layout)
                }
                mList.reverse()

                viewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(p0: DatabaseError) {
                mUtils(this@MainActivity).stopLoading(viewsList,main_layout)
            }

        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_login -> {
                if (FirebaseAuth.getInstance().currentUser==null){
                    startActivity(Intent(this, LoginActivity::class.java))
                }else{
                    FirebaseAuth.getInstance().signOut()
                    mUtils(this).clearAllData()
                    MotionToast.createColorToast(this,
                        "Opération réussie",
                        "Vous avez été déconnecté",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null)

                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
            }

            R.id.nav_admin -> {
                startActivity(Intent(this, AdminActivity::class.java))
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu
         menuInflater.inflate(R.menu.navigation, menu)
        return false
    }
}