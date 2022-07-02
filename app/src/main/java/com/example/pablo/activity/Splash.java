package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.pablo.databinding.ActivityMainBinding;
import com.example.pablo.details_activities.HotelOrdersDetails;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.onbording_1.IntroActivity;

import static com.example.pablo.activity.Login.SP;
import static com.example.pablo.activity.Signup.PREF_NAME;

public class Splash extends AppCompatActivity {

    public static SharedPreferences onboarding;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Handler handler = new Handler();

        Login.SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");
        String FCM_TOKEN = Login.SP.getString(Login.FCM_TOKEN, "");
        Log.e("tokenn", token);
        Log.e("FCM_TOKEN_splash", FCM_TOKEN);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //check if first time
                onboarding = getSharedPreferences("onboarding", MODE_PRIVATE);

                boolean isFirsttime = onboarding.getBoolean("firsttime", true);

                if (getIntent().getExtras() != null && getIntent().getStringExtra("order_id") != null) {

                    if (getIntent().getStringExtra("order_id") != null) {
//                        Toast.makeText(MainActivity.this, getIntent().getStringExtra("order_id") + "", Toast.LENGTH_SHORT).show();
//                        Intent notificationIntent = new Intent(MainActivity.this, HotelOrdersDetails.class);
//                        notificationIntent.putExtra("order_id", getIntent().getStringExtra("order_id"));
//                        startActivity(notificationIntent);
//                        finish();
                    }
                } else if (!isFirsttime && token != null) {
                    Intent i = new Intent(getBaseContext(), BottomNavigationBarActivity.class);
                    startActivity(i);
                    finish();
                } else if (isFirsttime) {
                    SharedPreferences.Editor EDIT = onboarding.edit();
                    EDIT.putBoolean("firsttime", false);
                    EDIT.commit();
                    Intent i = new Intent(getBaseContext(), IntroActivity.class);
                    startActivity(i);

                } else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }

}



