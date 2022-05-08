package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pablo.activity.RoomsBottomSheet;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivityHotelsDetailsBinding;
import com.example.pablo.model.hotels.HotelsExample;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;

public class HotelsDetails extends AppCompatActivity implements RoomsBottomSheet.BottomSheetListener{

    ActivityHotelsDetailsBinding binding;
    int ProductId ;
    Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//----------------------------Receive-------------------------------------------------

        if (getIntent() != null) {
            ProductId = getIntent().getIntExtra("hotel_id",0);
        }

        service= Service.ApiClient.getRetrofitInstance();
        getProductData();



//--------------------------------------------------------------------------------
      binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("geo:31.503355632448965, 34.46231765317062");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }else {
                    Toast.makeText(getBaseContext(), "ur mobile not sport google map", Toast.LENGTH_SHORT).show();

                }
            }
        });


}
    private void getProductData() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getHotelsDetails(ProductId,"Bearer " + token).enqueue(new Callback<HotelsExample>() {
            @Override
            public void onResponse(Call<HotelsExample> call, Response<HotelsExample> response) {
                if (response.body() != null){
                    binding.hotelName.setText( response.body().getData().getName());
                    binding.rate.setText( response.body().getData().getStar()+"");
                    binding.location.setText(response.body().getData().getAddress());
                    binding.details.setText( response.body().getData().getDetails());
//                    Glide.with(HotelsDetails.this).load(response.body().getData().getHotelImage())
                          //  .circleCrop().into(binding.hotelImg);
                    //rooms
                    binding.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            RoomsBottomSheet bottomSheet = new RoomsBottomSheet();
                            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                         //   intent1.putExtra("room_id", response.body().getData().getId());
                        }
                    });


                }
            }
            @Override
            public void onFailure(Call<HotelsExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


}

    @Override
    public void onButtonClicked(String text) {

    }
}