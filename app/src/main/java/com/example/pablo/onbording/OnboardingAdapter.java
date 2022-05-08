package com.example.pablo.onbording;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pablo.R;


public class OnboardingAdapter extends PagerAdapter {

    private Context context;
    private int[] layouts = {

            R.layout.onboarding_1,
            R.layout.onboarding_2,
            R.layout.onboarding_3,
    };

    OnboardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@androidx.annotation.NonNull android.view.View view,
                                    @androidx.annotation.NonNull Object object) {
        return view == object;
    }

    @androidx.annotation.NonNull
    @Override
    public Object instantiateItem(@androidx.annotation.NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        android.view.View view = layoutInflater.inflate(layouts[position], container, false);
        view.setTag(position);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@androidx.annotation.NonNull ViewGroup container, int position,
                            @androidx.annotation.NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
