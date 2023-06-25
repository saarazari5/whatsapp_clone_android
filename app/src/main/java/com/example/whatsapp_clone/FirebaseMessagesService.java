package com.example.whatsapp_clone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.whatsapp_clone.Views.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessagesService extends FirebaseMessagingService {
    private final String CHANEL_ID = "channelId";
//    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
//    private static final String SERVER_KEY = "key=BMdZXFMN14ryAopqIdywQGkGCF9bzHGJMWjNjQ5jzmpfvTkiM_Qkpjuv_Z0TErijxIcthm1ooxyltBqSnd3Y7UE";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            generateNotification(message.getFrom(), message.getNotification().getBody());
        }
    }

    private void generateNotification(String from, String messageBody) {
        pushNotification(from, messageBody);

        // Create an intent to open the app and navigate to the login fragment
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the fragment identifier as an extra
        intent.putExtra("fragment", "login");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create a notification builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(from)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Inflate the custom notification layout
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);
        notificationView.setTextViewText(R.id.notification_title, from);
        notificationView.setTextViewText(R.id.notification_message, messageBody);

        // Set the custom notification view
        notificationBuilder.setCustomContentView(notificationView);

        // Create a notification manager to display the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Generate a unique notification ID
            int notificationId = (int) System.currentTimeMillis();
            // Display the notification
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
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

    private void pushNotification(String from, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANEL_ID)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(from)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,"Chatgram channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

//    public static void sendNotification(Context context, String receiverToken, String from, String msg) {
//        try {
//            JSONObject notificationData = new JSONObject();
//            notificationData.put("title", from);
//            notificationData.put("body", msg);
//
//            JSONObject notification = new JSONObject();
//            notification.put("to", receiverToken);
//            notification.put("notification", notificationData);
//
//            // Create a new RequestQueue
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//            // Create a new JsonObjectRequest with POST method
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                    BASE_URL, notification, response -> {
//                        // Notification sent successfully
//                        Log.d("Notification", "Notification sent");
//                    }, error -> {
//                        // Error occurred while sending the notification
//                        Log.e("Notification", "Notification sending failed: " + error.getMessage());
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    // Set the request headers, including the Authorization header with your server key
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("Authorization", SERVER_KEY);
//                    headers.put("Content-Type", "application/json");
//                    return headers;
//                }
//            };
//
//            // Add the JsonObjectRequest to the RequestQueue
//            requestQueue.add(jsonObjectRequest);
//        } catch (JSONException e) {
//            Log.e("Notification", "Failed to create notification JSON: " + e.getMessage());
//        }
//    }

    //    private void sendNotification(String from, String body) {
//        new android.os.Handler(Looper.getMainLooper()).post(() ->
//                Toast.makeText(FirebaseMessagesService.this.getApplicationContext(),
//                        from + " -> " + body, Toast.LENGTH_LONG));
//    }

//    public static void sendNotification(Context context, String token, String from, String msg) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//    }

//    private void sendNotification(String from, String message) {
//        String receiverToken = "";
//        JSONObject notificationInner = new JSONObject();
//        JSONObject notification = new JSONObject();
//        try {
//            notificationInner.put("title", receiverToken);
//            notificationInner.put("body", message);
//            notification.put("to", receiverToken);
//            notification.put("notification", notificationInner);
//        } catch (JSONException e) {
//            Log.d("NOTI", e.toString());
//            throw new RuntimeException(e);
//        }
//    }
}
