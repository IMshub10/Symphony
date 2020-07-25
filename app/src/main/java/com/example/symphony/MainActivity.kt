package com.example.symphony

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.symphony.Services.FirebaseService
import com.example.symphony.Services.LocalUserService
import com.example.symphony.ui.Main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position==0){
                    fab.setImageResource(R.drawable.ic_baseline_email_24)
                    //Toast.makeText(this@MainActivity,"Chat Fragment Open",Toast.LENGTH_SHORT).show()
                }else if (tab.position==1){
                   // Toast.makeText(this@MainActivity,"Status Fragment Open",Toast.LENGTH_SHORT).show()
                    fab.setImageResource(R.drawable.picker_icon_camera)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fab.setOnClickListener { view ->
            if (tabs.selectedTabPosition==0) {
                val intent1 = Intent(this, MyContacts::class.java)
                startActivity(intent1)
            }else if (tabs.selectedTabPosition==1) {
                Snackbar.make(view, "Status Fragment Open", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            val intent1: Intent = Intent(this, SignIn::class.java)
            startActivity(intent1)
        }
        val serviceIntent=Intent(this,FirebaseService::class.java)
        serviceIntent.putExtra("MyName",LocalUserService.getLocalUserFromPreferences(this).Name)
        serviceIntent.putExtra("MyKey",LocalUserService.getLocalUserFromPreferences(this).Key)
        startService(serviceIntent)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        startActivity(a)
        super.onBackPressed()
    }
}