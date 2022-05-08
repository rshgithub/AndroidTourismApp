package com.example.pablo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.R;
import com.example.pablo.model.RestaurantsExam;
import com.example.pablo.databinding.AllRestaurantsBinding;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {
    private List<RestaurantsExam> list  ;
    Context context;
    MyInterface listener;
    public RestaurantsAdapter(Context context, MyInterface listener){
        this.context= context;
        this.listener=listener;

    }


    @Override
    public RestaurantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        AllRestaurantsBinding binding = AllRestaurantsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RestaurantsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RestaurantsAdapter.ViewHolder holder, int position) {

        holder.binding.restaurantName2.setText(list.get(position).getResName());
        holder.binding.location.setText(list.get(position).getFoodName());

        Glide.with(context).load(list.get(position).getImage())
                .transition(withCrossFade())
                .circleCrop()
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into((holder).binding.restaurantImage);


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setdata(List<RestaurantsExam> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        AllRestaurantsBinding binding;
        public ViewHolder(AllRestaurantsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}