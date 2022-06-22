package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pablo.R;
import com.example.pablo.databinding.ActivityPaymentBinding;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.buy_one_order.BuyOneOrderExample;
import com.example.pablo.model.buyorder.BuyOrderExample;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class Payment extends AppCompatActivity {
    static Service service;
    ActivityPaymentBinding binding;
Long price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        price = getIntent().getLongExtra("price", 0);
        binding.totalPrice.setText(price+"");

        if (!isOnLine()){
            Dialog dialog = new Dialog(getBaseContext(), R.style.NoInternet);
            dialog.setContentView(R.layout.no_internet);
            dialog.show();

            Button retry;
            retry = dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isOnLine()){
                        dialog.dismiss();
                    }

                }
            });

        }
        getRetrofitInstance();
        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.tvCardName == null || binding.tvExpiryMonth == null
                        || binding.tvCardNumber == null || binding.tvCvv == null){

                    String number = binding.tvCardNumber.getText().toString();
                    String Date = binding.tvExpiryMonth.getText().toString();
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
                    payment();
                }
            }
        });
    }


    public void payment() {
        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        Long number = Long.valueOf(binding.tvCardNumber.getText().toString());
        Long ExpiryMonth = Long.valueOf(binding.tvExpiryMonth.getText().toString());
        Long ExpiryYear = Long.valueOf(binding.tvExpiryYear.getText().toString());
        String Name = binding.tvCardName.getText().toString();
        Long Cvv = Long.valueOf(binding.tvCvv.getText().toString());

        Log.e("ll","  ExpiryMonth  "+ExpiryMonth+"  ExpiryYear  "
        +ExpiryYear+"number"+number+"name"+Name+"cvv"+Cvv);

        service.Payment(number, Name, ExpiryMonth, ExpiryYear,Cvv, token).enqueue(new Callback<com.example.pablo.model.payment.Payment>() {
            @Override
            public void onResponse(Call<com.example.pablo.model.payment.Payment> call, retrofit2.Response<com.example.pablo.model.payment.Payment> response) {

                Log.e("code",response.code()+"");
                if (response.isSuccessful()) {
                    Toast.makeText(Payment.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
             //       Toast.makeText(getBaseContext(), response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<com.example.pablo.model.payment.Payment> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(Payment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        service= Service.ApiClient.getRetrofitInstance();

    }


}