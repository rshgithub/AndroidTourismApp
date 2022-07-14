package com.example.pablo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pablo.R;
import com.example.pablo.databinding.ActivityBookingInfoBinding;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.fragments.CartFragment;
import com.example.pablo.model.bookingInfo.CartExample;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.edit.EditExample;
import com.example.pablo.model.edit_order.EditOrderDetails;
import com.example.pablo.model.rooms.RoomsExample;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.TokenKey;

public class BookingInfo extends AppCompatActivity {
    Long sum = Long.valueOf(1);
    SimpleDateFormat sdf;
    ActivityBookingInfoBinding binding;
    Service service;
    Long roomId,orderId;
    double tot;
    Double total;
    Date date2;
    Date date3;
    String checkInDate, checkOutDate;
    boolean isConnected = false;
    KProgressHUD hud;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.progress.setVisibility(View.GONE);

        service = Service.ApiClient.getRetrofitInstance();


        if (getIntent() != null) {

            boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
            orderId = getIntent().getLongExtra("orderId", 0);
            roomId = getIntent().getLongExtra("id", 0);
            Log.e("room1",roomId+"");
            Log.e("orderId1",orderId+"");

            if (isEdit) {
                 editRoomDetails();
                getEditOrderDetails();
                binding.addToCart.setVisibility(View.GONE);
                binding.editCart.setVisibility(View.VISIBLE);
            } else {
                binding.addToCart.setVisibility(View.VISIBLE);
                binding.editCart.setVisibility(View.GONE);
                getRoomDetails(roomId);
            }

        }


        checkInternetConnection();

        startShimmer();

        getRetrofitInstance();

      //  getIntentData();

        addToCartButton();

      //  cart();

        cartIntent();

