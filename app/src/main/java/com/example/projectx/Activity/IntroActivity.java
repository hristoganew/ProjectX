package com.example.projectx.Activity;

import com.example.projectx.Adapter.IntroAdapter;
import com.example.projectx.R;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends ProjectxActivity {

    private Button getStartedButton;
    private Animation buttonAnimation;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private IntroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (preferences.loadIntro() == false) {
            updateUIAndFinish(LoginActivity.class);
        }

        setContentView(R.layout.activity_intro);

        mSlideViewPager = findViewById(R.id.intro_view_pager);
        mDotLayout = findViewById(R.id.intro_dot_layout);
        getStartedButton = findViewById(R.id.get_started_button);
        buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        adapter = new IntroAdapter(this);
        mSlideViewPager.setAdapter(adapter);

        addDotIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
//
        }

        @Override
        public void onPageSelected(int i) {
            addDotIndicator(i);

            if (i == 2) {
                getStartedButton.setVisibility(View.VISIBLE);
            }else{
                getStartedButton.setVisibility(View.INVISIBLE);
                getStartedButton.setAnimation(buttonAnimation);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void addDotIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.textColorLight));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.transparentWhite));
        }
    }

    public void getStarted(View view) {
        updateUIAndFinish(LoginActivity.class);
        preferences.setIntroState(false);
    }
}
