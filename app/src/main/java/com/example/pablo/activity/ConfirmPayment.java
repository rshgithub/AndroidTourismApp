package com.example.pablo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.example.pablo.databinding.ActivityConfirmPaymentBinding;

import java.util.Calendar;
import java.util.Locale;

public class  ConfirmPayment extends AppCompatActivity {

    int sum = 0;
    String roomName, roomPrice, pr_num, images, tot;
    private int Item_Id = -1;
    SimpleDateFormat sdf;
    ActivityConfirmPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//-------------------------------- Open calender to choose date when click on edittext -------------------------------------
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar1 = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth + sum);

                String myFormat = "MMM dd, ''yyyy"; //In which you need put here

                sdf = new SimpleDateFormat(myFormat, Locale.US);

                binding.chikinDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        binding.chikinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ConfirmPayment.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        binding.chikinDate.setVisibility(View.INVISIBLE);




//----------------****-----------------------------------------------------

    }

    public int totalprice() {
        int Price = Integer.parseInt((binding.priceHotel.getText().toString()));
        int Count = Integer.parseInt((binding.dayCount.getText().toString()));
        tot = String.valueOf(Count * Price);
        return Integer.parseInt((tot));
    }


}