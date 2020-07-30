package com.example.symphony

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.asksira.bsimagepicker.BSImagePicker
import com.bumptech.glide.Glide
import com.example.symphony.Adapters.StatusAdapter
import com.example.symphony.Room.Model.Status
import com.example.symphony.Room.ViewModel.StatusViewModel
import com.example.symphony.Services.LocalUserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateStatus : AppCompatActivity(), BSImagePicker.OnSelectImageCancelledListener,
    BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.ImageLoaderDelegate {
    private val CHANNEL_3_ID = "channel3"

    private var back_button: ImageView? = null
    private var my_profile_image: CircleImageView? = null
    private var recyclerView: RecyclerView? = null
    private var statusAdapter: StatusAdapter? = null
    private var create_status_parent: RelativeLayout? = null
    private var add_status_image: ImageView? = null
    private lateinit var job: CompletableJob
    private var fab: FloatingActionButton? = null
    private var uri: Uri? = null
    private var auth: FirebaseAuth? = null
    private var My_Key: String? = null
    private var My_Phone: String? = null
    private var My_Name: String? = null
    private var status_edit: EditText? = null
    private var notificationManagerCompat: NotificationManagerCompat? = null
    private var sharedPreferences:SharedPreferences? = null
    private var statusViewModel: StatusViewModel? = null

    fun InitializeViewANDModels() {
        //Recycler View
        recyclerView = findViewById(R.id.recyclerView_status)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.setHasFixedSize(true)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        my_profile_image = findViewById(R.id.my_profile_image)
        back_button = findViewById(R.id.back_button)
        add_status_image = findViewById(R.id.add_status_image)
        create_status_parent = findViewById(R.id.create_status_parent)
        fab = findViewById(R.id.fab)
        status_edit = findViewById(R.id.status_edit)

        sharedPreferences= getSharedPreferences("LocalUser",0)
        //Objects Initialization
        statusViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(StatusViewModel::class.java)
        notificationManagerCompat = NotificationManagerCompat.from(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_status)
        InitializeViewANDModels()
        Listeners()
        SetViews()

    }


    fun SetViews() {
        auth = FirebaseAuth.getInstance()
        My_Key = LocalUserService.getLocalUserFromPreferences(this).Key
        My_Name = LocalUserService.getLocalUserFromPreferences(this).Name
        My_Phone = LocalUserService.getLocalUserFromPreferences(this).Phone
        Glide.with(this@CreateStatus)
            .load(LocalUserService.getLocalUserFromPreferences(this).ImageUrl)
            .into(my_profile_image!!)
    }

    fun Listeners() {
        back_button!!.setOnClickListener {
            onBackPressed()
        }
        add_status_image!!.setOnClickListener {
            val pickerDialog = BSImagePicker.Builder("com.example.symphony.fileprovider")
                .build()
            pickerDialog.show(supportFragmentManager, "picker")
        }
        fab!!.setOnClickListener {
            if (uri != null) {
                UploadImage(uri!!, status_edit!!.text.toString().trim())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val pickerDialog = BSImagePicker.Builder("com.example.symphony.fileprovider")
            .build()
        pickerDialog.show(supportFragmentManager, "picker")
    }

    override fun onCancelled(isMultiSelecting: Boolean, tag: String?) {
        Log.e("Cancelled TAG", tag)

    }

    override fun onSingleImageSelected(uri: Uri?, tag: String?) {
        if (uri != null) {
            this.uri = uri
            create_status_parent!!.visibility = View.VISIBLE
            statusAdapter = StatusAdapter(this, uri)
            recyclerView!!.adapter = statusAdapter
            statusAdapter!!.notifyDataSetChanged()
        }
    }

    override fun loadImage(imageUri: Uri?, ivImage: ImageView?) {
        Glide.with(this@CreateStatus).load(imageUri).into(ivImage!!)
    }

    fun UploadImage(imagUri: Uri, text: String) {
        job = Job()
        val storageRef = FirebaseStorage.getInstance().reference.child("Status").child(My_Key!!)
        CoroutineScope(Dispatchers.IO + job).launch {
            storageRef.putFile(imagUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        val date= SimpleDateFormat("dd MM yy hh:mm a").format( Date())
                        val hashMap: HashMap<String?, String?> = HashMap()
                        hashMap.put("Creator_Id", My_Key)
                        hashMap.put("Creator_Phone", My_Phone)
                        hashMap.put("Creator_Name", My_Name)
                        hashMap.put("Message", text)
                        hashMap.put("Create_Date",date)
                        hashMap.put("Status_Image_Url", it.toString())
                        FirebaseDatabase.getInstance().reference.child("Status").child(My_Key!!)
                            .setValue(hashMap)
                        statusViewModel!!.insert(Status(My_Key!!, My_Name!!,My_Phone!!, it.toString(), text, date,true))
                    }
                }.addOnProgressListener {
                    val max: Long = it.totalByteCount
                    val current: Long = it.bytesTransferred
                    val asd = max.toInt()
                    val asdCurrent = current.toInt()
                    val notification =
                        NotificationCompat.Builder(applicationContext, CHANNEL_3_ID)
                            .setSmallIcon(R.drawable.picker_icon_camera)
                            .setContentTitle("Uploading Status Image")
                            .setContentText("Progress")
                            .setOngoing(true)
                            .setOnlyAlertOnce(true)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setProgress(asd, asdCurrent, false).build()
                    notificationManagerCompat!!.notify(3, notification)

                }.addOnCompleteListener {
                    val notification =
                        NotificationCompat.Builder(applicationContext, CHANNEL_3_ID)
                            .setSmallIcon(R.drawable.picker_icon_camera)
                            .setContentTitle("Status Uploaded")
                            .setAutoCancel(true)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .build()
                    notificationManagerCompat!!.notify(3, notification)
                }
        }
    }

}