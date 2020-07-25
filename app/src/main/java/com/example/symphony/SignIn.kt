package com.example.symphony

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.symphony.Services.LocalUserService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignIn : AppCompatActivity() {
    var sendotp: Button? = null
    var verify: Button? = null
    var phone: EditText? = null
    var countrycode: EditText? = null
    var otp: EditText? = null
    var codeSent: String? = null

    var mAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null

    var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sendotp = findViewById(R.id.sendotp)
        verify = findViewById(R.id.verify)
        phone = findViewById(R.id.phone)
        countrycode = findViewById(R.id.countrycode)
        otp = findViewById(R.id.otp)

        phone!!.requestFocus()

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        sharedPreferences = getSharedPreferences("LocalUser", 0)

        sendotp!!.setOnClickListener { v: View? ->
            sendVerficationcode()
        }

        verify!!.setOnClickListener { v: View? ->
            val credential = PhoneAuthProvider.getCredential(codeSent!!, otp!!.text.toString())
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun sendVerficationcode() {
        if (phone!!.text.toString().length < 10 || phone!!.text.toString().length > 10) {
            phone!!.error = "Phone no. not valid"
            phone!!.requestFocus()
            return
        }
        if (phone!!.text.toString().isEmpty()) {
            phone!!.error = "Phone no. not valid"
            phone!!.requestFocus()
            return
        }
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(countrycode!!.text.toString() + phone!!.text.toString(),
                60,
                TimeUnit.SECONDS,
                this,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}
                    override fun onCodeSent(
                        s: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(s, forceResendingToken)
                        Toast.makeText(this@SignIn, "Otp has been sent.", Toast.LENGTH_SHORT).show()
                        codeSent = s
                    }

                    override fun onVerificationFailed(e: FirebaseException) {}
                })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                login()
            } else {
                if (task.exception is FirebaseAuthActionCodeException) {
                    Toast.makeText(this@SignIn, "Wrong Verification Code", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun login() {
        val intent = Intent(this@SignIn, Info::class.java)
        intent.putExtra("countrycode", countrycode!!.text.toString())
        intent.putExtra("phone", phone!!.text.toString())
        sharedPreferences!!.edit()
            .putString("Phone", countrycode!!.text.toString() + phone!!.text.toString()).apply()
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignIn, MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        if (currentUser != null
            && LocalUserService.getLocalUserFromPreferences(this).Name != null
            && LocalUserService.getLocalUserFromPreferences(this).ImageUrl != null
            && LocalUserService.getLocalUserFromPreferences(this).Phone != null
            && LocalUserService.getLocalUserFromPreferences(this).Key != null
        ) {
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }
    }
}