       editButton();


    }



    private void bookNow() {
        hud = KProgressHUD.create(BookingInfo.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        hud.setProgress(90);

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");//"No name defined" is the default value.

        String check_in = binding.chikinDate.getText().toString();
        String checkout = binding.chikoutDate.getText().toString();
        String count = binding.count.getText().toString();


        service.bookInfo(check_in, checkout, count, roomId + "", orderId + "", token).enqueue(new Callback<CartExample>() {
            @Override
            public void onResponse(Call<CartExample> call, retrofit2.Response<CartExample> response) {
                Log.e("response code", response.code() + "book");

                if (response.isSuccessful()) {
                    stopShimmer();
                    hud.dismiss();
                    Intent intent=new Intent(getBaseContext(),BottomNavigationBarActivity.class);
                    intent.putExtra("cart","cart");
                    startActivity(intent);
                    finish();
                } else {
                    hud.dismiss();

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.warning(BookingInfo.this, errorMessage, Toast.LENGTH_SHORT, true).show();

                }
            }

            @Override
            public void onFailure(Call<CartExample> call, Throwable t) {
                t.printStackTrace();
                hud.dismiss();

                Toasty.error(BookingInfo.this,  t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });
    }


    private void getRoomDetails(Long roomId) {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");//"No name defined" is the default value.

        service.getRoomDetails(roomId, token).enqueue(new Callback<RoomsExample>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<RoomsExample> call, Response<RoomsExample> response) {
                Log.e("response code", response.code() + "rd");

                if (response.body() != null) {
                    stopShimmer();

                    String myFormat = "dd-MM-yyyy"; //MMM dd, ''yyyy
                    sdf = new SimpleDateFormat(myFormat, Locale.US);

                    Date currentDate = new Date();// get the current date
                    currentDate.setDate(currentDate.getDate() + 1);
                    binding.chikoutDate.setText(sdf.format(currentDate)+"");

                    Date currentDate1 = new Date();// get the current date
                    currentDate1.setDate(currentDate1.getDate());
                    binding.chikinDate.setText(sdf.format(currentDate1)+"");

                    binding.count.setText("1");

                    binding.totalMoney.setText(response.body().getData().getPricePerNight() + "");
                    if (response.body().getData().getHasOffer() == null){
                        Long x =   response.body().getData().getPricePerNight();
                        binding.totalMoney.setText(x+"");

                    }else{
                        Long hotel_price = (response.body().getData().getPricePerNight());
                        tot = 1 * hotel_price * sum;
                        double offer= (double) response.body().getData().getHasOffer()/ 100;

                        total = tot * offer;
                        double x = tot - total;
                        binding.totalMoney.setText(x + "");

                        Log.e("discount",offer+"");
                    }

                    binding.dayCount.setText("1");

                    binding.hotelName.setText(response.body().getData().getName() + "");
                    binding.priceHotel.setText(response.body().getData().getPricePerNight() + "");
                    binding.personNum.setText(response.body().getData().getAvailableRooms() + "");
                    binding.roomPrice.setText(response.body().getData().getPricePerNight() + "");
                   // binding.totalMoney.setText(0 + "");
                    if (response.body().getData().getHasOffer() == null) {
                        binding.offer.setText("0");
                    } else {
                        binding.offer.setText(response.body().getData().getHasOffer() + "");
                    }



                    if(binding.count.getText().toString().equals(0)){
                         binding.chikinDate.setText(00+"");
                         binding.chikoutDate.setEnabled(false);
                        Toasty.warning(BookingInfo.this,"please add day count", Toast.LENGTH_SHORT, true).show();
                    }

    Glide.with(BookingInfo.this).load(response.body().getData().getRoomImages().get(0)).placeholder(R.drawable.bed1).into(binding.imageView6);

                    Log.e("room_image",response.body().getData().getRoomImages()+"");

//-----------------------------room count---------------------------------------


                    binding.min.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sum--;
                            if (sum >= 1) {
                                binding.min.setEnabled(true);
                                Long dayCount = Long.valueOf((binding.dayCount.getText().toString()));
                                binding.count.setText(sum + "");

                                if (response.body().getData().getHasOffer() == null){
                                    Long x =   response.body().getData().getPricePerNight()*sum*dayCount;
                                    binding.totalMoney.setText(x+"");

                                }else{
                                    Long hotel_price = (response.body().getData().getPricePerNight());
                                    tot = dayCount * hotel_price * sum;
                                    double offer= (double) response.body().getData().getHasOffer()/ 100;

                                    total = tot * offer;
                                    double x = tot - total;
                                    binding.totalMoney.setText(x + "");

                                    Log.e("discount",offer+"");
                                }
                            }else {
                                binding.min.setEnabled(false);
                                binding.add.setEnabled(true);
                                 sum = Long.valueOf(1);


                            }




                        }
                    });
                    //add
                    binding.add.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            sum++;

                            Long available = response.body().getData().getAvailableRooms();

                            if (sum <= response.body().getData().getAvailableRooms()) {
                                binding.add.setEnabled(true);
                                binding.min.setEnabled(true);

                                Long dayCount = Long.valueOf((binding.dayCount.getText().toString()));
                                binding.count.setText(sum + "");

                                if (response.body().getData().getHasOffer() == null){
                                    Long x =   response.body().getData().getPricePerNight()*sum*dayCount;
                                    binding.totalMoney.setText(x+"");

                                }else{
                                    Long hotel_price = (response.body().getData().getPricePerNight());
                                    tot = dayCount * hotel_price * sum;
                                    double offer= (double) response.body().getData().getHasOffer()/ 100;

                                    total = tot * offer;
                                    double x = tot - total;
                                    binding.totalMoney.setText(x + "");

                                    Log.e("discount",offer+"");
                                }
                            }else {
                                binding.add.setEnabled(false);
                                binding.min.setEnabled(true);
                                sum = available;


                            }





                        }
                    });
