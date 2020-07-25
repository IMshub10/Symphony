package com.example.symphony.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.symphony.R;
import com.example.symphony.Room.Model.ChatMessage;
import com.example.symphony.Room.ViewModel.ChatMessageViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FirebaseService extends Service {
    private String CHANNEL_1_ID = "channel1";

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
        String My_Name = intent.getStringExtra("MyName");
        String My_Key = intent.getStringExtra("MyKey");

        Notification notification = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle("Messaging Service")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        startForeground(1, notification);
        ChatMessageViewModel chatMessageViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ChatMessageViewModel.class);
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
