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
import androidx.appcompat.widget.SearchView;
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
import com.example.pablo.details_activities.MosqueDetails;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.PopularChurchesAdapter;
import com.example.pablo.adapters.ChurchesAdapter;
import com.example.pablo.model.churches.ChurchesExample;
import com.example.pablo.model.churches.Data;
import com.example.pablo.databinding.FragmentChurchesBinding;
import com.example.pablo.model.hotel.SearchHotel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

public class ChurchesFragment extends Fragment {

    ChurchesAdapter churchesAdapter;
    FragmentChurchesBinding binding;
    List<ChurchesExample> list;
    Service service;
    boolean isConnected = false;
    ConnectivityManager connectivityManager;
    public static final String Item_KEY = "churches_key";
    PopularChurchesAdapter popularChurchesAdapter;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChurchesFragment() {
    }

    public static ChurchesFragment newInstance() {
        ChurchesFragment fragment = new ChurchesFragment();
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
        binding = FragmentChurchesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        swipeRefresh();
        checkInternetConnection();
        list = new ArrayList<>() ;
        adapter();
        getRetrofitInstance();
        getChurches();
        getPopularChurches();
        startShimmer();

        binding.searchView3.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                checkInternetConnection();
                startShimmer();
                adapter();
                getRetrofitInstance();
                getChurches();
                getPopularChurches();
                return false;
            }
        });

        binding.searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });


        return view;
    }

    private void getChurches() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getChurches(token).enqueue(new Callback<List<ChurchesExample>>() {
            @Override
            public void onResponse(Call<List<ChurchesExample>> call, Response<List<ChurchesExample>> response) {

                if (response.isSuccessful()) {
//                    Toast.makeText(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();
                    binding.recyclerview.startLayoutAnimation();

                    stopShimmer();
                    list = response.body();
                    popularChurchesAdapter.setData(list);

                }
            }
            @Override
            public void onFailure(Call<List<ChurchesExample>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void getPopularChurches() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getTopChurches(token).enqueue(new Callback<List<ChurchesExample>>() {
            @Override
            public void onResponse(Call<List<ChurchesExample>> call, Response<List<ChurchesExample>> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();
                    stopShimmer();
                    binding.recyclerview2.startLayoutAnimation();

                    list = response.body();
                    churchesAdapter.setData(list);

                }
            }
            @Override
            public void onFailure(Call<List<ChurchesExample>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage()+"", Toast.LENGTH_LONG).show();

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview2.setLayoutManager(linearLayoutManager2);
        churchesAdapter = new ChurchesAdapter(getActivity(), new MyInterface() {
            @Override
            public void onItemClick(Long Id) {
                Intent intent = new Intent(getActivity(), MosqueDetails.class);
                intent.putExtra(Item_KEY, Id);
                startActivity(intent);
            }
        });
        popularChurchesAdapter = new PopularChurchesAdapter(getActivity(), new MyInterface() {
            @Override
            public void onItemClick(Long Id) {
                Intent intent = new Intent(getActivity(), MosqueDetails.class);
                intent.putExtra(Item_KEY, Id);
                startActivity(intent);
            }
        });
        binding.recyclerview.setAdapter(popularChurchesAdapter);
        binding.recyclerview2.setAdapter(churchesAdapter);

    }

    private void startShimmer(){
        binding.shimmerLayout.startShimmer();
        binding.shimmerLayout1.startShimmer();
    }

    private void stopShimmer(){
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout1.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.shimmerLayout1.setVisibility(View.GONE);
    }

    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();
    }

    private void swipeRefresh(){
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(()->{
                swipeRefreshLayout.setRefreshing(false);
                checkInternetConnection();
                list = new ArrayList<>() ;
                adapter();
                getRetrofitInstance();
                getChurches();
                getPopularChurches();
                startShimmer();
            },1000);
        });
    }

    private void search(String c) {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        String search = binding.searchView3.getQuery().toString();

        service.ChurchesSearch(token,search).enqueue(new Callback<ChurchesExample>() {
            @Override
            public void onResponse(Call<ChurchesExample> call, Response<ChurchesExample> response) {
                if (response.isSuccessful()){

                 //   Log.e("search",response.body().getData().size()+"");
                   // churchesAdapter.setData(response.body().getData());
                    //  Log.e("search",response.body().+"");
                }


            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<ChurchesExample> call, Throwable t) {
            }
        });

    }

}