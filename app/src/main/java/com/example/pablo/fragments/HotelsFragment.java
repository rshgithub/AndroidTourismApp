package com.example.pablo.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;


import com.example.pablo.activity.NoInternetConnection;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.AllHotelsAdapter;
import com.example.pablo.adapters.PopularHotelsAdapter;
import com.example.pablo.databinding.FragmentHotelsBinding;
import com.example.pablo.model.hotel.Hotels;
import com.example.pablo.model.hotel.HotelsData;
import com.example.pablo.model.hotels.Data;
import com.example.pablo.model.login.DataLogin;
import com.example.pablo.model.login.ExampleLogin;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class HotelsFragment extends Fragment {
    FragmentHotelsBinding binding;
    PopularHotelsAdapter popularHotelsAdapter;
    AllHotelsAdapter allHotelsAdapter;
    List<HotelsData> list;
    List<HotelsData> list1;
    Service service;
    ExampleLogin exampleLogin;
    DataLogin dataLogin;
    int page = 1, limit = 10;
    boolean isConnected = false;
    ConnectivityManager connectivityManager;
    boolean isLoading = false;
    boolean isLastPage = true;
    int lastVisibleItem;
    int totalItemCount;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HotelsFragment() {
    }

    public static HotelsFragment newInstance() {
        HotelsFragment fragment = new HotelsFragment();
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
        binding = FragmentHotelsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
           new Handler().postDelayed(()->{
               swipeRefreshLayout.setRefreshing(false);
               checkInternetConnection();
               startShimmer();
               adapter();
               getRetrofitInstance();
               getHotel();
               getPopularHotel();
           },1000);
        });

        checkInternetConnection();
        startShimmer();
        adapter();
        getRetrofitInstance();
        getHotel();
        getPopularHotel();
        return view;
    }


    private void getHotel() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.


        service.getHotels(token).enqueue(new Callback<Hotels>() {
            @Override
            public void onResponse(Call<Hotels> call, Response<Hotels> response) {

                if (response.isSuccessful()) {
                   list = response.body().getData();
                    binding.shimmerLayout1.stopShimmer();
                    binding.shimmerLayout1.setVisibility(View.GONE);
                    binding.recyclerview.startLayoutAnimation();

                    recyclerPagination(response.body());

                }else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");

                }
            }
            @Override
            public void onFailure(Call<Hotels> call, Throwable t) {
                t.printStackTrace();




                call.cancel();
            }
        });
    }


    private void recyclerPagination(Hotels hotels){
        if (page == 1) {
            allHotelsAdapter.setData(hotels.getData());
        } else
            allHotelsAdapter.addToList(hotels.getData());
        Log.e("page00",hotels.getData().get(0).getAddress()+"");
        if (hotels.getLastPage()==page){
            isLastPage=true;
            Log.e("lastPage",isLastPage+"");
        }
        else{
            isLastPage=false;

        }
    }

    private void getPopularHotel() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getPopularHotels(token).enqueue(new Callback<List<HotelsData>>() {
            @Override
            public void onResponse(Call<List<HotelsData>> call, Response<List<HotelsData>> response) {

                if (response.isSuccessful()) {
                    binding.recyclerview1.startLayoutAnimation();

                    list1 = response.body();
                    popularHotelsAdapter.setData(list1);
                    Log.e("Success", new Gson().toJson(response.body()));
                    stopShimmer();
                }else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");

                }

            }
            @Override
            public void onFailure(Call<List<HotelsData>> call, Throwable t) {
                t.printStackTrace();
                Log.e("error",t.getMessage());
            }
        });
    }

    @SuppressLint("NewApi")
    private void registerNetworkCallback(){


        try {

            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
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

    private void checkInternetConnection(){
        if (!isOnLine()){
            if (isConnected){
                Toast.makeText(getActivity(),"Connected",Toast.LENGTH_SHORT).show();
            }else{

                Intent i = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(i);
                getActivity().finish();

            }
        }
    }

    private void adapter(){

        exampleLogin = new ExampleLogin();
        dataLogin = new DataLogin();

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview1.setLayoutManager(linearLayoutManager2);
        allHotelsAdapter = new AllHotelsAdapter(getActivity());
        popularHotelsAdapter = new PopularHotelsAdapter(getActivity());
        binding.recyclerview.setAdapter(allHotelsAdapter);
        binding.recyclerview1.setAdapter(popularHotelsAdapter);



        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                Log.e("totalItemCount", totalItemCount + "");
                Log.e("lastVisibleItem", lastVisibleItem + "");
                if (lastVisibleItem == (totalItemCount - 1) && !isLoading && totalItemCount != 0 && !isLastPage) {
                    page++;
                    Log.e("listSize", "done");
                }
            }
        });


    }

    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();
    }


    private void search() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.search(token).enqueue(new Callback<HotelsData>() {
            @Override
            public void onResponse(Call<HotelsData> call, Response<HotelsData> response) {
                if (response.isSuccessful()){
                    Log.d("Success", new Gson().toJson(response.body()));
                }


            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<HotelsData> call, Throwable t) {
            }
        });

    }

}