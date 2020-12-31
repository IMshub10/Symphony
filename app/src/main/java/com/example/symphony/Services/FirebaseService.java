package com.example.symphony.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.symphony.ChatActivity;
import com.example.symphony.MainActivity;
import com.example.symphony.R;
import com.example.symphony.Room.Model.ChatMessage;
import com.example.symphony.Room.ViewModel.ChatMessageViewModel;
import com.example.symphony.Room.ViewModel.MyContactsViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class FirebaseService extends Service {
    private static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        String My_Name = intent.getStringExtra("MyName");
        String My_Key = intent.getStringExtra("MyKey");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Intent broadCastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle("Messaging Service")
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.common_full_open_on_phone, "STOP", actionIntent)
                .build();
        startForeground(1, notification);
        ChatMessageViewModel chatMessageViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ChatMessageViewModel.class);
        MyContactsViewModel myContactsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MyContactsViewModel.class);
        if (My_Key != null) {
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(My_Key).child("Personal Chats")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (My_Name != null) {
                                chatMessageViewModel.insert(new ChatMessage(Objects.requireNonNull(snapshot.getKey()),
                                        Objects.requireNonNull(snapshot.child("SenderKey").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("SenderName").getValue()).toString(),
                                        My_Key,
                                        My_Name,
                                        "",
                                        Objects.requireNonNull(snapshot.child("Create Date").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("Message").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("Timestamp").getValue()).toString(),
                                        2));
                                myContactsViewModel.updateContact(Objects.requireNonNull(snapshot.child("SenderKey").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("Message").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("Create Date").getValue()).toString(),
                                        Objects.requireNonNull(snapshot.child("Timestamp").getValue()).toString());
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(My_Key).child("Personal Chats").child(snapshot.getKey()).removeValue();
                                try {
                                    Uri notificationRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationRing);
                                    r.play();
                                    Intent notificationIntent1 = new Intent(getApplicationContext(), ChatActivity.class);
                                    notificationIntent1.putExtra("Friend_Key", Objects.requireNonNull(snapshot.child("SenderKey").getValue()).toString());
                                    notificationIntent1.putExtra("First_Name", Objects.requireNonNull(snapshot.child("SenderName").getValue()).toString());
                                    notificationIntent1.putExtra("Last_Name",Objects.requireNonNull(snapshot.child("SenderName").getValue()).toString());
                                    notificationIntent1.putExtra("Phone",Objects.requireNonNull(snapshot.child("ReceiverPhone").getValue()).toString());
                                    notificationIntent1.putExtra("Profile_Image",Objects.requireNonNull(snapshot.child("ReceiverImage").getValue()).toString());
                                    PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent1, 0);
                                    Notification notification1 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                                            .setSmallIcon(R.drawable.ic_baseline_photo_camera_24)
                                            .setContentTitle("New Message From " + Objects.requireNonNull(snapshot.child("SenderName").getValue()).toString())
                                            .setContentText(Objects.requireNonNull(snapshot.child("Message").getValue()).toString())
                                            .setContentIntent(pendingIntent1)
                                            .setAutoCancel(true)
                                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                            .build();

                                    notificationManagerCompat.notify(2, notification1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