//-------------------------------- check in date -------------------------------------
                    final Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "dd-MM-yyyy"; //MMM dd, ''yyyy
                            sdf = new SimpleDateFormat(myFormat, Locale.US);

                            date2 = myCalendar.getTime();
                            binding.chikinDate.setText(sdf.format(date2)+"");
                            //day count
                          if (date3 != null && date2 != null) {
                                Long day = (date3.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
                                binding.dayCount.setText(day + "");
                           }

                            //total price
                            Long Price = Long.valueOf((binding.priceHotel.getText().toString()));
                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));
                            Long RoomCount = Long.valueOf((binding.count.getText().toString()));

                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price * RoomCount;
                                double x =   tot*sum;
                                binding.totalMoney.setText(x + "");

                            } else {
                                Long hotel_price = (response.body().getData().getPricePerNight());
                                tot = Count * hotel_price * sum;
                                double offer= (double) response.body().getData().getHasOffer()/ 100;

                                total = tot * offer;
                                double x = tot - total;
                                binding.totalMoney.setText(x + "");

                                Log.e("discount",offer+"");
                            }

                        }
                    };
                    binding.chikinDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(BookingInfo.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });
                    binding.chikinDate.setVisibility(View.VISIBLE);


//-------------------------------- check out Date -------------------------------------
                    final Calendar myCalendar1 = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar1.set(Calendar.YEAR, year);
                            myCalendar1.set(Calendar.MONTH, monthOfYear);
                            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                            String myFormat = "dd-MM-yyyy"; //In which you need put here

                            sdf = new SimpleDateFormat(myFormat, Locale.US);
                            date3 = myCalendar1.getTime();
                            binding.chikoutDate.setText(sdf.format(date3)+"");

                            //day count
                            if (date3 != null && date2 != null) {
                                long day = (date3.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
                                binding.dayCount.setText(day + "");
                            }

                            //total price
                            Long Price = Long.valueOf(((binding.priceHotel.getText().toString())));
                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));
                            Long RoomCount = Long.valueOf((binding.dayCount.getText().toString()));

                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price* RoomCount;
                                double x =   tot*sum;

                                binding.totalMoney.setText(x + "");

                            } else {
                                Long hotel_price = (response.body().getData().getPricePerNight());
                                tot = Count * hotel_price * sum;
                                double offer= (double) response.body().getData().getHasOffer()/ 100;

                                total = tot * offer;
                                double x = tot - total;
                                binding.totalMoney.setText(x + "");

                                Log.e("discount",offer+"");

                            }


                        }
                    };
                    binding.chikoutDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(BookingInfo.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                                    myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    binding.chikoutDate.setVisibility(View.VISIBLE);


                    Log.e("Success", new Gson().toJson(response.body()));
                }

                else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(BookingInfo.this,errorMessage, Toast.LENGTH_SHORT, true).show();

                }
            }


            @Override
            public void onFailure(Call<RoomsExample> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }

        });


    }


    private void editRoomDetails() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.progress.setIndeterminate(true);

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");//"No name defined" is the default value.


        String count = binding.count.getText().toString();

        service.editItem(orderId, checkInDate, checkOutDate, count, roomId, "put", token).enqueue(new Callback<EditExample>() {
            @Override
            public void onResponse(Call<EditExample> call, Response<EditExample> response) {
                Log.e("response code", response.code() + "edr");
                if (response.isSuccessful()) {
                    stopShimmer();
                    binding.progress.setVisibility(View.GONE);


//-----------------------------room count---------------------------------------

              //      binding.count.setText(response.body().getData().getRoomCount()+"");

                    binding.min.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sum--;
                            if (sum >= 1) {
                                binding.min.setEnabled(true);
//                                Long dayCount = Long.valueOf((binding.dayCount.getText().toString()));
                                binding.count.setText(sum + "");


                                if (response.body().getData().getRoomHasOffer() == null){
                                    double x =   response.body().getData().getRoomPricePerNight()*sum*Double.valueOf((binding.dayCount.getText().toString()));
                                    binding.totalMoney.setText(x+"");

                                }else{
                                    Double hotel_price = (response.body().getData().getRoomPricePerNight());
                                    tot = Double.valueOf((binding.dayCount.getText().toString())) * hotel_price * sum;
                                    double offer= (double) response.body().getData().getRoomHasOffer()/ 100;

                                    total = tot * offer;
                                    double x = tot - total;
                                    binding.totalMoney.setText(x + "");

                                    Log.e("discount",offer+"");
                                }
                            }else {
                                binding.min.setEnabled(false);
                                binding.add.setEnabled(true);
                                sum = Long.valueOf(1);


                            }




                        }
                    });

                    //add
                    binding.add.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            sum++;

                            Long available = response.body().getData().getAvailableRooms();

                            if (sum <= response.body().getData().getAvailableRooms()) {
                                binding.add.setEnabled(true);
                                binding.min.setEnabled(true);

//                                double dayCount = Double.valueOf((binding.dayCount.getText().toString()));
                                binding.count.setText(sum + "");

                                if (response.body().getData().getRoomHasOffer() == null){
                                    double x =   response.body().getData().getRoomPricePerNight()*sum*Double.valueOf((binding.dayCount.getText().toString()));
                                    binding.totalMoney.setText(x+"");

                                }else{
                                    Double hotel_price = (response.body().getData().getRoomPricePerNight());
                                    tot = Double.valueOf((binding.dayCount.getText().toString())) * hotel_price * sum;
                                    double offer= (double) response.body().getData().getRoomHasOffer()/ 100;

                                    total = tot * offer;
                                    double x = tot - total;
                                    binding.totalMoney.setText(x + "");

                                    Log.e("discount",offer+"");
                                }
                            }else {
                                binding.add.setEnabled(false);
                                binding.min.setEnabled(true);
                                sum = available;


                            }





                        }
                    });
