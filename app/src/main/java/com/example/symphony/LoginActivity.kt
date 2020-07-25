package com.example.symphony

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.symphony.Services.LocalUserService
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    var login:Button? =null
    var intent1: Intent? = null
    var currentUser: FirebaseUser? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login=findViewById(R.id.login)

        FirebaseApp.initializeApp(applicationContext)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser

        login!!.setOnClickListener { v->
            intent1 = Intent(this, SignIn::class.java)
            startActivity(intent1)
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null
            && LocalUserService.getLocalUserFromPreferences(this).Name!=null
            && LocalUserService.getLocalUserFromPreferences(this).ImageUrl!=null
            && LocalUserService.getLocalUserFromPreferences(this).Phone!=null
            && LocalUserService.getLocalUserFromPreferences(this).Key!=null){
            intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }
    }
}