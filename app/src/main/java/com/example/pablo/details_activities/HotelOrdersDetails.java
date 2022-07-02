package com.example.pablo.details_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.adapters.OrderDetailsAdapter;
import com.example.pablo.databinding.ActivityHotelOrdersDetailsBinding;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.order_details.HotelOrderItem;
import com.example.pablo.model.order_details.OrderDetailsExample;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.adapters.HotelsOrderAdapter.ORDERDETAILS;
import static com.example.pablo.fcm.MyFirebaseMessagingService.ORDER_ID;

public class HotelOrdersDetails extends AppCompatActivity {

    List<HotelOrderItem> list ;
    OrderDetailsAdapter adapter;
    ActivityHotelOrdersDetailsBinding binding;
    static Service service;
    Long orderId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelOrdersDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            orderId = getIntent().getLongExtra(ORDER_ID,0);
        }

        Log.e("order",orderId+"");

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
                        getOrderDetails();
                        dialog.dismiss();
                    }

                }
            });

        }
        getData();
        adapter();
        swipeRefresh();
        getRetrofitInstance();
        getOrderDetails();


    }

    private void getOrderDetails() {

        Login.SP = getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        service.getHotelOrdersDetails(orderId,token).enqueue(new Callback<OrderDetailsExample>() {
            @Override
            public void onResponse(Call<OrderDetailsExample> call, Response<OrderDetailsExample> response) {

                if (response.isSuccessful()) {

                    binding.totalPrice.setText(response.body().getData().getTotalPrice()+"$");
                    binding.date.setText(response.body().getData().getCreatedFrom()+"");
                    binding.count.setText(response.body().getData().getOrderItemsCount()+"");
                    binding.hotelName.setText(response.body().getData().getHotelName()+"");
                    binding.status.setText(response.body().getData().getStatus()+"");
                    if(response.body().getData().getStatus().equals("rejected")){
                        binding.status.setTextColor(getBaseContext().getResources().getColor(R.color.red));
                    }else if (response.body().getData().getStatus().equals("pending")){//Checkout !!
                        binding.status.setTextColor(getBaseContext().getResources().getColor(R.color.yellow));
                    }else if (response.body().getData().getStatus().equals("approved")){
                        binding.status.setTextColor(getBaseContext().getResources().getColor(R.color.green));
                    }else{
                        binding.status.setTextColor(getBaseContext().getResources().getColor(R.color.red));
                    }
                    Glide.with(getBaseContext()).load(response.body().getData().getHotel_image())
                            .error(R.drawable.mosqes).skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.imageView19);


                    list = response.body().getHotelOrderItems();
                    adapter.setData(list);
                    Log.e("response",response.body().getHotelOrderItems()+" if");
                }
                else {


                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<OrderDetailsExample> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage());

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
        list =new ArrayList<>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);

        adapter = new OrderDetailsAdapter(this);
        binding.recyclerview.setAdapter(adapter);



    }

    private void getData(){

    }

    private void getRetrofitInstance(){
        service= Service.ApiClient.getRetrofitInstance();
    }

    private void swipeRefresh(){
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(()->{
                swipeRefreshLayout.setRefreshing(false);
                adapter();
                getRetrofitInstance();
                getOrderDetails();

            },1000);
        });
    }
}