//-------------------------------- check in date -------------------------------------
                    final Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "dd-MM-yyyy"; //MMM dd, ''yyyy
                            sdf = new SimpleDateFormat(myFormat, Locale.US);

                            date2 = myCalendar.getTime();
                            binding.chikinDate.setText(sdf.format(date2));

                            //day count
                            if (date3 != null && date2 != null) {
                                long day = (date3.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
                                binding.dayCount.setText(day + "");
                            }

                            //total price
                         //   Long Price = Long.valueOf((binding.priceHotel.getText().toString()));
//                            Long Count = Double.valueOf((binding.dayCount.getText().toString()));

                            if (response.body().getData().getRoomHasOffer() == null) {
                                tot = Double.valueOf((binding.dayCount.getText().toString())) *  Double.valueOf((binding.priceHotel.getText().toString()));
                                binding.totalMoney.setText(tot + "");

                            } else {
                                Double hotel_price = (response.body().getData().getRoomPricePerNight());
                                tot = Double.valueOf((binding.dayCount.getText().toString())) * hotel_price * sum;
                                double offer= (double) response.body().getData().getRoomHasOffer()/ 100;

                                total = tot * offer;
                                double x = tot - total;
                                binding.totalMoney.setText(x + "");

                                Log.e("discount",offer+"");

                            }

                        }
                    };
                    binding.chikinDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(BookingInfo.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });
                    binding.chikinDate.setVisibility(View.VISIBLE);


