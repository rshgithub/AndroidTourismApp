package com.example.pablo.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pablo.Constraints;
import com.example.pablo.activity.BookingInfo;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.RoomItemBinding;
import com.example.pablo.model.rooms.Data;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private List<Data> list  ;
    Context context;
    private static MyInterface listener ;
    int ProductId ;
    Service service;
//    public final static String OFFER_ID = "offer_id" ;



    public RoomAdapter(Context context, MyInterface listener){
        this.context= context;
        this.listener=listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RoomItemBinding binding = RoomItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RoomAdapter.ViewHolder holder, int position) {

        holder.binding.room.setText(list.get(position).getName());
        holder.binding.roomNum.setText(list.get(position).getCapacity());
        holder.binding.roomPrice.setText(list.get(position).getPricePerNight()+"");
        holder.binding.availableRoom.setText(list.get(position).getAvailableRooms()+"");

//        Glide.with(context).load(list.get(position).getRoomsExam().getImage1())
//                .transition(withCrossFade())
//                .circleCrop()
//                .apply(new RequestOptions().transform(new RoundedCorners(20))
//                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into((holder).binding.img1);
//
//        Glide.with(context).load(list.get(position).getRoomsExam().getImage2())
//                .transition(withCrossFade())
//                .circleCrop()
//                .apply(new RequestOptions().transform(new RoundedCorners(20))
//                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into((holder).binding.img2);
//
//        Glide.with(context).load(list.get(position).getRoomsExam().getImage3())
//                .transition(withCrossFade())
//                .circleCrop()
//                .apply(new RequestOptions().transform(new RoundedCorners(20))
//                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into((holder).binding.img3);

        holder.binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookingInfo.class);
                intent.putExtra(Constraints.ROOM_PAGE_KEY,Constraints.ROOM_PAGE_VAL);
                intent.putExtra("roomId", list.get(position).getId());
               // intent.putExtra("fromRoom", true);
                Log.e("id",list.get(position).getId()+"");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setdata(List<Data> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        RoomItemBinding binding;
        public ViewHolder(RoomItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}

