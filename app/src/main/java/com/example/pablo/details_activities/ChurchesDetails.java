package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.pablo.adapters.PopularChurchesAdapter;
import com.example.pablo.adapters.ChurchesAdapter;
import com.example.pablo.databinding.ActivityChurchesDetailsBinding;
import com.example.pablo.model.churchesdetails.ChurchesDetailsExample;
import com.example.pablo.model.mosquedetails.MosqueDetailsExample;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.TokenKey;

public class ChurchesDetails extends AppCompatActivity {
    ActivityChurchesDetailsBinding binding;
    int ChurchesId ;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChurchesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        swipeRefresh();

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
                        getChurchesDetails();
                        dialog.dismiss();
                    }

                }
            });

        }

        startShimmer();
        getData();
        getRetrofitInstance();
       getChurchesDetails();


    }


    private void getChurchesDetails() {

        Login.SP = this.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");//"No name defined" is the default value.

        service.getChurchesDetails(ChurchesId,token).enqueue(new Callback<ChurchesDetailsExample>() {
            @Override
            public void onResponse(Call<ChurchesDetailsExample> call, Response<ChurchesDetailsExample> response) {
                if (response.isSuccessful()){
                    stopShimmer();

                    binding.ChurchesName.setText( response.body().getData().getName());
                    binding.locationPin.setText( response.body().getData().getAddress());
                    binding.availableTime.setText(response.body().getData().getAvailableTime()+"");
                    binding.details.setText( response.body().getData().getDetails());
                    binding.availableDay.setText( response.body().getData().getAvailableDay()+"");
                    binding.km.setText( response.body().getData().getAreaSpace()+"");
                    binding.visitorsCount.setText( response.body().getData().getVisitorsCount() + " k visited");
                    Glide.with(ChurchesDetails.this).load(response.body().getData().getChurchImage())
                            .transition(withCrossFade())
                            .error(R.drawable.mosqe).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
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
                }else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<ChurchesDetailsExample> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();


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
    }

    private void stopShimmer(){
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void getData(){
        if (getIntent() != null) {
            ChurchesId = getIntent().getIntExtra(ChurchesAdapter.CHURCHES_ID,0);
            ChurchesId = getIntent().getIntExtra(PopularChurchesAdapter.CHURCHES_ID,0);

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
                startShimmer();
                getData();
                getRetrofitInstance();
                getChurchesDetails();
            },1000);
        });
    }
}