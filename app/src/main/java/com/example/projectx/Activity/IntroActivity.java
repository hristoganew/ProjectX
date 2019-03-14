package com.example.projectx.Activity;

import com.example.projectx.Adapter.IntroAdapter;
import com.example.projectx.R;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private IntroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mSlideViewPager = (ViewPager) findViewById(R.id.intro_view_pager);
//        mDotLayout = (LinearLayout) findViewById(R.id.intro_dot_layout);

        adapter = new IntroAdapter(this);
        mSlideViewPager.setAdapter(adapter);

    }
}
