package com.example.pablo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.R;
import com.example.pablo.details_activities.MosqueDetails;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.databinding.PopularMosqueBinding;
import com.example.pablo.model.mosques.MosqueExample;
import com.example.pablo.model.mosques.TopMosque;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class popularMosquesAdapter extends RecyclerView.Adapter<popularMosquesAdapter.ViewHolder> {
    private List<TopMosque> list  ;
    Context context;
    private static MyInterface listener ;
    public final static String MOSQUE_ID = "mosque_id" ;


    public popularMosquesAdapter(Context context, MyInterface listener){
        this.context= context;
        this.listener=listener;
    }


    @Override
    public popularMosquesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PopularMosqueBinding binding = PopularMosqueBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new popularMosquesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(popularMosquesAdapter.ViewHolder holder, int position) {

        if (holder!=null){
        holder.binding.mosqueName.setText(list.get(position).getName());
        holder.binding.locationPin.setText(list.get(position).getAddress());

            Glide.with(context).load(list.get(position).getMosqueImage())
                    .transition(withCrossFade())
                    .circleCrop()
                    .apply(new RequestOptions().transform(new RoundedCorners(10))
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                    .error(R.drawable.mosqes).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

                    .into((holder).binding.MosqueImageView);

        holder.binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "http://maps.google.com/maps?saddr=" + "31.503355632448965 ,  34.46231765317062" + "&daddr=" + 31.503355632448965 + "," + 34.46231765317062;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });}


        setUpActions(holder, position);

    }

    private void setUpActions(popularMosquesAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MosqueDetails.class);
                intent.putExtra(MOSQUE_ID, list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }




    public void setData(List<TopMosque> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        PopularMosqueBinding binding;
        public ViewHolder(PopularMosqueBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}
