package com.example.pablo.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pablo.R;
import com.example.pablo.activity.BookingInfo;
import com.example.pablo.activity.Login;
import com.example.pablo.activity.Payment;
import com.example.pablo.adapters.CartAdapter;
import com.example.pablo.databinding.FragmentCartBinding;
import com.example.pablo.interfaces.BookingInterface;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.cart.CartExample;
import com.example.pablo.model.cart.HotelOrderItem;
import com.example.pablo.model.reservations.Datum;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class CartFragment extends Fragment {

    List<HotelOrderItem> list;
    CartAdapter adapter;
    FragmentCartBinding binding;
    static Service service;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartFragment() {
    }


    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(this).build();
        noInternetDialog.setCancelable(true);


        if (!isOnLine()) {
            Dialog dialog = new Dialog(getActivity(), R.style.NoInternet);
            dialog.setContentView(R.layout.no_internet);
            dialog.show();

            Button retry;
            retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOnLine()) {
                        getRoomsCart();
                        dialog.dismiss();
                    }

                }
            });

        }

        swipeRefresh();
        startShimmer();
        list = new ArrayList<>();
        deleteAll();
        adapter();
        getRetrofitInstance();
        getRoomsCart();
        swipeToEditAndDelete();
  //     noData();

        return view;
    }

    private void getRoomsCart() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
        service.getCart(token).enqueue(new Callback<CartExample>() {
            @Override
            public void onResponse(Call<CartExample> call, Response<CartExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                    stopShimmer();
///                    Toast.makeText(getActivity(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();
                    list = response.body().getHotelOrderItems();
//                    noData();
                    adapter.setData(list);

                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CartExample> call, Throwable t) {
                Log.e("error", t.getMessage());

                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_LONG).show();


            }
        });
    }

    //delete
    public void delete(Long id, String token) {
        service.deleteItem(id, token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                    Log.e("code", response.code() + "");
                    Log.e("code", response.body().getId() + "");

                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_LONG).show();

                t.printStackTrace();
                Log.e("code", t.getMessage() + "");
            }
        });

    }

    //clear
    public void clear(String token) {

        service.clearCart(token).enqueue(new Callback<Datum>() {
            @Override
            public void onResponse(Call<Datum> call, Response<Datum> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "clear all successfully", Toast.LENGTH_SHORT).show();
                    Log.e("code", response.code() + "");

                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Datum> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_LONG).show();

                t.printStackTrace();
                Log.e("code", t.getMessage() + "");
            }
        });

    }

    public boolean isOnLine() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    private void deleteAll() {
        binding.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
                dialog.setContentView(R.layout.layout_custom_dialog);

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
                        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                        clear(token);
                        if (list != null)
                            list.clear();
                        adapter.notifyDataSetChanged();
                        binding.totalPrice.setText("0$");
                        binding.count.setText("0");
                        dialog.dismiss();
                        noData();
                    }
                });

                dialog.show();

            }
        });
    }


    private void adapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerView2.setLayoutManager(layoutManager);

        adapter = new CartAdapter(getActivity(), new BookingInterface() {

            @Override
            public void totalPriceOnItemClick(Long price) {
                binding.pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list.size()==0){
                            Toast.makeText(getActivity(), "Cart Is Empty", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(getActivity(), Payment.class);
                        intent.putExtra("price", price);
                        startActivity(intent);
                    }


                    }
                });
                binding.totalPrice.setText(price + "$");


            }

            @Override
            public void countOnItemClick(Long count) {
                binding.count.setText(count + "");
            }

        });

        binding.recyclerView2.setAdapter(adapter);


    }

    private void getRetrofitInstance() {
        service = Service.ApiClient.getRetrofitInstance();

    }

    private void swipeToEditAndDelete() {
        // Create and add a callback
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:

                        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
                        dialog.setContentView(R.layout.layout_custom_dialog2);

                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);

                        Button btnClose = dialog.findViewById(R.id.cancel);
                        Button btnClear = dialog.findViewById(R.id.clear);

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                getRoomsCart();
                            }
                        });

                        btnClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
                                delete(list.get(position).getId(), token);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                             //   noData();
                            }
                        });
                        dialog.show();
                        break;

                    case ItemTouchHelper.RIGHT:
                        Log.e("orderId", list.get(position).getOrderId() + "");
                        Log.e("roomId", list.get(position).getRoomId() + "");

                        Intent intent = new Intent(getActivity(), BookingInfo.class);
                        intent.putExtra("id", list.get(position).getRoomId());
                        intent.putExtra("orderId", list.get(position).getId());
                        intent.putExtra("isEdit", true);
                        adapter.notifyDataSetChanged();
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_mode_edit_24)
                        .addSwipeRightLabel("Edit")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView2);

    }

    private void startShimmer() {
        binding.shimmerLayout.startShimmer();

    }

    private void stopShimmer() {
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void swipeRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                startShimmer();
                list = new ArrayList<>();
                deleteAll();
                adapter();
                getRetrofitInstance();
                getRoomsCart();
                swipeToEditAndDelete();
            }, 1000);
        });
    }

    private void noData() {
        if (list.isEmpty()) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.empty.setText("No Reserved Rooms Yet");
            binding.imageView26.setVisibility(View.VISIBLE);
            binding.imageView26.setImageResource(R.drawable.undraw_empty_cart_co35);
            binding.recyclerView2.setVisibility(View.GONE);
            binding.count.setText("0");
            binding.totalPrice.setText("0$");

        } else {
            binding.empty.setVisibility(View.GONE);
            binding.imageView26.setVisibility(View.GONE);
            binding.recyclerView2.setVisibility(View.VISIBLE);
        }
    }



}