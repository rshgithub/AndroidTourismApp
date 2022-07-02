package com.example.pablo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.example.pablo.R;
import com.example.pablo.adapters.CartAdapter;
import com.example.pablo.databinding.ActivityCartBinding;
import com.example.pablo.interfaces.BookingInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.buyorder.BuyOrderExample;
import com.example.pablo.model.cart.CartExample;
import com.example.pablo.model.cart.HotelOrderItem;
import com.example.pablo.model.reservations.Datum;
import com.google.android.material.snackbar.Snackbar;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class Cart extends AppCompatActivity {

    List<HotelOrderItem> list;
    CartAdapter adapter;
    ActivityCartBinding binding;
    static Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.setCancelable(true);


        if (!isOnLine()){
            Dialog dialog = new Dialog(getBaseContext(),R.style.NoInternet);
            dialog.setContentView(R.layout.no_internet);
            dialog.show();

            Button retry;
            retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isOnLine()){
                        getRoomsCart();
                        dialog.dismiss();
                    }

                }
            });

        }
        startShimmer();
        list = new ArrayList<>();
        deleteAll();
        adapter();
        getRetrofitInstance();
        getRoomsCart();
        swipeToEditAndDelete();





    }

    private void getRoomsCart() {

        Login.SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
        service.getCart(token).enqueue(new Callback<CartExample>() {
            @Override
            public void onResponse(Call<CartExample> call, Response<CartExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                    stopShimmer();
                    Toast.makeText(getApplicationContext(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();
                    list = response.body().getHotelOrderItems();
                    noData();
                    adapter.setData(list);
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CartExample> call, Throwable t) {
                Log.e("error", t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();


            }
        });
    }

    //delete
    public void delete(Long id, String token) {
        service.deleteItem(id, token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.message()+"", Toast.LENGTH_LONG).show();

                    Log.e("code", response.code() + "");
                    Log.e("code", response.body().getId() + "");

                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

                t.printStackTrace();
                Log.e("code", t.getMessage() + "");
            }
        });

    }

    //clear
    public void clear(String token) {

        service.clearCart(token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), response.message()+"", Toast.LENGTH_LONG).show();

                    adapter.notifyDataSetChanged();
                    Toast.makeText(Cart.this, "clear all successfully", Toast.LENGTH_SHORT).show();
                    Log.e("code", response.code() + "");

                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

                t.printStackTrace();
                Log.e("code", t.getMessage() + "");
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

    private void deleteAll(){
        binding.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                clear(token);
                if (list != null)
                    list.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void adapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerView2.setLayoutManager(layoutManager);

        adapter = new CartAdapter(this, new BookingInterface() {

            @Override
            public void totalPriceOnItemClick(Long price) {
                binding.pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getBaseContext(),Payment.class);
                        intent.putExtra("price",price);
                        startActivity(intent);
                    }
                });
                binding.totalPrice.setText(price + "$");


            }

            @Override
            public void countOnItemClick(Long count) {
                binding.count.setText(count + "");
            }

        });

        binding.recyclerView2.setAdapter(adapter);


    }

    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();

    }

    private void swipeToEditAndDelete(){
        // Create and add a callback
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        Login.SP = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                        delete(list.get(position).getId(), token);
                        adapter.notifyDataSetChanged();
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                        break;

                    case ItemTouchHelper.RIGHT:
                        Log.e("orderId", list.get(position).getOrderId() + "");
                        Log.e("roomId", list.get(position).getId() + "");

                        Intent intent = new Intent(getApplicationContext(), BookingInfo.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("orderId", list.get(position).getOrderId());
                        intent.putExtra("isEdit", true);
                        adapter.notifyDataSetChanged();
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(Cart.this, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(Cart.this, R.color.blue))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_mode_edit_24)
                        .addSwipeRightLabel("Edit")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView2);

    }

    private void startShimmer(){
        binding.shimmerLayout.startShimmer();

    }

    private void stopShimmer(){
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void noData(){
        if(list.size()==0)
        {
            binding.empty.setVisibility(View.VISIBLE);
            binding.empty.setText("No Reserved Rooms Yet");
            binding.imageView26.setVisibility(View.VISIBLE);
            binding.imageView26.setImageResource(R.drawable.undraw_empty_cart_co35);
            binding.recyclerView2.setVisibility(View.GONE);

        }else{
            binding.empty.setVisibility(View.GONE);
            binding.imageView26.setVisibility(View.GONE);
            binding.recyclerView2.setVisibility(View.VISIBLE);
        }
    }


}