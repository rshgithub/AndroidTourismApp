package com.example.pablo.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.example.pablo.details_activities.HotelOrdersDetails;
import com.example.pablo.databinding.HotelOrderBinding;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.order_details.HotelOrderItem;
import com.example.pablo.model.orders.Datum;
import com.example.pablo.model.orders.OrdersExample;

import java.util.HashSet;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Signup.TokenKey;


public class HotelsOrderAdapter  extends RecyclerView.Adapter<HotelsOrderAdapter.ViewHolder> {
    public List<Datum> list  ;
    Context context;
    Service service;
    public static String ORDERDETAILS = "order_id";
;

    public HotelsOrderAdapter(Context context ){
        this.context= context;
    }



    @Override
    public HotelsOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HotelOrderBinding binding = HotelOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HotelsOrderAdapter.ViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(HotelsOrderAdapter.ViewHolder holder, int position) {

        holder.binding.date.setText(list.get(position).getTimeCount()+"");
        holder.binding.price.setText(list.get(position).getTotalPrice()+"$");
        holder.binding.count.setText(list.get(position).getOrderItemsCount()+"  Room Count");
        holder.binding.hotelName.setText(list.get(position).getHotelName());
        holder.binding.status.setText(list.get(position).getStatus());
        Glide.with(context).load(list.get(position).getHotel_image())
                 .error(R.drawable.bed1).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.image);


        if(list.get(position).getStatus().equals("rejected")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.binding.status.setBackgroundResource(R.drawable.rejected_background);
        }else if (list.get(position).getStatus().equals("pending")){//Checkout !!
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.binding.status.setBackgroundResource(R.drawable.pending_background);
        }else if (list.get(position).getStatus().equals("approved")){
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.binding.status.setBackgroundResource(R.drawable.approved_background);
        }else{
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.binding.status.setBackgroundResource(R.drawable.rejected_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, HotelOrdersDetails.class);
                intent.putExtra("order_id", list.get(position).getId());
                context.startActivity(intent);


            }
        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("code", list.get(position).getId() + "");

                if(list.get(position).getStatus().equals("rejected")||list.get(position).getStatus().equals("approved")){

                Dialog dialog = new Dialog(context, R.style.DialogStyle);
                dialog.setContentView(R.layout.layout_custom_dialog2);

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);

                Button btnClose = dialog.findViewById(R.id.cancel);
                Button btnClear = dialog.findViewById(R.id.clear);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("code", list.get(position).getId() + "");

                        Login.SP = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        String token = Login.SP.getString(TokenKey, "");
                        delete(list.get(position).getId(),token,dialog,position);

                    }
                });
                dialog.show();
                }
                else {

                    Dialog dialog = new Dialog(context, R.style.DialogStyle);
                    dialog.setContentView(R.layout.layout_custom_dialog2);

                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);

                    Button btnClose = dialog.findViewById(R.id.cancel);
                    Button btnClear = dialog.findViewById(R.id.clear);
                    TextView text = dialog.findViewById(R.id.textView17);
                    text.setText("Are you sure you want to delete this order ? , its still pending if you want to delete it , will refund your money");

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Login.SP = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                            String token = Login.SP.getString(TokenKey, "");
                            Log.e("code", list.get(position).getId() + "");
                            Log.e("token", token + "");
                            delete(list.get(position).getId(),token,dialog,position);
                        }
                    });
                    dialog.show();
                }
            }
        });



    }

    public void delete(Long id, String token, Dialog dialog, int position) {
        Service.ApiClient.getRetrofitInstance().deleteHotelOrders(id, token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {
                if (response.isSuccessful()) {
                    notifyDataSetChanged();
                    dialog.dismiss();
                    list.remove(position);
                    notifyItemRemoved(position);
                    Toasty.success(context, response.message() + "", Toast.LENGTH_LONG).show();
                    Log.e("code", response.code() + "");
                    Log.e("code", response.body().getId() + "");
                } else {
                    dialog.dismiss();
                    Toasty.error(context, "something went wrong", Toast.LENGTH_SHORT).show();
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(context, response.message() + "", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Datum> call, Throwable t) {
                Toasty.error(context, t.getMessage() + "", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                Log.e("code", t.getMessage() + "");
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
        HotelOrderBinding binding;
        public ViewHolder(HotelOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

}
