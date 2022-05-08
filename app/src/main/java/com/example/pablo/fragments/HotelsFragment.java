package com.example.pablo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.pablo.activity.Login;
import com.example.pablo.R;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.AllHotelsAdapter;
import com.example.pablo.adapters.PopularHotelsAdapter;
import com.example.pablo.databinding.FragmentHotelsBinding;
import com.example.pablo.model.hotels.Data;
import com.example.pablo.model.login.DataLogin;
import com.example.pablo.model.login.ExampleLogin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelsFragment extends Fragment {
FragmentHotelsBinding binding;
    RecyclerView recyclerView,recyclerView2;
    PopularHotelsAdapter popularHotelsAdapter;
    AllHotelsAdapter allHotelsAdapter;
    List<Data> list;
    List<Data> list1;
    Service service;
    ExampleLogin exampleLogin;
    DataLogin dataLogin;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HotelsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HotelsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        binding = FragmentHotelsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView2 = view.findViewById(R.id.recyclerview1);

        //get user token
        exampleLogin = new ExampleLogin();
        dataLogin = new DataLogin();

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        allHotelsAdapter = new AllHotelsAdapter(getActivity());
        popularHotelsAdapter = new PopularHotelsAdapter(getActivity());
        recyclerView.setAdapter(allHotelsAdapter);
        recyclerView2.setAdapter(popularHotelsAdapter);

        service = Service.ApiClient.getRetrofitInstance();

        getHotel();
        getpopularHotel();
        return view;
    }
    //2ed section popular hotels

    private void getHotel() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getHotels("Bearer "+ token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

//                Toast.makeText(getActivity(), "data", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    list = response.body();
                    allHotelsAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
               Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("err",t.getMessage());
            }
        });
    }

    //3ed section popular hotels
    private void getpopularHotel() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getPopularHotels("Bearer " +  token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

                if (response.body() != null) {
                    list1 = response.body();
                    popularHotelsAdapter.setdata(list1);
                    Log.e("Success", new Gson().toJson(response.body()));

                }

            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                t.printStackTrace();
                Log.e("error",t.getMessage());

               Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}