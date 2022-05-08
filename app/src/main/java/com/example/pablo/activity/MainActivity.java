package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.pablo.databinding.ActivityMainBinding;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.onbording.OnboardingActivity;

import static com.example.pablo.activity.Login.PREF_NAME;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences onboarding;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // -------------------------- If UserLogin is Already signed up Toast method ------------------------------

        Login.SP = getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        binding.getstsrted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if first time
                onboarding = getSharedPreferences("onboarding", MODE_PRIVATE);

                boolean isFirsttime = onboarding.getBoolean("firsttime", true);

                if (isFirsttime) {
                    SharedPreferences.Editor EDIT = onboarding.edit();
                    EDIT.putBoolean("firsttime", false);
                    EDIT.commit();
                    Intent i = new Intent(getBaseContext(), OnboardingActivity.class);
                    startActivity(i);

                }
                else if (!isFirsttime &&  !token.isEmpty()) {
                    Intent i = new Intent(getBaseContext(), BottomNavigationBarActivity.class);
                    startActivity(i);
                } else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }
            }
        });

    }


}