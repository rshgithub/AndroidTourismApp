package com.example.pablo.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.pablo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
public class BottomNavigationBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        if (getIntent().getStringExtra("cart")!=null){
            Toast.makeText(getBaseContext(), "cart", Toast.LENGTH_SHORT).show();
            openFragment(CartFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.Cart);
        }
        else if(getIntent().getStringExtra("order")!=null) {
            openFragment(OrderFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.order);

        }
        else
            openFragment(HomeFragment.newInstance());


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    openFragment(HotelsFragment.newInstance());
                    finish();
                } else if (item.getItemId() == R.id.Cart) {
                    openFragment(CartFragment.newInstance());
                } else if (item.getItemId() == R.id.Order) {
                    openFragment(OrderFragment.newInstance());
                } else {
                    openFragment(AccountFragment.newInstance());
                }
                return true;
            }
        });
    }

    void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.viewpager1, fragment);
        fragmentTransaction.commit();
    }


}