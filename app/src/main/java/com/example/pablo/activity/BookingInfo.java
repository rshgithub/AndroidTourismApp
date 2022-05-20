package com.example.pablo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pablo.Cart;
import com.example.pablo.Constraints;
import com.example.pablo.Payment;
import com.example.pablo.adapters.CartAdapter;
import com.example.pablo.adapters.RoomAdapter;
import com.example.pablo.model.bookingInfo.CartExample;
import com.example.pablo.databinding.ActivityBookingInfoBinding;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.buyorder.BuyOrderExample;
import com.example.pablo.model.edit.EditExample;
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
    int sum = 0;
    SimpleDateFormat sdf;
    ActivityBookingInfoBinding binding;
    Service service;
    int roomId, orderItemId;
    int tot;
    int totals;
    Date date2;
    Date date3;
    String checkInDate, checkOutDate;
    public static String CHECKIN, CHECKOUT, DAYCOUNT, PRICE;
    Bundle extras;
    Intent intent;
    boolean fromRoom;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = Service.ApiClient.getRetrofitInstance();
////from room adapter
//        if (getIntent().getIntExtra("roomId", 0) == 0) {
//            roomId = getIntent().getIntExtra("roomId", 0);
//            getRoomDetails(roomId);
//            Log.e("offerId", roomId + "");
//
//        }
//        //from cart
//        else if (getIntent().getIntExtra("editId", 0) == 1) {
//
//            roomId = getIntent().getIntExtra("roomId", 0);
//            orderItemId = getIntent().getIntExtra("editId", 0);
//            getRoomDetails(roomId);
//            editRoomDetails(orderItemId);
//            Log.e("orderItemId", orderItemId + "");
//        }

//       // if (getIntent().getIntExtra("roomId", 0) == isTrue)
        if (getIntent() != null) {
            roomId = getIntent().getIntExtra("roomId", 0);
            fromRoom = getIntent().getBooleanExtra("fromRoom", true);
            getRoomDetails(roomId);
            orderItemId = getIntent().getIntExtra("editId", 0);
            if (orderItemId != 0)
                editRoomDetails(orderItemId);
        }
        //cart
        Toast.makeText(this, fromRoom+"", Toast.LENGTH_SHORT).show();

        binding.edit.setVisibility(View.GONE);

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookNow();
            }
        });


        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Cart.class);
                startActivity(intent);
            }
        });

        binding.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Payment.class);
                startActivity(intent);
            }
        });


    }

    private void bookNow() {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        String check_in = binding.chikinDate.getText().toString();
        String checkout = binding.chikoutDate.getText().toString();
        String count = binding.count.getText().toString();


        service.bookInfo(check_in, checkout, count, roomId + "", "Bearer " + token).enqueue(new Callback<CartExample>() {
            @Override
            public void onResponse(Call<CartExample> call, retrofit2.Response<CartExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                    Toast.makeText(BookingInfo.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(BookingInfo.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartExample> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(BookingInfo.this, "on failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRoomDetails(int roomId) {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.getRoomDetails(roomId, "Bearer " + token).enqueue(new Callback<RoomsExample>() {
            @Override
            public void onResponse(Call<RoomsExample> call, Response<RoomsExample> response) {
                Log.e("response code", response.code() + "");

                if (response.body() != null && extras == null) {

                    binding.hotelName.setText(response.body().getData().getName());
                    binding.priceHotel.setText(response.body().getData().getPricePerNight() + "");
                    binding.personNum.setText(response.body().getData().getAvailableRooms() + "");
                    binding.roomPrice.setText(response.body().getData().getPricePerNight() + "");
                    binding.totalMoney.setText(response.body().getData().getPricePerNight() + "");
                    if (response.body().getData().getHasOffer() == null) {
                        binding.offer.setText("0");
                    } else {
                        binding.offer.setText(response.body().getData().getHasOffer() + "");
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
                            }
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
                            int Price = Integer.parseInt((binding.priceHotel.getText().toString()));
                            int Count = Integer.parseInt((binding.dayCount.getText().toString()));

                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price;
                                binding.totalMoney.setText(tot + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price;
                                totals = tot * (offer / 100);
                                int x = tot - totals;
                                binding.totalMoney.setText(x + "");

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
                            int Price = Integer.parseInt((binding.priceHotel.getText().toString()));
                            int Count = Integer.parseInt((binding.dayCount.getText().toString()));
                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price;
                                binding.totalMoney.setText(tot + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price;
                                totals = tot * (offer / 100);
                                int x = tot - totals;
                                binding.totalMoney.setText(x + "");

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

                }
            }


            @Override
            public void onFailure(Call<RoomsExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }

    private void editRoomDetails(int orderItemId) {

        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.


        String count = binding.count.getText().toString();

        service.editItem(orderItemId, checkInDate, checkOutDate, count, roomId, "put", "Bearer " + token).enqueue(new Callback<EditExample>() {
            @Override
            public void onResponse(Call<EditExample> call, Response<EditExample> response) {
                Log.e("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Toast.makeText(BookingInfo.this, response.message(), Toast.LENGTH_SHORT).show();

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
                            int Price = Integer.parseInt((binding.priceHotel.getText().toString()));
                            int Count = Integer.parseInt((binding.dayCount.getText().toString()));

                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price;
                                binding.totalMoney.setText(tot + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price;
                                totals = tot * (offer / 100);
                                int x = tot - totals;
                                binding.totalMoney.setText(x + "");

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
                            int Price = Integer.parseInt((binding.priceHotel.getText().toString()));
                            int Count = Integer.parseInt((binding.dayCount.getText().toString()));
                            if (response.body().getData().getHasOffer() == null) {
                                tot = Count * Price;
                                binding.totalMoney.setText(tot + "");

                            } else {
                                int offer = Integer.parseInt(response.body().getData().getHasOffer() + "");
                                tot = Count * Price;
                                totals = tot * (offer / 100);
                                int x = tot - totals;
                                binding.totalMoney.setText(x + "");

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
            }


            @Override
            public void onFailure(Call<EditExample> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    private void getData() {
        CHECKIN = binding.chikinDate.getText().toString();
        CHECKOUT = binding.chikoutDate.getText().toString();
        DAYCOUNT = binding.count.getText().toString();
        PRICE = binding.totalMoney.getText().toString();

        roomId = getIntent().getIntExtra("roomId", 0);
        getRoomDetails(roomId);
        extras = getIntent().getExtras();
        if (extras != null) {
            CHECKIN = extras.getString("CHECKIN");
            CHECKOUT = extras.getString("CHECKOUT");
            DAYCOUNT = extras.getString("DAYCOUNT");
            PRICE = extras.getString("PRICE");

            binding.chikinDate.setText(CHECKIN);
            binding.chikoutDate.setText(CHECKOUT);
            binding.count.setText(DAYCOUNT);
            binding.totalMoney.setText(PRICE);

        }
    }

}