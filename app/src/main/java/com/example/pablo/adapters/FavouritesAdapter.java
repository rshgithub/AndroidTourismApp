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
import com.example.pablo.databinding.FavouritesBinding;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FavouritesAdapter  extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private List<RestaurantsExam> list  ;
    Context context;

    public FavouritesAdapter(Context context){
        this.context= context;

    }


    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FavouritesBinding binding = FavouritesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FavouritesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FavouritesAdapter.ViewHolder holder, int position) {

        holder.binding.restartName.setText(list.get(position).getResName());
        holder.binding.foodName.setText(list.get(position).getFoodName());
        Glide.with(context).load(list.get(position).getImage())
                .transition(withCrossFade())
                .circleCrop()
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                       .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
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
        FavouritesBinding binding;
        public ViewHolder(FavouritesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}
