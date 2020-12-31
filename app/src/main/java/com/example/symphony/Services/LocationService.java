package com.example.symphony.Services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.symphony.MainActivity;
import com.example.symphony.R;
import com.example.symphony.Settings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class LocationService extends Service {

    public static final String CHANNEL_LOCATION = "location_channel";
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback mLocationCallback;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, Settings.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);
        Intent broadCastIntent = new Intent(this, LocationReceiver.class);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_LOCATION)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle("Location Service")
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(110, notification);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location mLocation = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + mLocation.getLatitude() + " " + mLocation.getLongitude());
                    mLastLocation = mLocation;
                }
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //10 secs interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        }

        /*
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!= null){

                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Objects.requireNonNull(LocalUserService.getLocalUserFromPreferences(getApplicationContext()).getKey()))
                            .child("Latitude").setValue(location.getLatitude());
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Objects.requireNonNull(LocalUserService.getLocalUserFromPreferences(getApplicationContext()).getKey()))
                            .child("Longitude").setValue(location.getLongitude());
                }
            }
        });
        */
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
