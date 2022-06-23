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

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.adapters.HotelsOrderAdapter.ORDERDETAILS;

public class HotelOrdersDetails extends AppCompatActivity {

    List<HotelOrderItem> list ;
    OrderDetailsAdapter adapter;
    ActivityHotelOrdersDetailsBinding binding;
    static Service service;
    Long Order_Id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelOrdersDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



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

        service.getHotelOrdersDetails(Order_Id,token).enqueue(new Callback<OrderDetailsExample>() {
            @Override
            public void onResponse(Call<OrderDetailsExample> call, Response<OrderDetailsExample> response) {

                if (response.isSuccessful()) {
                 Toast.makeText(getApplicationContext(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();


                   binding.totalPrice.setText(response.body().getData().getTotalPrice()+"$");
                   ////////////
                    binding.date.setText(response.body().getData().getTimeCount()+"");
                    binding.count.setText(response.body().getData().getOrderItemsCount()+"");
                    binding.hotelName.setText(response.body().getData().getHotelName()+"");

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
        if (getIntent() != null) {
            Order_Id = getIntent().getLongExtra("order_id",0);
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
                adapter();
                getRetrofitInstance();
                getOrderDetails();

            },1000);
        });
    }
}
