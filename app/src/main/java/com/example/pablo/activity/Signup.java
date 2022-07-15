package com.example.pablo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pablo.R;
import com.example.pablo.fcm.MyFirebaseMessagingService;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.model.LoginRequest;
import com.example.pablo.model.RegisterRequest;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivitySignupBinding;
import com.example.pablo.model.register.Example;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.pablo.activity.Login.UserNameKey;


public class Signup extends AppCompatActivity {

    public static final String PREF_NAME = "RegisterPrefrences";
    ActivitySignupBinding binding;
    public static final String TokenKey = "Token_K";
    public static final String USERKey = "USER_K";
    String Input_Name, Input_Email, Input_Address, Input_Password;
    public static final String FullNameKey = "FullName_K", EmailKey = "Email_K", PassKey = "Pass_K", AddressKey = "Address_K";
    public static SharedPreferences SP;    // to read from SharedPreferences
    public static SharedPreferences.Editor EDIT; // to write in / edit SharedPreferences
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        EDIT = SP.edit();


        // -------------------------- If UserLogin is Already signed up Toast method ------------------------------
        restoreResult();


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud = KProgressHUD.create(Signup.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                hud.setProgress(90);


                Input_Name = binding.fullName.getText().toString();
                Input_Email = binding.email.getText().toString();
                Input_Password = binding.password.getText().toString();
                Input_Address = binding.address.getText().toString();

                if (!Input_Name.isEmpty() && !Input_Email.isEmpty() && !Input_Password.isEmpty() && !Input_Address.isEmpty()) {
                    String fcm_token = SP.getString(MyFirebaseMessagingService.fcmToken,"");

                    //api
                    RegisterRequest user = new RegisterRequest();
                    user.setName(Input_Name);
                    user.setEmail(Input_Email);
                    user.setPassword(Input_Password);
                    user.setPasswordConfirmation(Input_Password);
                    user.setAddress(Input_Address);
                    user.setFcm_token(fcm_token);


                 //   RequestBody FCM_TOKEN = RequestBody.create(MediaType.parse("multipart/form-data"),fcm_token) ;

                    Service.ApiClient.getRetrofitInstance().register("application/json", user).enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                            Log.e("response code", response.code() + "");
                            if (response.isSuccessful()) {
                                hud.dismiss();
                                EDIT.putString(TokenKey, "Bearer " + response.body().getData().getToken());
                                EDIT.putLong(USERKey, response.body().getData().getUser().getId());
                                EDIT.putString(UserNameKey, String.valueOf(response.body().getData().getUser().getName()));
                                EDIT.putString(AddressKey, String.valueOf(response.body().getData().getUser().getAddress()));
                                EDIT.putString(EmailKey, String.valueOf(response.body().getData().getUser().getEmail()));
                                EDIT.apply();
                                Intent intent = new Intent(getBaseContext(), BottomNavigationBarActivity.class);
                                startActivity(intent);

                                Toasty.success(getBaseContext(), response.body().getMessage() , Toast.LENGTH_LONG).show();
                                Log.e("Success", new Gson().toJson(response.body()));
                                finish();
                            }else {
                                hud.dismiss();
                                String errorMessage = parseError(response);
                                Log.e("errorMessage", errorMessage + "");
                                Toasty.error(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();

                            }
                            EDIT.apply();


                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {
                            hud.dismiss();
                            t.printStackTrace();
                            Toasty.error(getBaseContext(), t.getMessage() , Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }
            }
        });

    }

    public static String parseError(Response<?> response) {
        String errorMsg = null;
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONObject jsonObject2 = jsonObject.getJSONObject("errors");
            JSONArray jsonArray = jsonObject2.getJSONArray("email");
            String s = jsonArray.getString(0);
            return s;

        } catch (Exception e) {
        }
        return errorMsg;
    }

    public void restoreResult() {
        String Full = SP.getString(FullNameKey, "");
        String Email = SP.getString(EmailKey, "");
        String address = SP.getString(AddressKey, "");
        String Pass = SP.getString(PassKey, "");


        if (Full == null && address == null && Email == null && Pass == null) {
            Toasty.info(getApplicationContext(), " you Didn't sign up yet ", Toast.LENGTH_SHORT).show();
        } else if (Full != null && Email != null && Pass != null && address != null) {
//            Toasty.info(getApplicationContext(), "you already signed up ", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;
        }
        return true;
    }
}