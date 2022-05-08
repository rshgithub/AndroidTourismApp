package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.MosqueAdapter;
import com.example.pablo.adapters.popularMosquesAdapter;
import com.example.pablo.databinding.ActivityMosqueDetailsBinding;
import com.example.pablo.model.mosquedetails.MosqueDetailsExample;
import com.example.pablo.model.mosques.MosqueExample;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class MosqueDetails extends AppCompatActivity {
     ActivityMosqueDetailsBinding binding;
    int mosqueId ;
    Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMosqueDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //----------------------------reserve-------------------------------------------------

        if (getIntent() != null) {
            mosqueId = getIntent().getIntExtra(MosqueAdapter.MOSQUE_ID, 0);
            mosqueId = getIntent().getIntExtra(popularMosquesAdapter.MOSQUE_ID, 0);
        }

        service= Service.ApiClient.getRetrofitInstance();
        getMosqueDetails();


    }
    private void getMosqueDetails() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getMosqueDetails(mosqueId,"Bearer " + token).enqueue(new Callback<MosqueExample>() {
            @Override
            public void onResponse(Call<MosqueExample> call, Response<MosqueExample> response) {
                Log.e("response code", response.code() + "");

                if (response.body() != null){

                    binding.ChurchesName.setText( response.body().getData().getName());
                    binding.locationPin.setText( response.body().getData().getAddress());
                    binding.availableTime.setText(response.body().getData().getAvailableTime()+"");
                    binding.details.setText( response.body().getData().getDetails());
                    binding.availableDay.setText( response.body().getData().getAvailableDay()+"");
                    binding.km.setText( response.body().getData().getAreaSpace()+"");
                    binding.visitorsCount.setText( response.body().getData().getVisitorsCount());
                    Glide.with(MosqueDetails.this).load(response.body().getData().getMosqueImage()).circleCrop().into(binding.mosqueImage);
                    Log.e("Success", new Gson().toJson(response.body()));
                    Log.e("response code", response.code() + "");

                    binding.call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //استقبال الرقم من api لازم نحطو جوا التصميم مش راضي ينضاف لنص
                            String phone = (String) response.body().getData().getPhoneNumber();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent);
                        }
                    });
                }else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }
            @Override
            public void onFailure(Call<MosqueExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }
}