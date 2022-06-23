package com.example.pablo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.R;
import com.example.pablo.details_activities.HotelOrdersDetails;
import com.example.pablo.databinding.HotelOrderBinding;
import com.example.pablo.model.order_details.HotelOrderItem;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.OrdersExample;

import java.util.HashSet;
import java.util.List;



public class HotelsOrderAdapter  extends RecyclerView.Adapter<HotelsOrderAdapter.ViewHolder> {
    public List<Datum> list  ;
    Context context;
    public static String ORDERDETAILS = "order_id";
;

    public HotelsOrderAdapter(Context context ){
        this.context= context;
    }



    @Override
    public HotelsOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HotelOrderBinding binding = HotelOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HotelsOrderAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HotelsOrderAdapter.ViewHolder holder, int position) {

        holder.binding.date.setText(list.get(position).getTimeCount()+"");
        holder.binding.price.setText(list.get(position).getTotalPrice()+"");
        holder.binding.count.setText(list.get(position).getOrderItemsCount()+"");
        holder.binding.hotelName.setText(list.get(position).getHotelName());




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, HotelOrdersDetails.class);
                intent.putExtra("order_id", list.get(position).getId());
                context.startActivity(intent);


            }
        });



    }



    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder  {
        HotelOrderBinding binding;
        public ViewHolder(HotelOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
