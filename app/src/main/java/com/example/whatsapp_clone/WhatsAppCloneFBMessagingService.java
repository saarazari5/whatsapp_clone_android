package com.example.whatsapp_clone;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class WhatsAppCloneFBMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Toast.makeText(this, "hello tokan!!!", Toast.LENGTH_LONG).show();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
