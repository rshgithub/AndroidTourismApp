package com.example.pablo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.Cart;
import com.example.pablo.R;
import com.example.pablo.activity.ConfirmPayment;
import com.example.pablo.activity.Login;
import com.example.pablo.adapters.RoomAdapter;
import com.example.pablo.databinding.ActivityRoomBinding;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.rooms.Data;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

public class RoomsBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    List<Data> list ;
    RoomAdapter adapter;
    public static final String Item_KEY = "hotel_key";
    int ProductId ;
    ActivityRoomBinding binding;
    Service service;
    RecyclerView recyclerView;
    ImageView cart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_room, container, false);
        list =new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerview);
        cart = v.findViewById(R.id.cart);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Cart.class);
                startActivity(intent);
            }
        });

        if (getActivity().getIntent() != null) {
            ProductId = getActivity().getIntent().getIntExtra("room_id",0);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RoomAdapter(getActivity(), new MyInterface() {
            @Override
            public void onItemClick(int Id) {
                Intent intent = new Intent(getActivity(), ConfirmPayment.class);
                intent.putExtra(Item_KEY, Id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        service= Service.ApiClient.getRetrofitInstance();
        getRoomDetails();
        adapter.notifyDataSetChanged();

        return v;
    }

    private void getRoomDetails() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        //here put room model class or hotel model ???
        service.getRoom("Bearer " + token).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {

                if (response.body() != null) {
                    list = response.body();
                    adapter.setdata(list);

                }
            }
            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                Log.e("erorr", t.getMessage());

            }
        });
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}

