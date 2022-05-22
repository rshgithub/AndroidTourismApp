package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pablo.Cart;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.RoomAdapter;
import com.example.pablo.databinding.ActivityRoomBinding;
import com.example.pablo.model.rooms.Data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;

public class RoomActivity extends AppCompatActivity {

    List<Data> list ;
    RoomAdapter adapter;
    public static final String Item_KEY = "hotel_key";
    int ProductId ;
    ActivityRoomBinding binding;
    Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list =new ArrayList<>();

        if (getIntent() != null) {
            ProductId = getIntent().getIntExtra("room_id",0);
        }


        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Cart.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);

        adapter = new RoomAdapter(getApplicationContext(), new MyInterface() {
            @Override
            public void onItemClick(Long Id) {
                Intent intent = new Intent(getBaseContext(), BookingInfo.class);
                intent.putExtra(Item_KEY, Id);
                startActivity(intent);
            }
        });
        binding.recyclerview.setAdapter(adapter);

        service= Service.ApiClient.getRetrofitInstance();
        getRoomDetails();
        adapter.notifyDataSetChanged();

    }

    private void getRoomDetails() {

        Login.SP = getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        //here put room model class or hotel model ???
        service.getRoom("Bearer " + token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

                if (response.body() != null) {
                    list = response.body();
                    adapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage());

            }
        });
    }
}