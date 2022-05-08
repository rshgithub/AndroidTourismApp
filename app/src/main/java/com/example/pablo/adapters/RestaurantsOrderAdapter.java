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
import com.example.pablo.model.RestaurantsExam;
import com.example.pablo.databinding.RestaurantsOrderBinding;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RestaurantsOrderAdapter extends RecyclerView.Adapter<RestaurantsOrderAdapter.ViewHolder> {
    private List<RestaurantsExam> list  ;
    Context context;

    public RestaurantsOrderAdapter(Context context){
        this.context= context;

    }


    @Override
    public RestaurantsOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RestaurantsOrderBinding binding = RestaurantsOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RestaurantsOrderAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RestaurantsOrderAdapter.ViewHolder holder, int position) {

        holder.binding.restaurantName.setText(list.get(position).getResName());
        holder.binding.foodName.setText(list.get(position).getFoodName());
        holder.binding.price.setText(list.get(position).getPrice()+"");
        holder.binding.count.setText(list.get(position).getCount()+"");
        holder.binding.date.setText(list.get(position).getDate()+"");

        Glide.with(context).load(list.get(position).getImage())
                .transition(withCrossFade())
                .circleCrop()
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into((holder).binding.image);


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
        RestaurantsOrderBinding binding;
        public ViewHolder(RestaurantsOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}