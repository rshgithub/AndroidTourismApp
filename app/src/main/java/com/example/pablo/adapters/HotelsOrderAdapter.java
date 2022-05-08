package com.example.pablo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.R;
import com.example.pablo.databinding.HotelOrderBinding;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.HotelOrderItem;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HotelsOrderAdapter  extends RecyclerView.Adapter<HotelsOrderAdapter.ViewHolder> {
    private List<HotelOrderItem> list  ;
    Context context;

    public HotelsOrderAdapter(Context context){
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
        holder.binding.room.setText(list.get(position).getCheckIn()+" - "+list.get(position).getCheckOut());
        holder.binding.price.setText(list.get(position).getOrderTotalPrice()+"");
        holder.binding.count.setText(list.get(position).getRoomCount()+"");

//        Glide.with(context).load(list.get(position).getImage())
//                .transition(withCrossFade())
//                .circleCrop()
//                .apply(new RequestOptions().transform(new RoundedCorners(20))
//                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//                .into((holder).binding.image);


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

//
    public void setData(List<HotelOrderItem> list) {
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
