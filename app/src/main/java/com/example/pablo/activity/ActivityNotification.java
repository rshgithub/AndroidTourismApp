package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.activity.BookingInfo;
import com.example.pablo.activity.Cart;
import com.example.pablo.activity.Login;
import com.example.pablo.activity.Payment;
import com.example.pablo.adapters.CartAdapter;
import com.example.pablo.adapters.NotificationAdapter;
import com.example.pablo.databinding.ActivityNotificationBinding;
import com.example.pablo.databinding.ActivityPaymentBinding;
import com.example.pablo.interfaces.BookingInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.cart.CartExample;
import com.example.pablo.model.cart.HotelOrderItem;
import com.example.pablo.model.notification.Notification;
import com.example.pablo.model.notification.NotificationData;
import com.example.pablo.model.reservations.Datum;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class ActivityNotification extends AppCompatActivity {
    static Service service;
    ActivityNotificationBinding binding;
    List<NotificationData> list;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        adapter();
        getRetrofitInstance();
        getNotification();


    }

    private void getNotification() {

        Login.SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
        service.getNotification(token).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();
                    list = response.body().getData();
                    noData();
                    adapter.setData(list);
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.e("error", t.getMessage());
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

    private void adapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);

        adapter = new NotificationAdapter(this);


        binding.recyclerview.setAdapter(adapter);


    }

    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();

    }

    private void noData(){
        if(list.size()==0)
        {
            binding.imageView26.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.VISIBLE);
            binding.empty.setText("No Notification Yet");
            binding.recyclerview.setVisibility(View.GONE);

        }else{
            binding.imageView26.setVisibility(View.GONE);
            binding.empty.setVisibility(View.GONE);
            binding.recyclerview.setVisibility(View.VISIBLE);
        }

    }



}