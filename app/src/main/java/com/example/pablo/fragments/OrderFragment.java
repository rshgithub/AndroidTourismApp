package com.example.pablo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.pablo.activity.NoInternetConnection;
import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.adapters.HotelsOrderAdapter;
import com.example.pablo.databinding.FragmentOrderBinding;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.order_details.HotelOrderItem;
import com.example.pablo.model.order_details.OrderDetailsExample;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.OrdersExample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.USERKey;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.PREF_NAME;

public class OrderFragment extends Fragment {
    FragmentOrderBinding binding;
    Service service;
    HotelsOrderAdapter hotelsOrderAdapter;
    List<Datum> list;
    boolean isConnected = false;
    ConnectivityManager connectivityManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        swipeRefresh();
        startShimmer();
        checkInternetConnection();
        adapter();
        getRetrofitInstance();
        getOrders();




        return view;
    }

    private void getOrders() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getHotelOrders(token).enqueue(new Callback<OrdersExample>() {
            @Override
            public void onResponse(Call<OrdersExample> call, Response<OrdersExample> response) {

                if (response.isSuccessful()) {

                    stopShimmer();
                    list = response.body().getData();
                    noData();
                    hotelsOrderAdapter.setData(list);

                } else {
                    stopShimmer();
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<OrdersExample> call, Throwable t) {
                stopShimmer();

                t.printStackTrace();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback() {


        try {

            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });


        } catch (Exception e) {

            isConnected = false;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    public boolean isOnLine() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    private void checkInternetConnection() {
        if (!isOnLine()) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(i);
                getActivity().finish();

            }
        }
    }

    private void adapter() {
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        hotelsOrderAdapter = new HotelsOrderAdapter(getActivity());

        binding.recyclerview.setAdapter(hotelsOrderAdapter);

    }

    private void startShimmer() {
        binding.shimmerLayout.startShimmer();
    }

    private void stopShimmer() {
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void noData() {
        if (list.size() == 0) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.empty.setText("No Reserved Rooms Yet");
            binding.imageView26.setVisibility(View.VISIBLE);
            binding.imageView26.setImageResource(R.drawable.undraw_empty_cart_co35);
            binding.recyclerview.setVisibility(View.GONE);

        } else {
            binding.empty.setVisibility(View.GONE);
            binding.imageView26.setVisibility(View.GONE);
            binding.recyclerview.setVisibility(View.VISIBLE);
        }
    }

    private void getRetrofitInstance() {
        service = Service.ApiClient.getRetrofitInstance();
    }

    private void swipeRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                startShimmer();
                checkInternetConnection();
                adapter();
                getRetrofitInstance();
                getOrders();
            }, 1000);
        });
    }

}