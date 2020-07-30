package com.example.symphony;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";


    @Override
    public void onCreate() {
        super.onCreate();
        /*
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);

         */
        //Normal app init code
        createNotificationChannel();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Back Ground Service", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Back Ground Service");
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Messaging", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Messaging");
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Progress", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Progress");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}