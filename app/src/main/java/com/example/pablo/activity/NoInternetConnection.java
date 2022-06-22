package com.example.pablo.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pablo.R;
import com.example.pablo.fragments.BottomNavigationBarActivity;

public class NoInternetConnection extends AppCompatActivity {

    boolean isConnected;
    Button try_again;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);


        try_again = findViewById(R.id.try_again_btn);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected){

                    Intent i = new Intent(NoInternetConnection.this, BottomNavigationBarActivity.class);
                    startActivity(i);
                    finish();


                }else   {

                    Toast.makeText(NoInternetConnection.this,"Not Connected",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @SuppressLint("NewApi")
    private void registerNetworkCallback(){
        try {

            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){

                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });




        }catch (Exception e){

            isConnected = false;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkCallback();
    }}

