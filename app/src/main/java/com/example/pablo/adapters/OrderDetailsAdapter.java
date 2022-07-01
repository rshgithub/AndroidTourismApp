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

        holder.binding.checkin.setText(list.get(position).getCheckIn()+"  -  "+list.get(position).getCheckOut()+"");
        holder.binding.roomCount.setText(list.get(position).getRoomCount()+"");
        holder.binding.roomOffer.setText(list.get(position).getRoomHasOffer()+"");
        holder.binding.totalNights.setText(list.get(position).getTotalNights()+"");
        holder.binding.pricePerNights.setText(list.get(position).getRoomPricePerNight()+"");
        holder.binding.roomServing.setText(list.get(position).getSavingsPerRoom()+"");
        holder.binding.roomName.setText(list.get(position).getRoomName()+"");


//        if (list.get(position).getStatus().equals("rejected")){
//            holder.binding.imageView2.setBackgroundColor(R.drawable.cancel);
//        }else if (list.get(position).getStatus().equals("accepted")){
//            holder.binding.imageView2.setBackgroundColor(R.drawable.cancel);
//        }else if (list.get(position).getStatus().equals("payed")){
//            holder.binding.imageView2.setBackgroundColor(R.drawable.cancel);
//        }else if (list.get(position).getStatus().equals("carted")){
//            holder.binding.imageView2.setBackgroundColor(R.drawable.cancel);
//        }




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

