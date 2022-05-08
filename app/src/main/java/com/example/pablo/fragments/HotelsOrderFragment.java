package com.example.pablo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.adapters.HotelsOrderAdapter;
import com.example.pablo.databinding.FragmentHotelsOrderBinding;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.HotelOrderItem;
import com.example.pablo.model.orders.OrdersExample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelsOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelsOrderFragment extends Fragment {
    FragmentHotelsOrderBinding binding;
    Service service;
    HotelsOrderAdapter hotelsOrderAdapter;
    RecyclerView recyclerView;
    List<HotelOrderItem> list;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HotelsOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HotelsOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelsOrderFragment newInstance() {
        HotelsOrderFragment fragment = new HotelsOrderFragment();
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
        binding = FragmentHotelsOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = view.findViewById(R.id.recyclerview);

       list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        hotelsOrderAdapter = new HotelsOrderAdapter(getActivity());

        recyclerView.setAdapter(hotelsOrderAdapter);

        service = Service.ApiClient.getRetrofitInstance();

       getOrders();

        return view;
    }

    private void getOrders() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
        service.getHotelOrders("Bearer " + token).enqueue(new Callback<OrdersExample>() {
            @Override
            public void onResponse(Call<OrdersExample> call, Response<OrdersExample> response) {

                if (response.body() != null) {
                  //  list = response.body().getData();
                    hotelsOrderAdapter.setData(list);

                }
            }
            @Override
            public void onFailure(Call<OrdersExample> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }

}