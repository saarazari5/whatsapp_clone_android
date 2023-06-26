package com.example.whatsapp_clone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.whatsapp_clone.Model.Events.AddEvent;
import com.example.whatsapp_clone.Model.Events.AddMessageEvent;
import com.example.whatsapp_clone.Model.Events.DeleteEvent;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Views.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FirebaseMessagesService extends FirebaseMessagingService {

    private final String CHANEL_ID = "channelId";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        RemoteMessage.Notification notification = message.getNotification();
        // Check if message contains a notification payload.
        if (notification != null) {
            generateNotification(notification.getTitle(), notification.getBody());
        }

        Map<String, String> data = message.getData();

        if(data.size() > 0) {
            String type = data.get("type");
            if(type == null) {return;}

            if(type.equals("message")) {
                int chatId = Integer.parseInt(Objects.requireNonNull(data.get("chatId")));
                int messageId = Integer.parseInt(Objects.requireNonNull(data.get("id")));
                String senderName = data.get("sender");
                String displayName = data.get("displayName");
                String content = data.get("content");
                String created = data.get("created");

                MessageEntity messageEntity = new MessageEntity(messageId,
                        new User(senderName, displayName,""),
                        content,
                        created,
                        chatId
                );

                EventBus.getDefault().post(new AddMessageEvent(messageEntity));
            } else if(type.equals("delete")) {
                EventBus.getDefault().post(new DeleteEvent());

            } else if(type.equals("add")) {
                EventBus.getDefault().post(new AddEvent());

            }

        }
    }

    private void generateNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANEL_ID)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences prefs = getSharedPreferences("fcmTokens", MODE_PRIVATE);
        prefs.edit().putString("fcmToken", token).apply();
    }
}