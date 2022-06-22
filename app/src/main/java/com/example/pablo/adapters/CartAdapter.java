package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.R;
import com.example.pablo.activity.BookingInfo;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.BookingInterface;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.RoomCartBinding;
import com.example.pablo.model.cart.CartExample;
import com.example.pablo.model.cart.HotelOrderItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<HotelOrderItem> list;
    Context context;
    BookingInterface myInterface;


    public CartAdapter(Context context, BookingInterface myInterface) {
        this.context = context;
        this.myInterface = myInterface;
    }


    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RoomCartBinding binding = RoomCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartAdapter.ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.roomTotalPrice.setText(list.get(position).getOrderTotalPrice() + "");
        holder.binding.hotelRoomName.setText(list.get(position).getRoomCount() + " Room Count");
        holder.binding.dateTime.setText(list.get(position).getCheckIn() + " - " + list.get(position).getCheckOut());


        Long count = list.get(position).getOrderTotalPrice();
        Long c = Long.valueOf(0);
        for (int i = 0; i < list.size(); i++) {
            c += count;
        }

        myInterface.totalPriceOnItemClick(c);
        myInterface.countOnItemClick(Long.valueOf(list.size()));

        if(getItemCount()==0)
        {
//            holder.binding.empty.setVisibility(View.GONE);
//            holder.binding.imageView26.setVisibility(View.GONE);
        }else {

//            holder.binding.empty.setVisibility(View.VISIBLE);
//            holder.binding.empty.setText("There's No Orders For You");
//            holder.binding.imageView26.setVisibility(View.VISIBLE);
////            holder.binding.container.setVisibility(View.GONE);
//            holder.binding.imageView26.setImageResource(R.drawable.undraw_empty_cart_co35);
        }
    }



    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public void setData(List<HotelOrderItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoomCartBinding binding;

        public ViewHolder(RoomCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }


}
