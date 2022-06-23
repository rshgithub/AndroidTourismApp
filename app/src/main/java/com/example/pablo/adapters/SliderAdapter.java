package com.example.pablo.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pablo.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder>{


    Context context;
    private List<String> images  ;


    public SliderAdapter(List<String> images, Context context){
        this.images = images;
        this.context = context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        Glide.with(context).load(images).into((viewHolder.imageView));

    }

    @Override
    public int getCount() {
        return images.size();
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_image);

        }
    }

}