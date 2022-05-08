package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivityRestaurantsDetailsBinding;
import com.example.pablo.model.RestaurantsExam;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsDetails extends AppCompatActivity {
    ActivityRestaurantsDetailsBinding binding;
    int RestaurantsId ;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (getIntent() != null) {
            RestaurantsId = getIntent().getIntExtra("restaurant_key",0);
        }

        service= Service.ApiClient.getRetrofitInstance();
        getRestaurantsDetails();

    }
    private void getRestaurantsDetails() {
        service.getRestaurantDetails(RestaurantsId).enqueue(new Callback<RestaurantsExam>() {
            @Override
            public void onResponse(Call<RestaurantsExam> call, Response<RestaurantsExam> response) {
                if (response.body() != null){
                    binding.restaurantName.setText( response.body().getResName());
                    binding.locationPin.setText( response.body().getLocation());
                    binding.resName.setText(response.body().getResName());
                    binding.details.setText( response.body().getDetails());
                    Glide.with(RestaurantsDetails.this).load(response.body().getImage()).circleCrop().into(binding.restaurantImageView);

                }
            }
            @Override
            public void onFailure(Call<RestaurantsExam> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }
}