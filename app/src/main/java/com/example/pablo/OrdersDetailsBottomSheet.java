package com.example.pablo;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pablo.activity.Login;
import com.example.pablo.databinding.ActivityOrdersDetailsBinding;
import com.example.pablo.fragments.OrderFragment;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.order_details.OrderDetailsExample;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;

public class OrdersDetailsBottomSheet extends BottomSheetDialogFragment {

    ActivityOrdersDetailsBinding binding;
    Service service;
    Long Order_id ;
    OrdersDetailsBottomSheet.ListenerBottomSheet mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityOrdersDetailsBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

//        if (getActivity().getIntent() != null) {
//            Order_id = getActivity().getIntent().getLongExtra(OrderFragment.ORDER_ID,0);
//
//        }

        service= Service.ApiClient.getRetrofitInstance();
       // getOrderDetails();

        return v;
    }

    private void getOrderDetails() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getHotelOrdersDetails(Order_id, "Bearer " + token).enqueue(new Callback<OrderDetailsExample>() {
            @Override
            public void onResponse(Call<OrderDetailsExample> call, Response<OrderDetailsExample> response) {
                if (response.body() != null) {

                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    binding.checkin.setText(response.body().getData().getCheckIn() + "");
                    binding.checkout.setText(response.body().getData().getCheckOut() + "");
                    binding.roomCount.setText(response.body().getData().getRoomCount() + "");
                    binding.totalNights.setText(response.body().getData().getTotalNights() + "");
                    binding.totalPrice.setText(response.body().getData().getOrderTotalPrice() + "");
                    binding.roomOffer.setText(response.body().getData().getRoomHasOffer() + "");
                    binding.roomSering.setText(response.body().getData().getSavingsPerRoom() + "");
                    binding.pricePerNights.setText(response.body().getData().getRoomPricePerNight() + "");
//                    Glide.with(ChurchesDetails.this).load(response.body().getData().getChurchImage()).circleCrop()
//                            .into(binding.churchesImage);
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsExample> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }



    public interface ListenerBottomSheet {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OrdersDetailsBottomSheet.ListenerBottomSheet) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
    }
}



