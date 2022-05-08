package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.PopularChurchesAdapter;
import com.example.pablo.adapters.ChurchesAdapter;
import com.example.pablo.model.churches.ChurchesExample;
import com.example.pablo.model.churchesdetails.ChurchsDetailsExample;
import com.example.pablo.databinding.ActivityChurchesDetailsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;

public class ChurchesDetails extends AppCompatActivity {
    ActivityChurchesDetailsBinding binding;
    int ChurchesId ;
    Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChurchesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //----------------------------reserve-------------------------------------------------

        if (getIntent() != null) {
            ChurchesId = getIntent().getIntExtra(ChurchesAdapter.CHURCHES_ID,0);
            ChurchesId = getIntent().getIntExtra(PopularChurchesAdapter.CHURCHES_ID,0);

        }

        service= Service.ApiClient.getRetrofitInstance();
       getChurchesDetails();


    }


    private void getChurchesDetails() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getChurchesDetails(ChurchesId,"Bearer " +token).enqueue(new Callback<ChurchesExample>() {
            @Override
            public void onResponse(Call<ChurchesExample> call, Response<ChurchesExample> response) {
                if (response.body() != null){
                    binding.ChurchesName.setText( response.body().getData().getName());
                    binding.locationPin.setText( response.body().getData().getAddress());
                    binding.availableTime.setText(response.body().getData().getAvailableTime()+"");
                    binding.details.setText( response.body().getData().getDetails());
                    binding.availableDay.setText( response.body().getData().getAvailableDay()+"");
                    binding.km.setText( response.body().getData().getAreaSpace()+"");
                    binding.visitorsCount.setText( response.body().getData().getVisitorsCount());
                    Glide.with(ChurchesDetails.this).load(response.body().getData().getChurchImage()).circleCrop()
                            .into(binding.churchesImage);
                    binding.call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //استقبال الرقم من api لازم نحطو جوا التصميم مش راضي ينضاف لنص
                            String phone =  response.body().getData().getPhoneNumber()+"";
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ChurchesExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }
}