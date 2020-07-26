package com.example.symphony.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,FirebaseService.class);
        context.stopService(intent1);
        Toast.makeText(context, "Messaging Service stopped", Toast.LENGTH_SHORT).show();
    }
}
