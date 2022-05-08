package com.example.pablo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pablo.R;
import com.example.pablo.model.RestaurantsExam;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.RestaurantsOrderAdapter;
import com.example.pablo.databinding.FragmentRestaurantsOrderBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantsOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsOrderFragment extends Fragment {
    FragmentRestaurantsOrderBinding binding;
    Service service;
    RestaurantsOrderAdapter restaurantsOrderAdapter;
    RecyclerView recyclerView;
    List<RestaurantsExam> list;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantsOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment RestaurantsOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsOrderFragment newInstance() {
        RestaurantsOrderFragment fragment = new RestaurantsOrderFragment();
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
        binding = FragmentRestaurantsOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = view.findViewById(R.id.recyclerview);

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        restaurantsOrderAdapter = new RestaurantsOrderAdapter(getActivity());

        recyclerView.setAdapter(restaurantsOrderAdapter);

        service = Service.ApiClient.getRetrofitInstance();

        getOrders();

        return view;
    }
    //2ed section popular hotels
    private void getOrders() {
        service.getRestaurantOrders().enqueue(new Callback<List<RestaurantsExam>>() {
            @Override
            public void onResponse(Call<List<RestaurantsExam>> call, Response<List<RestaurantsExam>> response) {

//                Toast.makeText(getActivity(), "data", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    list = response.body();
                    restaurantsOrderAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<RestaurantsExam>> call, Throwable t) {
//                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }

}