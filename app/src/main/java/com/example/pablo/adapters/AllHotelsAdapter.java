package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.details_activities.HotelsDetails;
import com.example.pablo.R;
import com.example.pablo.model.amenities.Amenities;
import com.example.pablo.model.hotel.Hotels;
import com.example.pablo.model.hotel.HotelsData;
import com.example.pablo.model.hotels.Data;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AllHotelsAdapter extends RecyclerView.Adapter<AllHotelsAdapter.ViewHolder> {
    private List<HotelsData> list;
    Context context;

    public AllHotelsAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<HotelsData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addToList(List<HotelsData> myList){
        list.addAll(myList);
        notifyDataSetChanged();
    }

    @Override
    public AllHotelsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotels, parent, false);
        return new AllHotelsAdapter.ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(AllHotelsAdapter.ViewHolder holder, int position) {
      //  if (holder!=null) {
            holder.name.setText(list.get(position).getName());
            holder.city.setText(list.get(position).getAddress());
            holder.rate.setText(list.get(position).getStar()+"");

            Glide.with(context).load(list.get(position).getHotelImage())
                    .transition(withCrossFade())
                            .error(R.drawable.bed1).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.img);

            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uri = "http://maps.google.com/maps?saddr=" + 31.503355632448965 + "," + 34.46231765317062 + "&daddr=" + 31.503355632448965 + "," + 34.46231765317062;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }
            });
     //   }

        holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, HotelsDetails.class);
                    context.startActivity(intent);
                }
            });


        setUpActions(holder, position);

    }

    private void setUpActions(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, HotelsDetails.class);
                intent.putExtra("hotel_id", list.get(position).getId()+"");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,rate,city;
        Button details,map;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.image1);
            name = view.findViewById(R.id.name);
            rate = view.findViewById(R.id.rate);
            city = view.findViewById(R.id.location);
            details = view.findViewById(R.id.details);
            map = view.findViewById(R.id.map);


        }

    }

}