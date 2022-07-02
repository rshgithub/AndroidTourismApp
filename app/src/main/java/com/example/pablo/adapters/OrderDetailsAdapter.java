package com.example.pablo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.databinding.ActivityHotelOrdersDetailsBinding;
import com.example.pablo.databinding.HotelOrderBinding;
import com.example.pablo.databinding.OrderDetailsBinding;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.model.order_details.HotelOrderItem;
import com.example.pablo.model.orders.Datum;

import java.util.HashSet;
import java.util.List;

public class OrderDetailsAdapter  extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    public List<HotelOrderItem> list  ;
    Context context;
    private View.OnClickListener defaultRequestBtnClickListener;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public OrderDetailsAdapter(Context context ){
        this.context= context;
    }



    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        OrderDetailsBinding binding = OrderDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderDetailsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OrderDetailsAdapter.ViewHolder holder, int position) {

        holder.binding.reservationDate.setText(list.get(position).getCheckIn()+"  -  "+list.get(position).getCheckOut()+"");
        holder.binding.roomCount.setText(list.get(position).getRoomCount()+"");
        holder.binding.roomOffer.setText(list.get(position).getRoomHasOffer()+"");
        holder.binding.orderTotalPrice.setText(list.get(position).getOrderTotalPrice()+"");
        holder.binding.totalNights.setText(list.get(position).getTotalNights()+"");
        holder.binding.pricePerNights.setText(list.get(position).getRoomPricePerNight()+"");
        holder.binding.roomServing.setText(list.get(position).getSavingsPerRoom()+"");
        holder.binding.roomName.setText(list.get(position).getRoomName()+"");
        holder.binding.hotelName.setText(list.get(position).getHotelName()+"");
        holder.binding.createdFrom.setText(list.get(position).getCreatedFrom()+"");
        holder.binding.day.setText(list.get(position).getCreatedAt()+"");
        Glide.with(context).load(list.get(position).getRoomImage())
                .error(R.drawable.mosqes).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.roomImage);

        holder.binding.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.textView40.setVisibility(View.VISIBLE);
                holder.binding.textView33.setVisibility(View.VISIBLE);
                holder.binding.checkin.setVisibility(View.VISIBLE);
                holder.binding.roomOffer.setVisibility(View.VISIBLE);
                holder.binding.reservationDate.setVisibility(View.VISIBLE);
                holder.binding.reservationDate.setVisibility(View.VISIBLE);
                holder.binding.roomServing.setVisibility(View.VISIBLE);
                holder.binding.less.setVisibility(View.VISIBLE);
                holder.binding.details.setVisibility(View.GONE);

            }
        });

        holder.binding.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.textView40.setVisibility(View.GONE);
                holder.binding.textView33.setVisibility(View.GONE);
                holder.binding.checkin.setVisibility(View.GONE);
                holder.binding.roomOffer.setVisibility(View.GONE);
                holder.binding.reservationDate.setVisibility(View.GONE);
                holder.binding.reservationDate.setVisibility(View.GONE);
                holder.binding.roomServing.setVisibility(View.GONE);
                holder.binding.details.setVisibility(View.VISIBLE);
                holder.binding.less.setVisibility(View.GONE);

            }
        });







    }




    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<HotelOrderItem> list) {
        this.list = list;

        notifyDataSetChanged();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder  {
        OrderDetailsBinding binding;
        public ViewHolder(OrderDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}

