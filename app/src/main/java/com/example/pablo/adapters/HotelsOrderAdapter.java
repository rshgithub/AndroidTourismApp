package com.example.pablo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.OrdersDetailsBottomSheet;
import com.example.pablo.databinding.HotelOrderBinding;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.model.orders.Datum;

import java.util.List;



public class HotelsOrderAdapter  extends RecyclerView.Adapter<HotelsOrderAdapter.ViewHolder> implements OrdersDetailsBottomSheet.ListenerBottomSheet {
    public List<Datum> list  ;
    Context context;
    public static MyInterface listener ;
    public FragmentActivity context4;


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

//        holder.binding.hotelName.setText(list.get(position).getName());
        holder.binding.room.setText(list.get(position).getCreatedAt()+"");
        holder.binding.price.setText(list.get(position).getTotalPrice()+"");
        holder.binding.count.setText(list.get(position).getOrderItemsCount()+"");



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //  listener.onItemClick(list.get(position).getId());

                OrdersDetailsBottomSheet bottomSheet = new OrdersDetailsBottomSheet();
                bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "orderDetails");

            }
        });



    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<Datum> list, MyInterface listener) {
        this.list = list;
        this.listener=listener;

        notifyDataSetChanged();
    }

    @Override
    public void onButtonClicked(String text) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
       HotelOrderBinding binding;
        public ViewHolder(HotelOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
