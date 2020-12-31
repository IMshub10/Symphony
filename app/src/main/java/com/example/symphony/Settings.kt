package com.example.symphony

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.symphony.Services.LocalUserService.getLocalUserFromPreferences
import com.example.symphony.Services.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.temp.*


@Suppress("DEPRECATION")
class Settings : AppCompatActivity() {

    final val FINE_LOCATION_CODE = 111
    final val COARSE_LOCATION_CODE = 112

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        location_switch.isChecked = isMyServiceRunning(LocationService::class.java)
        location_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                startLocationService()
            } else {
                FirebaseDatabase.getInstance().reference.child("Users").child(
                    getLocalUserFromPreferences(this@Settings).Key!!
                ).child("Latitude").setValue("0.0")
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(getLocalUserFromPreferences(this@Settings).Key!!).child("Longitude")
                    .setValue("0.0")
                stopLocationService()
            }
        }

    }
    private fun startLocationService(){
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
    }
    private fun stopLocationService(){
        val serviceIntent = Intent(this, LocationService::class.java)
        stopService(serviceIntent)
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (services in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == services.service.className) {
                return true
            }
        }
        return false
    }
}