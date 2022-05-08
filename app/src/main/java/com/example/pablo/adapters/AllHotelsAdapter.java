package com.example.pablo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pablo.model.hotels.Data;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AllHotelsAdapter extends RecyclerView.Adapter<AllHotelsAdapter.ViewHolder> {
    private List<Data> list;
    Context context;

    public AllHotelsAdapter(Context context) {
        this.context = context;
    }

    public void setdata(List<Data> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public AllHotelsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotels, parent, false);
        return new AllHotelsAdapter.ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(AllHotelsAdapter.ViewHolder holder, int position) {
        if (holder!=null) {
            holder.name.setText(list.get(position).getName());
            holder.city.setText(list.get(position).getAddress());
            holder.rate.setText(list.get(position).getStar()+"");

//            Glide.with(context).load(list.get(position).getHotelImage())
//                    .transition(withCrossFade())
//                    .circleCrop()
//                    .apply(new RequestOptions().transform(new RoundedCorners(10))
//                           .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
//                    .into(holder.img);
        }

        setUpActions(holder, position);

    }

    private void setUpActions(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, HotelsDetails.class);
                intent.putExtra("hotel_id", list.get(position).getId());
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
        Button button;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            rate = view.findViewById(R.id.rate);
            city = view.findViewById(R.id.location);
            button = view.findViewById(R.id.button);


        }

    }

}