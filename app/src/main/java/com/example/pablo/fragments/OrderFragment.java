package com.example.pablo.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pablo.OrdersDetailsBottomSheet;
import com.example.pablo.activity.Login;
import com.example.pablo.adapters.HotelsOrderAdapter;
import com.example.pablo.databinding.FragmentOrderBinding;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.OrdersExample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.USERKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment implements OrdersDetailsBottomSheet.ListenerBottomSheet {
    FragmentOrderBinding binding;
    Service service;
    HotelsOrderAdapter hotelsOrderAdapter;
    List<Datum> list;
    public final static String ORDER_ID = "Order_id" ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
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


        list = new ArrayList<>() ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        hotelsOrderAdapter = new HotelsOrderAdapter(getActivity());

        binding.recyclerview.setAdapter(hotelsOrderAdapter);

        service = Service.ApiClient.getRetrofitInstance();

        getOrders();

        return view;
    }

    private void getOrders() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        Toast.makeText(getActivity(), token+"", Toast.LENGTH_SHORT).show();

        service.getHotelOrders(token).enqueue(new Callback<OrdersExample>() {
            @Override
            public void onResponse(Call<OrdersExample> call, Response<OrdersExample> response) {

                if (response.isSuccessful()) {
                    Log.e("response",response.code()+"");

                    List<Datum> datum=response.body().getData();

                    //user id
                    SharedPreferences SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    Long userId=SP.getLong(USERKey,0);

                    for (int i = 0; i < datum.size() ; i++) {
                       if (datum.get(i).getUserId()==userId){
                            list = response.body().getData();
                            hotelsOrderAdapter.setData(list, new MyInterface() {
                                @Override
                                public void onItemClick(Long Id) {
                                  Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

//                                    OrdersDetailsBottomSheet bottomSheet = new OrdersDetailsBottomSheet();
//                                    bottomSheet.show(getActivity().getSupportFragmentManager(), "orderDetails");
                                }
                            });
                            Log.e("orders", i+"");

                       }
                    }


                }else {

                    Toast.makeText(getActivity(), "else", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<OrdersExample> call, Throwable t) {
                t.printStackTrace();
//                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onButtonClicked(String text) {

    }


}