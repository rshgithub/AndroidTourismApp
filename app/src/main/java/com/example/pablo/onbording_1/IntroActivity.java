package com.example.pablo.onbording_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.pablo.R;
import com.example.pablo.activity.Login;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePreData()){
            Intent mainActivity = new Intent(getApplicationContext(), Login.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);

        btnNext = findViewById(R.id.skip);
        tabIndicator = findViewById(R.id.tab_indicator);

        //Data
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Easy to Book", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.raimond_klavins_qdroli4aifm_unsplash));
        mList.add(new ScreenItem("Easy to Book", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.raimond));
        mList.add(new ScreenItem("Easy to Book", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.", R.drawable.raimond3));

        //Setup viewPager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        screenPager.setPageTransformer(false, new OnboardingPageTransformer());

        //Setup tab indicator
        tabIndicator.setupWithViewPager(screenPager);

        //Button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenPager.setCurrentItem(screenPager.getCurrentItem()+1, true);
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==mList.size()-1){
//                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Button Get Started
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), Login.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePreData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }


}
