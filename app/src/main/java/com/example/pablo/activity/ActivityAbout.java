package com.example.pablo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pablo.R;
import com.example.pablo.databinding.ActivityAboutBinding;
import com.example.pablo.databinding.ActivitySupportBinding;
import com.example.pablo.databinding.FragmentAccountBinding;
import com.example.pablo.fragments.AccountFragment;

public class ActivityAbout extends AppCompatActivity {

    ActivitySupportBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
}