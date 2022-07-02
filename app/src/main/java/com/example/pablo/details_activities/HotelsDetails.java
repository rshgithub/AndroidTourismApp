package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pablo.activity.NoInternetConnection;
import com.example.pablo.activity.RoomsBottomSheet;
import com.example.pablo.activity.Login;
import com.example.pablo.adapters.AllHotelsAdapter;
import com.example.pablo.adapters.AmenitiesAdapter;
import com.example.pablo.adapters.ChurchesAdapter;
import com.example.pablo.adapters.PopularChurchesAdapter;
import com.example.pablo.adapters.PopularHotelsAdapter;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.ActivityHotelsDetailsBinding;
import com.example.pablo.model.amenities.Amenities;
import com.example.pablo.model.hotel.HotelsData;
import com.example.pablo.model.hotels.HotelsExample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.adapters.AllHotelsAdapter.HOTELS_ID;

public class HotelsDetails extends AppCompatActivity implements RoomsBottomSheet.BottomSheetListener{

    ActivityHotelsDetailsBinding binding;
    Long HotelId ;
    Service service;
    boolean isConnected = false;
    AmenitiesAdapter amenitiesAdapter;
    List<Amenities>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            HotelId = getIntent().getLongExtra("hotel_id",0);
        }

        Log.e("hotel_id",getIntent().getLongExtra("hotel_id",0)+"");

        swipeRefresh();

        list = new ArrayList<>() ;



        checkInternetConnection();
        startShimmer();
        getRetrofitInstance();
        getHotelData();
        mapLocation();
        adapter();
        getAmenities();



   //   receiveData();
    }
    private void receiveData(){
        if (getIntent() != null) {
                HotelId =getIntent().getLongExtra("hotel_id",0);
             //   HotelId =getIntent().getLongExtra(HOTELS_ID,0);
        }
        Log.e("id",HotelId+"   ff");

    }


    private void getHotelData() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getHotelsDetails(HotelId,token).enqueue(new Callback<HotelsExample>() {
            @Override
            public void onResponse(Call<HotelsExample> call, Response<HotelsExample> response) {
                if (response.isSuccessful()){
                    stopShimmer();
                    binding.hotelName.setText( response.body().getData().getName());
                    binding.rate.setText( response.body().getData().getStar()+"");
                    binding.location.setText(response.body().getData().getAddress());
                    binding.details.setText( response.body().getData().getDetails());
                    Glide.with(HotelsDetails.this).load(response.body().getData().getHotelImage())
                            .into(binding.hotelImg);
                    Toast.makeText(HotelsDetails.this, response.body().getData().getName()+"", Toast.LENGTH_SHORT).show();
                    //rooms
                    binding.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            RoomsBottomSheet bottomSheet = new RoomsBottomSheet();
                            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

                        }
                    });


                }else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<HotelsExample> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

            }

        });


    }

    private void getAmenities() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        service.getAmenitiesData(token).enqueue(new Callback<List<Amenities>>() {
            @Override
            public void onResponse(Call<List<Amenities>> call, Response<List<Amenities>> response) {
                if (response.isSuccessful()){
                    stopShimmer();
                    list = response.body();
                    amenitiesAdapter.setData(list);

                }else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Log.e("code", response.code() + "");
                    Toast.makeText(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Amenities>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

            }

        });


    }

    @Override
    public void onButtonClicked(String text) {

    }

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;
        }
        return true;
    }

    private void startShimmer(){
        binding.button.setVisibility(View.GONE);
        binding.amenities.setVisibility(View.GONE);
        binding.map.setVisibility(View.GONE);
        binding.imgv.setVisibility(View.GONE);
        binding.shimmerLayout.setVisibility(View.VISIBLE);
    }

    private void stopShimmer(){
//        binding.shimmerLayout.stopShimmer();
        binding.relativeLayout.setVisibility(View.VISIBLE);
        binding.button.setVisibility(View.VISIBLE);
        binding.amenities.setVisibility(View.VISIBLE);
        binding.map.setVisibility(View.VISIBLE);
        binding.imgv.setVisibility(View.VISIBLE);
        binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void checkInternetConnection(){
        if (!isOnLine()){
            if (isConnected){
                Toast.makeText(getBaseContext(),"Connected",Toast.LENGTH_SHORT).show();
            }else{

                Intent i = new Intent(getBaseContext(), NoInternetConnection.class);
                startActivity(i);
                finish();

            }
        }
    }


    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();
    }

    private void mapLocation(){
        binding.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "http://maps.google.com/maps?saddr=" + 31.503355632448965 + "," + 34.46231765317062 + "&daddr=" + 31.503355632448965 + "," + 34.46231765317062;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    private void adapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        amenitiesAdapter = new AmenitiesAdapter(this);
        binding.recyclerview.setAdapter(amenitiesAdapter);

    }

    private void swipeRefresh(){
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(()->{
                swipeRefreshLayout.setRefreshing(false);
                checkInternetConnection();
                startShimmer();
                receiveData();
                getRetrofitInstance();
                getHotelData();
                mapLocation();
                adapter();
                getAmenities();
            },1000);
        });
    }

}