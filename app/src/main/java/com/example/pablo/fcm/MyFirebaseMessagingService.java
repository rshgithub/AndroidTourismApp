

package com.example.pablo.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.activity.MainActivity;
import com.example.pablo.details_activities.HotelOrdersDetails;
import com.example.pablo.details_activities.HotelsDetails;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static SharedPreferences SP;    // to read from SharedPreferences
    public static SharedPreferences.Editor EDIT;
    public static final String PREF_NAME = "token";
    public static final String fcmToken = "Token_K";
    String TAG = "MyFirebaseMessagingService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message_data_payload: " + remoteMessage.getData());
            Map<String, String> params = remoteMessage.getData();

            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());
            try {
                if (object.has("order_id")) {
                    Long postId = object.getLong("order_id");
                    if (remoteMessage.getNotification() != null)
                        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), postId );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    @Override
    public void onNewToken(String token) {
        Log.e("onNewToken",token);


        SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        EDIT = SP.edit();
        EDIT.putString(fcmToken,token);
        EDIT.apply();



        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    //This method is only generating push notification
    private void sendNotification(String messageTitle, String messageBody, Long type) {

        PendingIntent contentIntent = null;
        Intent intent ;

        intent = new Intent(this, HotelOrdersDetails.class);
        intent.putExtra("order_id",type+"");
        Log.e("order_id3",type+"");


        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        CharSequence tickerText = messageBody;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(tickerText));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            String channelId = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(channelId, messageTitle, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messageBody);
            channel.enableVibration(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            MediaPlayer mp= MediaPlayer.create(getBaseContext(), R.raw.notification);
            mp.start();
            channel.setSound(sound, audioAttributes);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(0, notificationBuilder.build());
//        count++;
    }



}

