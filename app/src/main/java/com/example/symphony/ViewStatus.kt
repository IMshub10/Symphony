package com.example.symphony

import android.R.id.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.symphony.Room.ViewModel.MyContactsViewModel
import com.example.symphony.Room.ViewModel.StatusViewModel
import com.example.symphony.Services.LocalUserService
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_view_status.*
import kotlinx.coroutines.*


class ViewStatus : AppCompatActivity() {
    var back_button: ImageView? = null
    var profile_image: CircleImageView? = null
    var status_image: ImageView? = null
    var Friend_Key: String? = null
    var Status_Image: String? = null
    var Create_Date: String? = null
    var statusViewModel: StatusViewModel? = null
    var contactsViewModel: MyContactsViewModel? = null
    var view_status_text: TextView? = null
    var view_status_time_textView: TextView? = null




    fun InitializeViews() {
        back_button = findViewById(R.id.back_button)
        profile_image = findViewById(R.id.profile_image)
        status_image = findViewById(R.id.status_image)
        view_status_text = findViewById(R.id.view_status_text)
        view_status_time_textView = findViewById(R.id.view_status_time_textView)
        statusViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(StatusViewModel::class.java)
        contactsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MyContactsViewModel::class.java)
        progress.max=10000
        progress.progress=0
    }

    fun GetFromIntent() {
        Friend_Key = intent.getStringExtra("Friend_Key")
        Status_Image = intent.getStringExtra("Status_Image")
        Create_Date = intent.getStringExtra("Create_Date")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_status)
        GetFromIntent()
        InitializeViews()
        SetViews()
        Listeners()

    }

    fun SetViews() {
        Glide.with(this)
            .load(Status_Image)
            .into(status_image!!)
        view_status_time_textView!!.text = Create_Date
        getFriendContact()

    }

    fun getFriendContact() {
        if (Friend_Key == LocalUserService.getLocalUserFromPreferences(this).Key) {
            Glide.with(this)
                .load(LocalUserService.getLocalUserFromPreferences(this).ImageUrl)
                .error(R.drawable.no_profile)
                .into(profile_image!!)
        }
        contactsViewModel!!.getFriendContact(Friend_Key!!).observe(this, Observer {
            if (it != null) {
                view_status_text!!.text = it.f_name
                Glide.with(this)
                    .load(it.profileImage).error(R.drawable.no_profile)
                    .into(profile_image!!)
            }
        })
    }

    override fun onStart() {
        super.onStart()

        statusViewModel!!.updateSeen(Friend_Key!!, true)
        object : CountDownTimer(10000, 10) {
            override fun onTick(l: Long) {
                progress.progress=(10000-l.toInt())
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                onBackPressed()
            }
        }.start()

    }
    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun Listeners() {
        back_button!!.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}