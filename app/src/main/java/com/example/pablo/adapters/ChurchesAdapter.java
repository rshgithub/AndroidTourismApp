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
import com.example.pablo.details_activities.ChurchesDetails;
import com.example.pablo.interfaces.MyInterface;
import com.example.pablo.databinding.MosquesItemBinding;
import com.example.pablo.model.churches.Datum;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ChurchesAdapter  extends RecyclerView.Adapter<ChurchesAdapter.ViewHolder> {
    private List<Datum> list  ;
    Context context;
    private static MyInterface listener ;
    public final static String CHURCHES_ID = "Churches_id" ;

    public ChurchesAdapter(Context context, MyInterface listener){
        this.context= context;
        this.listener=listener;
    }


    @Override
    public ChurchesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MosquesItemBinding binding = MosquesItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChurchesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ChurchesAdapter.ViewHolder holder, int position) {

        holder.binding.name.setText(list.get(position).getName());
        holder.binding.address.setText(list.get(position).getAddress());
        holder.binding.availableTime.setText(list.get(position).getAvailableTime());

        Glide.with(context).load(list.get(position).getChurchImage())
                .transition(withCrossFade())
                .circleCrop()
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                        .error(R.drawable.mosqes).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into((holder).binding.image);

        holder.binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "http://maps.google.com/maps?saddr=" + 31.503355632448965 + "," + 34.46231765317062 + "&daddr=" + 31.503355632448965 + "," + 34.46231765317062;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
        setUpActions(holder, position);

    }

    private void setUpActions(ChurchesAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChurchesDetails.class);
                intent.putExtra(CHURCHES_ID, list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public void setData(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        MosquesItemBinding binding;
        public ViewHolder(MosquesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

}
