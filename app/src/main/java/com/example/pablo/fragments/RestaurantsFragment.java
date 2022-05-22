package com.example.pablo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.R;
import com.example.pablo.details_activities.RestaurantsDetails;
import com.example.pablo.model.RestaurantsExam;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.FreshRecipesAdapter;
import com.example.pablo.adapters.RestaurantsAdapter;
import com.example.pablo.databinding.FragmentRestaurantsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends Fragment {

    RestaurantsAdapter restaurantsAdapter;
    FreshRecipesAdapter freshRecipesAdapter;
    FragmentRestaurantsBinding binding;
    RecyclerView recyclerView,recyclerView2;
    List<RestaurantsExam> list;
    Service service;
    public static final String Item_KEY = "restaurant_key";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment RestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsFragment newInstance() {
        RestaurantsFragment fragment = new RestaurantsFragment();
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
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView2 = view.findViewById(R.id.recyclerview2);

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        restaurantsAdapter = new RestaurantsAdapter(getActivity(), new MyInterface() {
            @Override
            public void onItemClick(Long Id) {
                Intent intent = new Intent(getActivity(), RestaurantsDetails.class);
                intent.putExtra(Item_KEY, Id);
                startActivity(intent);
            }
        });
        freshRecipesAdapter = new FreshRecipesAdapter(getActivity(), new MyInterface() {
            @Override
            public void onItemClick(Long Id) {
//                Intent intent = new Intent(getActivity(), RestaurantsDetails.class);
//                intent.putExtra(Item_KEY, Id);
//                startActivity(intent);
            }
        });
        recyclerView.setAdapter(freshRecipesAdapter);
        recyclerView2.setAdapter(restaurantsAdapter);

        service = Service.ApiClient.getRetrofitInstance();

        getRestaurants();
        getfreshRecipes();
        return view;
    }
    //2ed section popular hotels
    private void getRestaurants() {
        service.getRestaurant().enqueue(new Callback<List<RestaurantsExam>>() {
            @Override
            public void onResponse(Call<List<RestaurantsExam>> call, Response<List<RestaurantsExam>> response) {

//                Toast.makeText(getActivity(), "data", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    list = response.body();
                    restaurantsAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<RestaurantsExam>> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //3ed section popular hotels
    private void getfreshRecipes() {
        service.getFreshRecipes().enqueue(new Callback<List<RestaurantsExam>>() {
            @Override
            public void onResponse(Call<List<RestaurantsExam>> call, Response<List<RestaurantsExam>> response) {

//                Toast.makeText(getActivity(), "data", Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    list = response.body();
                    freshRecipesAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<RestaurantsExam>> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}