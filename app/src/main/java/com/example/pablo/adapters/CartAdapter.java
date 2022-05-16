package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.Constraints;
import com.example.pablo.activity.BookingInfo;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.RoomCartBinding;
import com.example.pablo.model.cart.Datum;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.Cart.delete;
import static com.example.pablo.activity.Login.PREF_NAME;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private List<Datum> list  ;
    Context context;
    Service service;
    MyInterface myInterface;
    public static String NAME,CHECKIN,CHECKOUT,DAYCOUNT,PRICE;
    public static final String Item_KEY = "room_key";

    public CartAdapter(Context context, MyInterface myInterface){
        this.context= context;
        this.myInterface=myInterface;
    }


    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RoomCartBinding binding = RoomCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartAdapter.ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            holder.binding.roomTotalPrice.setText(list.get(position).getOrderTotalPrice()+"");
            holder.binding.hotelRoomName.setText(list.get(position).getRoomCount() + " Room Count");
            holder.binding.dateTime.setText(list.get(position).getCheckIn() +" - "+ list.get(position).getCheckOut());

            int count = list.get(position).getOrderTotalPrice();
            int c=0;
            for (int i=0;i<count;i++){
                c += count;

            }
        Log.e("count",c+"       " +list.get(position).getOrderTotalPrice()+"");
            holder.binding.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Login.SP = context.getSharedPreferences(PREF_NAME ,MODE_PRIVATE);
                    String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                    delete(list.get(position).getId(),"Bearer " + token);
                    list.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                }
            });

            holder.binding.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context,BookingInfo.class);
                    intent.putExtra("editId",list.get(position).getId());
                    intent.putExtra("roomId",list.get(position).getRoomId());
                    intent.putExtra("CHECKIN", list.get(position).getCheckIn());
                    intent.putExtra("CHECKOUT", list.get(position).getCheckOut());
                    intent.putExtra("DAYCOUNT", list.get(position).getTotalNights());
                    intent.putExtra("PRICE", list.get(position).getOrderTotalPrice());

                    Log.e("ID",list.get(position).getId()+"");

                    String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                    delete(list.get(position).getId(),"Bearer " + token);
                    list.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);

                    view.getContext().startActivity(intent);
                }
            });


    }




    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<com.example.pablo.model.cart.Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
            RoomCartBinding binding;
        public ViewHolder(RoomCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }



}
