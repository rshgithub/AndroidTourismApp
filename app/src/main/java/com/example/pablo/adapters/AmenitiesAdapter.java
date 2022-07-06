package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.databinding.AmenitiesBinding;
import com.example.pablo.databinding.MosquesItemBinding;
import com.example.pablo.details_activities.HotelsDetails;
import com.example.pablo.model.amenities.Amenities;
import com.example.pablo.model.hotels.Data;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {
    private List<Amenities> list;
    Context context;

    public AmenitiesAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Amenities> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public AmenitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AmenitiesBinding binding = AmenitiesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AmenitiesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AmenitiesAdapter.ViewHolder holder, int position) {
        if (holder != null) {
            holder.binding.name.setText(list.get(position).getName());

          holder.binding.color.setBackgroundColor(Color.parseColor(list.get(position).getColor()));

            Glide.with(context).load(list.get(position).getIcon())
                    .transition(withCrossFade())
                    .error(R.drawable.apple).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.binding.image);
        }


    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AmenitiesBinding binding;
        public ViewHolder(AmenitiesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

}