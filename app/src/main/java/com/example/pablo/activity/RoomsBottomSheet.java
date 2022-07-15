package com.example.pablo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pablo.R;
import com.example.pablo.adapters.RoomAdapter;
import com.example.pablo.details_activities.HotelsDetails;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.fragments.CartFragment;
import com.example.pablo.interfaces.RoomsInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.hotel.HotelRoom;
import com.example.pablo.model.hotel.Hotels;
import com.example.pablo.model.hotel.HotelsData;
import com.example.pablo.model.hotels.HotelsExample;
import com.example.pablo.model.rooms.Data;
import com.example.pablo.model.rooms.RoomsExample;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.TokenKey;

public class RoomsBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    List<HotelRoom> list ;
    RoomAdapter adapter;
    Service service;
    RecyclerView recyclerView;
    ImageView cart;
    TextView roomCount;
    boolean isConnected = false;
    Long HotelId ;

    public RoomsBottomSheet(Long i) {
        HotelId=i;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_room, container, false);


        list =new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerview);
        roomCount = v.findViewById(R.id.rooms_count);
        cart = v.findViewById(R.id.cart);

        checkInternetConnection();

        adapter();

        cartIntent();

        getRetrofitInstance();

        getRoomDetails();



        return v;
    }

    private void getRoomDetails() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");

        service.getHotelsDetails(HotelId,token).enqueue(new Callback<HotelsExample>() {
            @Override
            public void onResponse(Call<HotelsExample> call, Response<HotelsExample> response) {
                if (response.isSuccessful()){
                    list = response.body().getData().getHotelRooms();
                    adapter.setData(list);
                    if (response.body().getData().getAvailableRoomsCount()==null){
                        roomCount.setText("0");

                    }else{
                        roomCount.setText(response.body().getData().getAvailableRoomsCount()+"");
                        Log.e("count",response.body().getData().getRoomsCount()+"");
                        Log.e("count",response.body().getData().getAvailableRoomsCount()+"");

                    }


                }else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<HotelsExample> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(getActivity(), t.getMessage()+"", Toast.LENGTH_LONG).show();

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

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;
        }
        return true;
    }

    private void adapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



            adapter = new RoomAdapter(getActivity(), new RoomsInterface() {
                @Override
                public void onItemClick(Long Id) {
                    Intent intent = new Intent(getActivity(), BookingInfo.class);
                    intent.putExtra("id", Id);
                    Log.e("roomid",Id+"");
                    intent.putExtra("isEdit", false);
                    startActivity(intent);
                }

                @Override
                public void onItemRoomClick(Long RoomCount) {
//                    if (RoomCount == null) {
//                        rooms_count.setText("0");
//                    } else {
//                        rooms_count.setText(RoomCount + "");
//                    }
                }
            });

                recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void cartIntent(){
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),BottomNavigationBarActivity.class);
                intent.putExtra("cart","cart");
                startActivity(intent);
            }
        });
    }

    private void getRetrofitInstance(){
        service= Service.ApiClient.getRetrofitInstance();

    }

    private void checkInternetConnection(){
        if (!isOnLine()){
            if (isConnected){
                Toasty.success(getActivity(),"Connected",Toast.LENGTH_SHORT).show();
            }else{

                Intent i = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(i);
                getActivity().finish();

            }
        }
    }


}

