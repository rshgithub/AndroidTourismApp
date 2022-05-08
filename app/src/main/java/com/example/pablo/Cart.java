package com.example.pablo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pablo.activity.Login;
import com.example.pablo.adapters.CartAdapter;
import com.example.pablo.databinding.ActivityCartBinding;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.cart.Test;
import com.example.pablo.model.edit.EditExample;
import com.example.pablo.reservations.Datum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class Cart extends AppCompatActivity {

    List<com.example.pablo.model.cart.Datum> list ;
    CartAdapter adapter;
    ActivityCartBinding binding;
    static Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list =new ArrayList<com.example.pablo.model.cart.Datum>();

        binding.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.SP = getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
                String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                clear("Bearer " + token);
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerView2.setLayoutManager(layoutManager);

        adapter = new CartAdapter(this, new MyInterface() {
            @Override
            public void onItemClick(int Id) {

            }
        });


        service= Service.ApiClient.getRetrofitInstance();
        getRoomsCart();

        binding.recyclerView2.setAdapter(adapter);

    }

    private void getRoomsCart() {

        Login.SP = getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        //here put room model class or hotel model ???
        service.getCart("Bearer " + token).enqueue(new Callback<Test>() {
            @Override
            public void onResponse(Call<Test> call, Response<Test> response) {
                Log.e("response code", response.code() + "");

                if (response.body() != null) {
                    list = response.body().getData();
                   adapter.setData(list);
                }
                else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(Cart.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Test> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage());

            }
        });
    }

    //delete
    public static void delete(int id, String token){
        service.deleteItem(id,token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()){

                    Log.e("code",response.code()+"");
                    Log.e("code",response.body().getId()+"");

                }else {
                    Log.e("code",response.code()+"");
                    Log.e("code",response.errorBody()+"");
                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                t.printStackTrace();
                Log.e("code",t.getMessage()+"");
            }
        });

    }


//    //edit
//    public static void edit(int id, String token){
//        service.editItem(id,token).enqueue(new Callback<EditExample>() {
//            @Override
//            public void onResponse(Call<EditExample> call, Response<EditExample> response) {
//
//                if (response.isSuccessful()){
//
//                    Log.e("code",response.code()+"");
//                    Log.e("code",response.body().getData().getId()+"");
//
//                }else {
//                    Log.e("code",response.code()+"");
//                    Log.e("code",response.errorBody()+"");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EditExample> call, Throwable t) {
//
//                t.printStackTrace();
//                Log.e("code",t.getMessage()+"");
//            }
//        });
//
//    }


    //clear
    public void clear( String token){

        service.clearCart( token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()){
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Cart.this, "clear all successfully", Toast.LENGTH_SHORT).show();
                    Log.e("code",response.code()+"");

                }else {
                    Log.e("code",response.code()+"");
                    Log.e("code",response.errorBody()+"");
                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                t.printStackTrace();
                Log.e("code",t.getMessage()+"");
            }
        });

    }


}