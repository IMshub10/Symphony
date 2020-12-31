package com.example.symphony

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.symphony.Models.ContactExists
import com.example.symphony.Room.Model.Status
import com.example.symphony.Room.ViewModel.ChatMessageViewModel
import com.example.symphony.Room.ViewModel.MyContactsViewModel
import com.example.symphony.Room.ViewModel.StatusViewModel
import com.example.symphony.Services.FirebaseService
import com.example.symphony.Services.LocalUserService
import com.example.symphony.Services.LocalUserService.getLocalUserFromPreferences
import com.example.symphony.Services.Tools
import com.example.symphony.ui.Main.SectionsPagerAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    val PERMISSION_READ_CONTACTS = 1
    var mAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null
    var my_profile_image: CircleImageView? = null
    var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    var myContactsViewModel: MyContactsViewModel? = null
    var chaViewModel: ChatMessageViewModel? = null
    var statusViewModel: StatusViewModel? = null
    var main_toolbar: androidx.appcompat.widget.Toolbar? = null
    var My_Key: String? = null
    var My_Phone: String? = null
    var My_Name: String? = null
    private lateinit var job: Job


    fun SetViews() {
        My_Key = LocalUserService.getLocalUserFromPreferences(this).Key
        My_Phone = LocalUserService.getLocalUserFromPreferences(this).Phone
        My_Name = LocalUserService.getLocalUserFromPreferences(this).Phone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SetViews()
        main_toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(main_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        val view = collapsingToolbarLayout!!.rootView
        my_profile_image = view.findViewById(R.id.my_profile_image)
        Glide.with(this)
            .asBitmap()
            .error(R.drawable.no_profile)
            .load(LocalUserService.getLocalUserFromPreferences(this).ImageUrl)
            .into(my_profile_image!!)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    fab.setImageResource(R.drawable.ic_baseline_email_24)
                    //Toast.makeText(this@MainActivity,"Chat Fragment Open",Toast.LENGTH_SHORT).show()
                } else if (tab.position == 1) {
                    // Toast.makeText(this@MainActivity,"Status Fragment Open",Toast.LENGTH_SHORT).show()
                    fab.setImageResource(R.drawable.picker_icon_camera)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fab.setOnClickListener { view ->
            if (tabs.selectedTabPosition == 0) {
                val intent1 = Intent(this, MyContacts::class.java)
                startActivity(intent1)
            } else if (tabs.selectedTabPosition == 1) {
                val intent1 = Intent(this, CreateStatus::class.java)
                startActivity(intent1)
            }
        }
        myContactsViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MyContactsViewModel::class.java)
        chaViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(ChatMessageViewModel::class.java)
        statusViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(StatusViewModel::class.java)
        startServing()
        getStatus()
    }

    fun startServing() {
        val serviceIntent = Intent(this, FirebaseService::class.java)
        serviceIntent.putExtra("MyName", LocalUserService.getLocalUserFromPreferences(this).Name)
        serviceIntent.putExtra("MyKey", LocalUserService.getLocalUserFromPreferences(this).Key)
        startService(serviceIntent)
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            val intent1 = Intent(this, SignIn::class.java)
            startActivity(intent1)
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            applicationContext
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(
                        Objects.requireNonNull(
                            getLocalUserFromPreferences(
                                applicationContext
                            ).Key
                        )!!
                    )
                    .child("Latitude").setValue(location.latitude)
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(
                        Objects.requireNonNull(
                            getLocalUserFromPreferences(
                                applicationContext
                            ).Key
                        )!!
                    )
                    .child("Longitude").setValue(location.longitude)
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_READ_CONTACTS
            )
        } else {
            if (Tools.isNetworkAvailable(this)) {
                insertContacts()
            } else {
                Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show()
            }
        }
        val nManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.cancel(2)

    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        startActivity(a)
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Tools.isNetworkAvailable(this)) {
                    insertContacts()
                } else {
                    Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun insertContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDatabase.getInstance().reference.child("Users")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val contactExists: ContactExists = contactExistsFun(
                            this@MainActivity,
                            snapshot.child("Phone Number").getValue().toString()
                        )
                        if (contactExists.exists) {
                            if (snapshot.key!! != My_Key) {
                                myContactsViewModel!!.insert(
                                    com.example.symphony.Room.Model.MyContacts(
                                        snapshot.key!!,
                                        contactExists.names,
                                        snapshot.child("Name").getValue().toString(),
                                        snapshot.child("Phone Number").getValue().toString(),
                                        snapshot.child("Profile Image").getValue().toString(),
                                        "",
                                        "",
                                        0,
                                        "", 1
                                    )
                                )
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }


                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                })
        }
    }

    fun contactExistsFun(context: Context, number: String?): ContactExists {
        // number is the phone number
        val lookupUri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val mPhoneNumberProjection = arrayOf(
            ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.NUMBER,
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )
        val cur: Cursor? =
            context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
        try {
            if (cur!!.moveToFirst()) {
                return ContactExists(
                    true,
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                )
            }
        } finally {
            cur?.close()
        }
        return ContactExists(
            false, "Nope"
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            LocalUserService.deleteLocalUserFromPreferences(this)
            myContactsViewModel!!.deleteAllMyContacts()
            statusViewModel!!.deleteAllStatus()
            // evenn user logs out store his old chats chaViewModel!!.deleteAlChatMessages()
            FirebaseAuth.getInstance().signOut()
            val serviceIntent = Intent(this, FirebaseService::class.java)
            stopService(serviceIntent)
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.maps){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.settings){
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
        return true
    }

    fun getStatus() {
        job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            FirebaseDatabase.getInstance().getReference().child("Status")
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        if (My_Phone != snapshot.child("Creator_Phone").value.toString()) {
                            val contactExists: ContactExists = contactExistsFun(
                                this@MainActivity,
                                snapshot.child("Creator_Phone").getValue().toString()
                            )
                            if (contactExists.exists) {
                                statusViewModel!!.update(
                                    Status(
                                        snapshot.key!!,
                                        contactExists.names,
                                        snapshot.child("Creator_Phone").value.toString(),
                                        snapshot.child("Status_Image_Url").value.toString(),
                                        snapshot.child("Message").value.toString(),
                                        snapshot.child("Create_Date").value.toString(),
                                        false
                                    )
                                )
                            }
                        }
                    }

                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (My_Phone != snapshot.child("Creator_Phone").value.toString()) {
                            val contactExists: ContactExists = contactExistsFun(
                                this@MainActivity,
                                snapshot.child("Creator_Phone").getValue().toString()
                            )
                            if (contactExists.exists) {
                                statusViewModel!!.insert(
                                    Status(
                                        snapshot.key!!,
                                        contactExists.names,
                                        snapshot.child("Creator_Phone").value.toString(),
                                        snapshot.child("Status_Image_Url").value.toString(),
                                        snapshot.child("Message").value.toString(),
                                        snapshot.child("Create_Date").value.toString(),
                                        false
                                    )
                                )
                            }
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        if (My_Phone != snapshot.child("Creator_Phone").value.toString()) {
                            val contactExists: ContactExists = contactExistsFun(
                                this@MainActivity,
                                snapshot.child("Creator_Phone").value.toString()
                            )
                            if (contactExists.exists) {
                                statusViewModel!!.deleteByKey(snapshot.key!!)
                            }
                        }
                    }

                })
        }
    }
}