package com.example.pablo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pablo.model.RegisterRequest;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivitySignupBinding;
import com.example.pablo.model.register.Example;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Signup extends AppCompatActivity {

    public static final String PREF_NAME = "RegisterPrefrences";
    ActivitySignupBinding binding;
    String Input_Name, Input_Email, Input_Address, Input_Password;
    public static final String FullNameKey = "FullName_K", EmailKey = "Email_K", PassKey = "Pass_K", AddressKey = "Address_K";
    public static SharedPreferences SP;    // to read from SharedPreferences
    public static SharedPreferences.Editor EDIT; // to write in / edit SharedPreferences
    Service service;

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
                Input_Name = binding.fullName.getText().toString();
                Input_Email = binding.email.getText().toString();
                Input_Password = binding.password.getText().toString();
                Input_Address = binding.address.getText().toString();

                EDIT.putString(FullNameKey, Input_Name);
                EDIT.putString(EmailKey, Input_Email);
                EDIT.putString(PassKey, Input_Password);
                EDIT.putString(AddressKey, Input_Address);
                EDIT.apply();

                if (!Input_Name.isEmpty() && !Input_Email.isEmpty() && !Input_Password.isEmpty() && !Input_Address.isEmpty()) {

                    //api
                    RegisterRequest user = new RegisterRequest();
                    user.setName(Input_Name);
                    user.setEmail(Input_Email);
                    user.setPassword(Input_Password);
                    user.setPasswordConfirmation(Input_Password);
                    user.setAddress(Input_Address);
                    Service.ApiClient.getRetrofitInstance().register("application/json", user).enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                            Log.e("response code", response.code() + "");
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(getBaseContext(), Login.class);
                                startActivity(intent);
                                Toast.makeText(getBaseContext(), "Welcome" , Toast.LENGTH_LONG).show();
                                Log.e("Success", new Gson().toJson(response.body()));
                            } else {
                                String errorMessage = parseError(response);
//                                UtilsMethods.createFailSnackbar(binding.parent, errorMessage).show();
                                Log.e("errorMessage", errorMessage + "");
                            }


                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {
                            t.printStackTrace();

                        }
                    });

                } else {
                    Toast.makeText(getBaseContext(), "Empty", Toast.LENGTH_SHORT).show();

                }
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

//            errorMsg = jObjError.getString("message");
//            Util.Logd(jObjError.getString("errorMessage"));
            return s;
        } catch (Exception e) {
//            Util.Logd(e.getMessage());
        }
        return errorMsg;
    }


//    public static String parseError(Response<?> response) {
//        try {
//            Gson gson = new Gson();
//            Errors one = gson.fromJson(new Gson().toJson(response.body()), Errors.class);
//            return one.getPassword().get(0);
//        } catch (Exception e) {
////            Util.Logd(e.getMessage());
//            Log.e("Exception", response.toString());
//        }
//        return "";
//    }

    public void restoreResult() {
        String Full = SP.getString(FullNameKey, "");
        String Email = SP.getString(EmailKey, "");
        String address = SP.getString(AddressKey, "");
        String Pass = SP.getString(PassKey, "");


        if (Full == null && address == null && Email == null && Pass == null) {
            Toast.makeText(getApplicationContext(), " you Didn't sign up yet ", Toast.LENGTH_SHORT).show();
        } else if (Full != null && Email != null && Pass != null && address != null) {
            Toast.makeText(getApplicationContext(), "you already signed up ", Toast.LENGTH_SHORT).show();
        }
    }


}