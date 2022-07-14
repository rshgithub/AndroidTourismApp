package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pablo.R;
import com.example.pablo.databinding.ActivityPaymentBinding;
import com.example.pablo.fragments.BottomNavigationBarActivity;
import com.example.pablo.interfaces.Service;
import com.example.pablo.model.buy_one_order.BuyOneOrderExample;
import com.example.pablo.model.buyorder.BuyOrderExample;
import com.github.ybq.android.spinkit.sprite.CircleSprite;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;
import static com.example.pablo.activity.Signup.TokenKey;

public class Payment extends AppCompatActivity {
    static Service service;
    ActivityPaymentBinding binding;
    KProgressHUD hud;

    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        price = getIntent().getDoubleExtra("price", 0);
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

                if(binding.tvCardName.getText().toString().isEmpty() || binding.tvExpiryMonth.getText().toString().isEmpty()
                        || binding.tvCardNumber.getText().toString().isEmpty()|| binding.tvCvv.getText().toString().isEmpty()){

                    String number = binding.tvCardNumber.getText().toString();
                    String Date = binding.tvExpiryMonth.getText().toString();
                    String Name = binding.tvCardName.getText().toString();
                    String Cvv = binding.tvCvv.getText().toString();

                    int CardNumber = number.length();
                    int ExpiryDate = Date.length();
                    int CardName = Name.length();
                    int tvCvv = Cvv.length();

                    if (CardNumber != 16){
                        Toasty.error(Payment.this, "The card number must contain 16 number", Toast.LENGTH_LONG).show();
                    }
                     if (ExpiryDate != 5){
                         Toasty.error(Payment.this, "The expiration date should be written as 3/13", Toast.LENGTH_LONG).show();
                    }
                     if (CardName > 30){
                         Toasty.error(Payment.this, "Card Name must be less than 30 char", Toast.LENGTH_LONG).show();
                    }
                     if (tvCvv != 3){
                         Toasty.error(Payment.this, "Cvv Name must contain 3 number", Toast.LENGTH_LONG).show();
                    }



                }else{

                    payment();
                }
            }
        });
    }


    public void payment() {
        Login.SP = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(TokenKey, "");


        Long number = Long.valueOf(binding.tvCardNumber.getText().toString());
        Long ExpiryMonth = Long.valueOf(binding.tvExpiryMonth.getText().toString());
        Long ExpiryYear = Long.valueOf(binding.tvExpiryYear.getText().toString());
        String Name = binding.tvCardName.getText().toString();
        Long Cvv = Long.valueOf(binding.tvCvv.getText().toString());

        Log.e("ll","  ExpiryMonth  "+ExpiryMonth+"  ExpiryYear  "
        +ExpiryYear+"number"+number+"name"+Name+"cvv"+Cvv);

        hud = KProgressHUD.create(Payment.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        hud.setProgress(90);


        service.Payment(number, Name, ExpiryMonth, ExpiryYear,Cvv, token).enqueue(new Callback<com.example.pablo.model.payment.Payment>() {
            @Override
            public void onResponse(Call<com.example.pablo.model.payment.Payment> call, retrofit2.Response<com.example.pablo.model.payment.Payment> response) {

                Log.e("code",response.code()+"");
                if (response.isSuccessful()) {
                    hud.dismiss();
                    Toasty.success(Payment.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //open order fragment
                    Intent intent=new Intent(getBaseContext(),BottomNavigationBarActivity.class);
                    intent.putExtra("order","order");
                    startActivity(intent);
                    finish();

                } else {
                    hud.dismiss();

                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<com.example.pablo.model.payment.Payment> call, Throwable t) {
                t.printStackTrace();
                hud.dismiss();

//                Toasty.error(Payment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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