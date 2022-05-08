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
import com.example.pablo.databinding.FreshRecipesBinding;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FreshRecipesAdapter extends RecyclerView.Adapter<FreshRecipesAdapter.ViewHolder> {
    private List<RestaurantsExam> list  ;
    Context context;
    MyInterface listener;
    public FreshRecipesAdapter(Context context, MyInterface listener){
        this.context= context;
        this.listener=listener;

    }


    @Override
    public FreshRecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FreshRecipesBinding binding = FreshRecipesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FreshRecipesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FreshRecipesAdapter.ViewHolder holder, int position) {

        holder.binding.ingredients.setText(list.get(position).getFoodName());
        holder.binding.restaurantName.setText(list.get(position).getResName());
        holder.binding.time.setText(list.get(position).getDate());
        holder.binding.restaurantName.setText(list.get(position).getResName());

        Glide.with(context).load(list.get(position).getImage())
                .transition(withCrossFade())
                .circleCrop()
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                        .error(R.drawable.applogo_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into((holder).binding.imageView8);


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
        FreshRecipesBinding binding;
        public ViewHolder(FreshRecipesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}