package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pablo.R;
import com.example.pablo.interfaces.RoomsInterface;
import com.example.pablo.databinding.RoomItemBinding;
import com.example.pablo.model.rooms.Data;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{
    private List<Data> list  ;
    Context context;
    private RoomsInterface listener ;

    public RoomAdapter(Context context, RoomsInterface listener){
        this.context= context;
        this.listener=listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RoomItemBinding binding = RoomItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RoomAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.room.setText(list.get(position).getName()+"");
        holder.binding.roomNum.setText(list.get(position).getCapacity());
        holder.binding.roomPrice.setText(list.get(position).getPricePerNight()+"");
        holder.binding.availableRoom.setText(list.get(position).getAvailableRooms()+"");


        if (list.get(position).getAvailableRooms()==0){
            holder.binding.book.setEnabled(false);
            holder.binding.book.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.disable_button));
        }else{
            holder.binding.book.setEnabled(true);
            holder.binding.book.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.booknow));

        }

        List<String> imgeList = list.get(position).getRoomImages();
        switch (imgeList.size()) {
            case 1:
                Glide.with(context).load(imgeList.get(0)).placeholder(R.drawable.bed1).into(holder.binding.img1);
                break;
            case 2:
                Glide.with(context).load(imgeList.get(0)).placeholder(R.drawable.bed1).into(holder.binding.img1);
                Glide.with(context).load(imgeList.get(1)).placeholder(R.drawable.bed1).into(holder.binding.img2);
                break;
            case 3:
                Glide.with(context).load(imgeList.get(0)).placeholder(R.drawable.bed1).into(holder.binding.img1);
                Glide.with(context).load(imgeList.get(1)).placeholder(R.drawable.bed1).into(holder.binding.img2);
                Glide.with(context).load(list.get(position).getRoomImages().get(2)).placeholder(R.drawable.bed1).into(holder.binding.img3);

        }


        listener.onItemRoomClick(list.get(position).getAvailableRooms());

        holder.binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(list.get(position).getId());


            }
        });


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<Data> list) {
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