//-------------------------------- check out Date -------------------------------------
                    final Calendar myCalendar1 = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar1.set(Calendar.YEAR, year);
                            myCalendar1.set(Calendar.MONTH, monthOfYear);
                            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                            String myFormat = "dd-MM-yyyy"; //In which you need put here

                            sdf = new SimpleDateFormat(myFormat, Locale.US);
                            date3 = myCalendar1.getTime();

                            binding.chikoutDate.setText(sdf.format(date3));

                            //day count
                            if (date3 != null && date2 != null) {
                                long day = (date3.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
                                binding.dayCount.setText(day + "");
                            }

                            //total price
//                            Long Price = Long.valueOf((binding.priceHotel.getText().toString()));
//                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));
                            if (response.body().getData().getRoomHasOffer() == null) {
                                tot = Double.valueOf((binding.dayCount.getText().toString())) *  Double.valueOf((binding.priceHotel.getText().toString()));
                                binding.totalMoney.setText(tot + "");

                            } else {
                                Double hotel_price = (response.body().getData().getRoomPricePerNight());
                                tot = Double.valueOf((binding.dayCount.getText().toString())) * hotel_price * sum;
                                double offer= (double) response.body().getData().getRoomHasOffer()/ 100;

                                total = tot * offer;
                                double x = tot - total;
                                binding.totalMoney.setText(x + "");

                                Log.e("discount",offer+"");

                            }


                        }
                    };
                    binding.chikoutDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(BookingInfo.this, date1, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                                    myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    binding.chikoutDate.setVisibility(View.VISIBLE);


                    Log.e("Success", new Gson().toJson(response.body()));
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(BookingInfo.this, response.message(), Toast.LENGTH_SHORT, true).show();


                }
            }


            @Override
            public void onFailure(Call<EditExample> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }

        });

    }


    private void getEditOrderDetails() {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");//"No name defined" is the default value.

        service.getEditOrdersDetails(orderId, token).enqueue(new Callback<EditOrderDetails>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<EditOrderDetails> call, Response<EditOrderDetails> response) {
                Log.e("response code", response.code() + "edito");

                if (response.body() != null) {
                    stopShimmer();

                    binding.count.setText(response.body().getData().getRoomCount()+"");
                    binding.totalMoney.setText(response.body().getData().getOrderTotalPrice()+"");
                    binding.hotelName.setText(response.body().getData().getHotelName()+"");
                    binding.priceHotel.setText(response.body().getData().getRoomPricePerNight()+"");
                    binding.chikinDate.setText(response.body().getData().getCheckIn()+"");
                    binding.chikoutDate.setText(response.body().getData().getCheckOut()+"");
                    binding.dayCount.setText(response.body().getData().getTotalNights()+"");
                    binding.roomPrice.setText(response.body().getData().getRoomPricePerNight()+"");
                    binding.offer.setText(response.body().getData().getRoomHasOffer()+"");
                    binding.personNum.setText(response.body().getData().getAvailableRooms()+"");
                    Glide.with(getBaseContext()).load(response.body().getData().getRoomImage())
                            .error(R.drawable.bed1).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.imageView6);



                    Log.e("Success", new Gson().toJson(response.body()));
                    Log.e("count", response.body().getData().getRoomCount()+"");
                    Log.e("offer", response.body().getData().getRoomHasOffer()+"");
                    Log.e("totalMoney", response.body().getData().getOrderTotalPrice()+"");
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toasty.error(BookingInfo.this,response.message(), Toast.LENGTH_SHORT, true).show();

                }
            }



            @Override
            public void onFailure(Call<EditOrderDetails> call, Throwable t) {
                t.printStackTrace();

                Toasty.error(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }

        });


    }


    private void startShimmer(){
        binding.shimmerLayout.startShimmer();
        binding.container.setVisibility(View.GONE);

    }


    private void stopShimmer(){
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.container.setVisibility(View.VISIBLE);
    }


    private void checkInternetConnection(){
        if (!isOnLine()){
            if (isConnected){
                Toasty.success(BookingInfo.this, "connected", Toast.LENGTH_SHORT, true).show();
            }else{

                Intent i = new Intent(getBaseContext(), NoInternetConnection.class);
                startActivity(i);
                finish();

            }
        }
    }


    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;
        }
        return true;
    }


    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();

    }


    public void getIntentData(){

        if (getIntent() != null) {

            boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
            orderId = getIntent().getLongExtra("orderId", 0);
            roomId = getIntent().getLongExtra("id", 0);
            Log.e("room1",roomId+"");
            Log.e("orderId1",orderId+"");

            if (isEdit) {
               // editRoomDetails();
                getEditOrderDetails();
                binding.addToCart.setVisibility(View.GONE);
                binding.editCart.setVisibility(View.VISIBLE);
            } else {
                binding.addToCart.setVisibility(View.VISIBLE);
                binding.editCart.setVisibility(View.GONE);
                getRoomDetails(roomId);
            }

        }
    }


    private void addToCartButton(){
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookNow();
//                finish();
            }
        });


    }


    private void cart(){
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Cart.class);
                startActivity(intent);
//                finish();
            }
        });

    }


    private void editButton(){

        binding.editCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRoomDetails();
                finish();

            }
        });
    }


    private void cartIntent(){
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),BottomNavigationBarActivity.class);
                intent.putExtra("cart","cart");
                startActivity(intent);
//                finish();
            }
        });
    }

}