package com.example.pablo.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

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

        EventBus.getDefault().register(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    openFragment(HomeFragment.newInstance());
                    //  finish();
                } else if (item.getItemId() == R.id.Favorites) {
                    openFragment(CartFragment.newInstance());
                } else if (item.getItemId() == R.id.Order) {
                    openFragment(OrderFragment.newInstance());
                } else {
                    openFragment(AccountFragment.newInstance());
                }
                return true;
            }
        });
        openFragment(HomeFragment.newInstance());
    }

    void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.viewpager1, fragment);
        fragmentTransaction.commit();
    }
    @Subscribe
     public void onEvent(String event) {
        if (event.equals("cart")) {
            openFragment(CartFragment.newInstance());
        }
    }
}