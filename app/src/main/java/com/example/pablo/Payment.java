package com.example.pablo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pablo.activity.Login;
import com.example.pablo.databinding.ActivityCartBinding;
import com.example.pablo.databinding.ActivityPaymentBinding;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.buyorder.BuyOrderExample;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class Payment extends AppCompatActivity {
    static Service service;
    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.tvCardName == null || binding.tvExpiryDate == null
                        || binding.tvCardNumber == null || binding.tvCvv == null){

                    String number = binding.tvCardNumber.getText().toString();
                    String Date = binding.tvExpiryDate.getText().toString();
                    String Name = binding.tvCardName.getText().toString();
                    String Cvv = binding.tvCvv.getText().toString();

                    int CardNumber = number.length();
                    int ExpiryDate = Date.length();
                    int CardName = Name.length();
                    int tvCvv = Cvv.length();

                    if (CardNumber != 16){
                        Toast.makeText(Payment.this, "The card number must contain 16 number", Toast.LENGTH_LONG).show();
                    }
                     if (ExpiryDate != 5){
                        Toast.makeText(Payment.this, "The expiration date should be written as 3/13", Toast.LENGTH_LONG).show();
                    }
                     if (CardName > 30){
                        Toast.makeText(Payment.this, "Card Name must be less than 30 char", Toast.LENGTH_LONG).show();
                    }
                     if (tvCvv != 3){
                        Toast.makeText(Payment.this, "Cvv Name must contain 3 number", Toast.LENGTH_LONG).show();
                    }

                }else{
                    buyOrder();
                }
            }
        });
    }
    public void buyOrder(){
        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.BuyOrder("Bearer success  " + token).enqueue(new Callback<BuyOrderExample>() {
            @Override
            public void onResponse(Call<BuyOrderExample> call, retrofit2.Response<BuyOrderExample> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(Payment.this, response.message() , Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(Payment.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BuyOrderExample> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Payment.this, "on failure" , Toast.LENGTH_SHORT).show();
            }
        });
    }

}