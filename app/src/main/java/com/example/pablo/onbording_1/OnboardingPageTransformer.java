package com.example.pablo.onbording_1;

import androidx.viewpager.widget.ViewPager;

import com.example.pablo.R;


public class OnboardingPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(android.view.View page, float position) {


        int pageWidth = page.getWidth();
        float pageWidthTimesPosition = pageWidth * position;
        float absPosition = Math.abs(position);

        if (position <= -1.0f || position >= 1.0f) {


        } else if (position == 0.0f) {

        } else {

            android.view.View title = page.findViewById(R.id.textView2);
            title.setAlpha(1.0f - absPosition);

            android.view.View description = page.findViewById(R.id.view);
            description.setTranslationY(-pageWidthTimesPosition / 2f);
            description.setAlpha(1.0f - absPosition);


            android.view.View computer = page.findViewById(R.id.textView3);


            if (computer != null) {
                computer.setAlpha(1.0f - absPosition);
                computer.setTranslationX(-pageWidthTimesPosition * 1.5f);
            }

        }
    }

}
