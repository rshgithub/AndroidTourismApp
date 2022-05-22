package com.example.pablo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pablo.activity.Login;
import com.example.pablo.details_activities.MosqueDetails;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.R;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.PopularChurchesAdapter;
import com.example.pablo.adapters.ChurchesAdapter;
import com.example.pablo.model.churches.Data;
import com.example.pablo.databinding.FragmentChurchesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChurchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChurchesFragment extends Fragment {

    ChurchesAdapter churchesAdapter;
    FragmentChurchesBinding binding;
    RecyclerView recyclerView,recyclerView2;
    List<Data> list;
    Service service;
    public static final String Item_KEY = "churches_key";
    PopularChurchesAdapter popularChurchesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChurchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ChurchesFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        binding = FragmentChurchesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView2 = view.findViewById(R.id.recyclerview2);

        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);
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
        recyclerView.setAdapter(popularChurchesAdapter);
        recyclerView2.setAdapter(churchesAdapter);

        service = Service.ApiClient.getRetrofitInstance();

        getMosque();
        getpopularChurches();
        return view;
    }
    //2ed section popular hotels
    private void getMosque() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getChurches("Bearer " + token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

                if (response.body() != null) {
                    list = response.body();
                    churchesAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //3ed section popular hotels
    private void getpopularChurches() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getChurches("Bearer " + token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

                if (response.body() != null) {
                    list = response.body();
                    popularChurchesAdapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}