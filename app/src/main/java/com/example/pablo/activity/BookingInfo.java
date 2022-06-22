package com.example.pablo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.pablo.databinding.ActivityBookingInfoBinding;
import com.example.pablo.model.bookingInfo.CartExample;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.edit.EditExample;
import com.example.pablo.model.hotel.HotelRoom;
import com.example.pablo.model.rooms.RoomsExample;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class BookingInfo extends AppCompatActivity {
    int sum = 1;
    SimpleDateFormat sdf;
    ActivityBookingInfoBinding binding;
    Service service;
    Long roomId,orderId;
    Long tot;
    Long totals;
    Date date2;
    Date date3;
    String checkInDate, checkOutDate;
    boolean isConnected = false;
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkInternetConnection();
        startShimmer();
        getRetrofitInstance();
        getIntentData();
        addToCartButton();
        cart();

    }

    private void bookNow() {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        String check_in = binding.chikinDate.getText().toString();
        String checkout = binding.chikoutDate.getText().toString();
        String count = binding.count.getText().toString();


        service.bookInfo(check_in, checkout, count, roomId + "", token).enqueue(new Callback<CartExample>() {
            @Override
            public void onResponse(Call<CartExample> call, retrofit2.Response<CartExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                    stopShimmer();
                    Toast.makeText(BookingInfo.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CartExample> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRoomDetails(Long roomId) {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getRoomDetails(roomId, token).enqueue(new Callback<RoomsExample>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<RoomsExample> call, Response<RoomsExample> response) {
                Log.e("response code", response.code() + "");

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
                        Toast.makeText(BookingInfo.this, "add day count", Toast.LENGTH_SHORT).show();
                    }

                    // Glide.with(BookingInfo.this).load(response.body().getImage()).circleCrop().into(binding.imageView6);


//-----------------------------room count---------------------------------------


                    binding.min.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sum--;
                            if (sum < 1) {
                                sum = 1;
                                return;
                            }


                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));

                            Long x =   response.body().getData().getPricePerNight()*sum*Count;
                            binding.totalMoney.setText(x+"");
                            binding.count.setText(sum + "");


                        }
                    });
                    //add
                    binding.add.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            sum++;

                            if (sum > response.body().getData().getAvailableRooms()) {
                                return;
                            }else{
                                Long Count = Long.valueOf((binding.dayCount.getText().toString()));

                                Long x =   response.body().getData().getPricePerNight()*sum*Count;
                                binding.totalMoney.setText(x+"");
                                binding.count.setText(sum + "");
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
//                            Date currentDate = new Date();// get the current date
//                            currentDate.setDate(currentDate.getDate());
//                            date2 = currentDate;

                            binding.chikinDate.setText(sdf.format(date2)+"");
//                            binding.chikinDate.setText(sdf.format(date2));
//
//                            //get today date
//                            Date d = new Date();
//                            CharSequence s  = DateFormat.format("dd-MM-yyyy", d.getTime());
//                            binding.chikinDate.setText(s+"");

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
                                Long x =   tot*sum;
                                binding.totalMoney.setText(x + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price* RoomCount;
                                totals = tot * (offer / 100);
                                Long x = tot - totals;
                                Long x1 =   x*sum;
                                binding.totalMoney.setText(x1 + "");

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


                            //get tomorrow date
//                            Date currentDate = new Date();// get the current date
//                            currentDate.setDate(currentDate.getDate() + 1);
//                            date3.setDate(currentDate.getDate() + 1);


                            //day count
                            if (date3 != null && date2 != null) {
                                long day = (date3.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
                                binding.dayCount.setText(day + "");
                            }

                            //total price
                            Long Price = Long.valueOf(((binding.priceHotel.getText().toString())));
                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));
                            Long RoomCount = Long.valueOf((binding.dayCount.getText().toString()));

                            if (response.body().getData().getHasOffer() == 0) {
                                tot = Count * Price* RoomCount;
                                Long x =   tot*sum;

                                binding.totalMoney.setText(x + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price* RoomCount;
                                totals = tot * (offer / 100);
                                Long x = tot - totals;
                                Long x1 =   x*sum;
                                binding.totalMoney.setText(x1 + "");

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
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<RoomsExample> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });


    }

    private void editRoomDetails(Long roomId,Long orderItemId) {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.


        String count = binding.count.getText().toString();

        service.editItem(orderItemId, checkInDate, checkOutDate, count,roomId , "put", token).enqueue(new Callback<EditExample>() {
            @Override
            public void onResponse(Call<EditExample> call, Response<EditExample> response) {
                Log.e("response code", response.code() + "");
                if (response.isSuccessful()) {
//                    getRoomDetails(roomId);
                    stopShimmer();
                    Toast.makeText(BookingInfo.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

//-----------------------------room count---------------------------------------

                    binding.min.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sum--;
                            if (sum < 1) {
                                sum = 1;
                                return;
                            }

                            binding.count.setText(sum + "");

                        }
                    });
                    //add
                    binding.add.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            sum++;

//                            if (sum > response.body().getData().getAvailableRooms()) {
//                                return;
//                            }
                            binding.count.setText(sum + "");


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
                            Long Price = Long.valueOf((binding.priceHotel.getText().toString()));
                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));

//                            if (response.body().getData().getRoomHasOffer() == null) {
//                                tot = Count * Price;
//                                binding.totalMoney.setText(tot + "");
//
//                            } else {
//                                Long offer = Long.valueOf((response.body().getData().getRoomHasOffer() + ""));
//                                tot = Count * Price;
//                                totals = tot * (offer / 100);
//                                Long x = tot - totals;
//                                binding.totalMoney.setText(x + "");
//
//                            }

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
                            Long Price = Long.valueOf((binding.priceHotel.getText().toString()));
                            Long Count = Long.valueOf((binding.dayCount.getText().toString()));
//                            if (response.body().getData().getRoomHasOffer() == null) {
//                                tot = Count * Price;
//                                binding.totalMoney.setText(tot + "");
//
//                            } else {
//                                Long offer = Long.valueOf((response.body().getData().getRoomHasOffer() + ""));
//                                tot = Count * Price;
//                                totals = tot * (offer / 100);
//                                Long x = tot - totals;
//                                binding.totalMoney.setText(x + "");
//
//                            }


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
                    Toast.makeText(getBaseContext(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<EditExample> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(BookingInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getBaseContext(),"Connected",Toast.LENGTH_SHORT).show();
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


            if (isEdit) {
                editRoomDetails(roomId,orderId);
            } else {
                getRoomDetails(roomId);
            }
        }
    }

    private void addToCartButton(){
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookNow();
            }
        });

    }

    private void cart(){
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Cart.class);
                startActivity(intent);
                finish();
            }
        });

    }
}