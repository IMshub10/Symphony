package com.example.symphony

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.symphony.Adapters.MyContactsAdapter
import com.example.symphony.Models.ContactExists
import com.example.symphony.Room.Model.MyContacts
import com.example.symphony.Room.ViewModel.MyContactsViewModel
import com.example.symphony.Services.Tools
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class MyContacts : AppCompatActivity() {
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    val PERMISSION_READ_CONTACTS = 1
    var myContactsViewModel: MyContactsViewModel? = null
    var recyclerView: RecyclerView? = null
    var myContactsAdapter: MyContactsAdapter? = null

    fun init() {
        //Toolbar
        toolbar = findViewById(R.id.toolBar_my_contacts)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        //Recycler View
        recyclerView = findViewById(R.id.recyclerView)
        myContactsAdapter = MyContactsAdapter(this)
        recyclerView!!.adapter = myContactsAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.setHasFixedSize(true)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        //MyContacts View Model Initialization
        myContactsViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MyContactsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_contacts)
        init()
        toolbar!!.setNavigationOnClickListener {
            onBackPressed()
        }
        myContactsViewModel!!.getAllMyContacts().observe(this, Observer { myContactList ->
            myContactsAdapter!!.submitList(myContactList)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.my_contacts_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
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
                }else{
                    Toast.makeText(this,"Network not available",Toast.LENGTH_SHORT).show()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
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
                }else{
                    Toast.makeText(this,"Network not available",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun insertContacts() {
        CoroutineScope(IO).launch {
            FirebaseDatabase.getInstance().reference.child("Users")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val contactExists: ContactExists = contactExistsFun(
                            this@MyContacts,
                            snapshot.child("Phone Number").getValue().toString()
                        )
                        if (contactExists.exists) {
                            myContactsViewModel!!.insert(
                                MyContacts(
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

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        TODO("Not yet implemented")
                    }


                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        TODO("Not yet implemented")
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
}