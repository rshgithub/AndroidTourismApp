package com.example.pablo.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pablo.R;
import com.example.pablo.databinding.ActivityTopNavigationBarBinding;
import com.google.android.material.navigation.NavigationBarView;

public class TopNavigationBarActivity extends AppCompatActivity {

    ActivityTopNavigationBarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopNavigationBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Fragment
        binding.tabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Hotels) {
                    openFragment(HotelsFragment.newInstance());

                } else if (item.getItemId() == R.id.Mosque) {
                    openFragment(MosqueFragment.newInstance());
                }else if (item.getItemId() == R.id.Restaurants) {
                } else {
                    openFragment(ChurchesFragment.newInstance());
                }
                return true;
            }
        });
        openFragment(HotelsFragment.newInstance());
    }
    //Fragment
    void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.viewpager, fragment);
        fragmentTransaction.commit();
}}