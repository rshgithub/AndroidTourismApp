package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Signup.TokenKey;

public class Splash extends AppCompatActivity {

    public static SharedPreferences SP;    // to read from SharedPreferences
    public static SharedPreferences.Editor EDIT;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        EDIT = SP.edit();


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = SP.getString(TokenKey, "");

                //check if first time
                boolean isFirstTime = SP.getBoolean("firsttime", true);

                if (!isFirstTime && !token.equals("")) {
                    Intent i = new Intent(getBaseContext(), BottomNavigationBarActivity.class);
                    startActivity(i);
                    finish();
                } else if (isFirstTime) {
                    SharedPreferences.Editor EDIT = SP.edit();
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



