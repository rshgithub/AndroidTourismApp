package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.databinding.AmenitiesBinding;
import com.example.pablo.databinding.NotificationItemBinding;
import com.example.pablo.model.amenities.Amenities;
import com.example.pablo.model.notification.NotificationData;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationData> list;
    Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<NotificationData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NotificationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        if (holder != null) {
            holder.binding.tvNotificationText.setText(list.get(position).getTitle());
            holder.binding.tvNotificationBody.setText(list.get(position).getBody());
            holder.binding.tvNotificationTime.setText(list.get(position).getTime_count());
            holder.binding.tvNotificationDate.setText(list.get(position).getCreated_at());


            if(list.get(position).getStatus()==1){
                holder.binding.imageView22.setBackgroundResource(R.drawable.side_color);
            }else{
                holder.binding.imageView22.setBackgroundResource(R.drawable.side_error_color);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding binding;
        public ViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

}