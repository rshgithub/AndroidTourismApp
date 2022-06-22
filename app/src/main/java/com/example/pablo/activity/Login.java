package com.example.pablo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pablo.R;
import com.example.pablo.fcm.MyFirebaseMessagingService;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivityLoginBinding;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.model.LoginRequest;
import com.example.pablo.model.login.ExampleLogin;
import com.google.gson.Gson;
import com.victor.loading.newton.NewtonCradleLoading;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    public static SharedPreferences SP;    // to read from SharedPreferences
    public static SharedPreferences.Editor EDIT; // to write in / edit SharedPreferences
    public static final String PREF_NAME = "token";
    public static final String TokenKey = "Token_K";
    public static final String USERKey = "USER_K";
    public static final String UserNameKey = "UserName_K";
    public static final String AddressKey = "AddressKey_K";
    public String FCM_TOKEN ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        EDIT = SP.edit();

        FCM_TOKEN = SP.getString(MyFirebaseMessagingService.fcmToken,null);
        Log.e("FCM_TOKEN",FCM_TOKEN+"");

        NewtonCradleLoading newtonCradleLoading;
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);


        if (binding.newtonCradleLoading.isStart()) {
            binding.newtonCradleLoading.start();
            binding.newtonCradleLoading.setLoadingColor(Color.parseColor("#FF0E4C75"));
        } else {
            binding.newtonCradleLoading.start();
            binding.newtonCradleLoading.setLoadingColor(Color.parseColor("#FF0E4C75"));

        }

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_str = binding.email.getText().toString();
                String password_str = binding.password.getText().toString();

                if (!email_str.isEmpty() && !password_str.isEmpty()) {
                    //api
                    FCM_TOKEN = SP.getString(MyFirebaseMessagingService.fcmToken,null);
                    LoginRequest user = new LoginRequest(email_str, password_str,FCM_TOKEN);
                    Service.ApiClient.getRetrofitInstance().login(user).enqueue(new Callback<ExampleLogin>() {
                        @Override
                        public void onResponse(Call<ExampleLogin> call, retrofit2.Response<ExampleLogin> response) {

                            Log.e("error", String.valueOf(response.code()));
                            if (response.isSuccessful()) {
                                binding.newtonCradleLoading.stop();
                                binding.newtonCradleLoading.setLoadingColor(Color.parseColor("#00FFFFFF"));
                                Toast.makeText(getApplicationContext(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();

                                //token
                                EDIT.putString(TokenKey, "Bearer " + response.body().getData().getToken());
                                EDIT.putLong(USERKey, response.body().getData().getUser().getId());
                                EDIT.putString(UserNameKey, String.valueOf(response.body().getData().getUser().getName()));
                                EDIT.putString(AddressKey, String.valueOf(response.body().getData().getUser().getAddress()));
                                EDIT.apply();

                                Intent intent = new Intent(getBaseContext(), BottomNavigationBarActivity.class);
                                startActivity(intent);
                                Toast.makeText(getBaseContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("Success", new Gson().toJson(response.body()));
                                finish();
                            } else {

                                String errorMessage = parseError(response);
                                Log.e("errorMessage", errorMessage + "");
                                Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                            }


                        }

                        @Override
                        public void onFailure(Call<ExampleLogin> call, Throwable t) {
                            binding.newtonCradleLoading.stop();
                            binding.newtonCradleLoading.setLoadingColor(Color.parseColor("#00FFFFFF"));
                            Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

                            t.printStackTrace();
                            Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            call.cancel();
                        }
                    });

                } else {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(findViewById(R.id.email));
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(findViewById(R.id.password));
                    Toast.makeText(getBaseContext(), "Please check your Email Field and Password Field", Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Signup.class);
                startActivity(i);
            }
        });
    }

    public static String parseError(Response<?> response) {
        String errorMsg = null;
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONObject jsonObject2 = jsonObject.getJSONObject("errors");
            JSONArray jsonArray = jsonObject2.getJSONArray("password");
            String s = jsonArray.getString(0);


            return s;
        } catch (Exception e) {
        }
        return errorMsg;
    }

}
