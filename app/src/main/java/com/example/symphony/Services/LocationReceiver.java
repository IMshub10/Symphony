package com.example.symphony.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,LocationService.class);
        context.stopService(intent1);

        Toast.makeText(context, "Location Service stopped", Toast.LENGTH_SHORT).show();
    }
}
