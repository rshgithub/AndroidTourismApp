package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.MosqueAdapter;
import com.example.pablo.adapters.popularMosquesAdapter;
import com.example.pablo.databinding.ActivityMosqueDetailsBinding;
import com.example.pablo.model.mosquedetails.MosqueDetailsExample;
import com.example.pablo.model.mosques.MosqueExample;
import com.google.gson.Gson;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.activity.Signup.PREF_NAME;
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

        swipeRefresh();

        startShimmer();
        if (!isOnLine()){
            Dialog dialog = new Dialog(getBaseContext(), R.style.NoInternet);
            dialog.setContentView(R.layout.no_internet);
            dialog.show();

            Button retry;
            retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isOnLine()){
                        getMosqueDetails();
                        dialog.dismiss();
                    }

                }
            });

        }
        getData();
        getRetrofitInstance();
        getMosqueDetails();


    }
    private void getMosqueDetails() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.


        service.getMosqueDetails(mosqueId,token).enqueue(new Callback<MosqueDetailsExample>() {
            @Override
            public void onResponse(Call<MosqueDetailsExample> call, Response<MosqueDetailsExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()){
                 // Toast.makeText(getApplicationContext(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();
                    stopShimmer();
                    binding.ChurchesName.setText( response.body().getData().getName());
                    binding.locationPin.setText( response.body().getData().getAddress());
                    binding.availableTime.setText(response.body().getData().getAvailableTime()+"");
                    binding.details.setText( response.body().getData().getDetails());
                    binding.availableDay.setText( response.body().getData().getAvailableDay()+"");
                    binding.km.setText( response.body().getData().getAreaSpace()+"");
                    binding.visitorsCount.setText( response.body().getData().getVisitorsCount());
                    Glide.with(MosqueDetails.this).load(response.body().getData().getMosqueImage())
                            .transition(withCrossFade())
                            .error(R.drawable.mosqe).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.mosqueImage);
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
                    Toast.makeText(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<MosqueDetailsExample> call, Throwable t) {
                t.printStackTrace();
              Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

            }

        });


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
        binding.shimmerLayout.startShimmer();
        binding.textView22.setVisibility(View.GONE);
        binding.call.setVisibility(View.GONE);
        binding.km.setVisibility(View.GONE);
        binding.imageView5.setVisibility(View.GONE);
        binding.imageButton2.setVisibility(View.GONE);
    }

    private void stopShimmer(){
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.textView22.setVisibility(View.VISIBLE);
        binding.call.setVisibility(View.VISIBLE);
        binding.km.setVisibility(View.VISIBLE);
        binding.imageView5.setVisibility(View.VISIBLE);
        binding.imageButton2.setVisibility(View.VISIBLE);
    }

    private void getData(){
        if (getIntent() != null) {
            mosqueId = getIntent().getIntExtra(MosqueAdapter.MOSQUE_ID, 0);
            mosqueId = getIntent().getIntExtra(popularMosquesAdapter.MOSQUE_ID, 0);
        }
    }

    private void getRetrofitInstance(){
        service= Service.ApiClient.getRetrofitInstance();
        }

    private void swipeRefresh(){
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(()->{
                swipeRefreshLayout.setRefreshing(false);
                getData();
                getRetrofitInstance();
                getMosqueDetails();
            },1000);
        });
    }
}