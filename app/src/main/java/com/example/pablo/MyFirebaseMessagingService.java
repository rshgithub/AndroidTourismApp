package com.example.pablo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("onNewToken",token);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.e("onMessageReceived","Done");

        if (message.getNotification() != null) {
            Log.d("TAG", "Message Notification Body: " + message.getNotification().getTitle());
            Log.d("TAG", "Message Notification Body: " + message.getNotification().getBody());
        }

        if (message.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " +
                    message.getData());
            Map<String, String> params = message.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());
            if (object.has("type")) {
                try {
                    String type = object.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }}
