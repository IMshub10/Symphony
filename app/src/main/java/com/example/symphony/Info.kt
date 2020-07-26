package com.example.symphony

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.asksira.bsimagepicker.BSImagePicker
import com.bumptech.glide.Glide
import com.example.symphony.Services.LocalUserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class Info : AppCompatActivity(), BSImagePicker.OnSelectImageCancelledListener,
    BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.ImageLoaderDelegate {
    var select_image: CircleImageView? = null
    var temp_name: EditText? = null
    var toolbar: Toolbar? = null
    var info_progress: ProgressBar? = null
    var upload_uri: Uri? = null
    var downloadUrl: String? = null

    var mAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null
    var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_class)

        select_image = findViewById(R.id.select_image)
        temp_name = findViewById(R.id.temp_name)
        info_progress = findViewById(R.id.info_progress)

        toolbar = findViewById(R.id.toolbar_info)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)


        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        sharedPreferences = getSharedPreferences("LocalUser", 0)

        select_image!!.setOnClickListener { v: View? ->
            val pickerDialog =
                BSImagePicker.Builder("com.example.symphony.fileprovider")
                    .build()
            pickerDialog.show(supportFragmentManager, "picker")
        }
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser != null
            && LocalUserService.getLocalUserFromPreferences(this).Name!=null
            && LocalUserService.getLocalUserFromPreferences(this).ImageUrl!=null
            && LocalUserService.getLocalUserFromPreferences(this).Phone!=null
            && LocalUserService.getLocalUserFromPreferences(this).Key!=null){
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }
    }

    override fun onCancelled(isMultiSelecting: Boolean, tag: String?) {
        Toast.makeText(
            this,
            "Selection is cancelled",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSingleImageSelected(uri: Uri?, tag: String?) {
        Glide.with(this@Info).load(uri).into(select_image!!)
        upload_uri = uri
    }

    override fun loadImage(imageUri: Uri?, ivImage: ImageView?) {
        Glide.with(this@Info).load(imageUri).into(ivImage!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tick) {
            if (temp_name!!.text.toString().trim() != "") {
                uploadImage(upload_uri)
            } else {
                Toast.makeText(this, "Text can't be empty", Toast.LENGTH_SHORT).show()
            }
            //val uploadTask:UploadTask? =null
            //uploadTask!!.execute()
        }
        return true
    }

    private fun uploadImage(uri: Uri?) {
        info_progress!!.visibility = View.VISIBLE
        val storageReference: StorageReference?
        storageReference =
            FirebaseStorage.getInstance().reference.child("Users").child("ProfilePhotos")
                .child(mAuth!!.currentUser!!.uid)
        if (uri == null) {
            val map: HashMap<String, String?> = hashMapOf(
                "Phone Number" to LocalUserService.getLocalUserFromPreferences(
                    applicationContext
                ).Phone,
                "Name" to temp_name!!.text.toString(),
                "Profile Image" to "qwerty"
            )
            FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth!!.currentUser!!.uid).setValue(map)
            info_progress!!.visibility = View.INVISIBLE
            sharedPreferences!!.edit().putString("ImageUrl", "qwerty").apply()
            sharedPreferences!!.edit().putString("Key", mAuth!!.currentUser!!.uid).apply()
            sharedPreferences!!.edit().putString("Name", temp_name!!.text.toString()).apply()
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)

        } else {
            storageReference.putFile(uri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri: Uri? ->
                        downloadUrl = uri.toString()
                        val map: HashMap<String, String?> = hashMapOf(
                            "Phone Number" to LocalUserService.getLocalUserFromPreferences(
                                applicationContext
                            ).Phone,
                            "Name" to temp_name!!.text.toString(),
                            "Profile Image" to downloadUrl
                        )
                        FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(mAuth!!.currentUser!!.uid).setValue(map)
                        info_progress!!.visibility = View.INVISIBLE
                        sharedPreferences!!.edit().putString("ImageUrl", downloadUrl).apply()
                        sharedPreferences!!.edit().putString("Key", mAuth!!.currentUser!!.uid).apply()
                        sharedPreferences!!.edit().putString("Name", temp_name!!.text.toString()).apply()
                        val intent1 = Intent(this, MainActivity::class.java)
                        startActivity(intent1)

                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent1 = Intent(this, SignIn::class.java)
        startActivity(intent1)
    }
}

