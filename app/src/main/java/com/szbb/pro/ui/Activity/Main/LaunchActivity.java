package com.szbb.pro.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.BuildConfig;
import com.szbb.pro.R;
import com.szbb.pro.adapters.LaunchAdapter;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.LaunchLayout;
import com.szbb.pro.test.TestAty;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.login.LoginActivity;
import com.szbb.pro.widget.Indicator;

public class LaunchActivity extends BaseAty {
    private LaunchLayout launchLayout;
    private ViewPager viewPager;
    private Indicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);//全屏
        super.onCreate(savedInstanceState);
        launchLayout = (LaunchLayout) viewDataBinding;

    }

    @Override
    protected void initViews() {
        viewPager = launchLayout.viewPager;
        indicator = launchLayout.indicators;
    }

    @Override
    protected void initEvents() {
        LaunchAdapter adapter = new LaunchAdapter();
        adapter.setOnClickListener(this);
        viewPager.setAdapter(adapter);

        indicator.setColorNor(R.color.color_bg_gravy);
        indicator.setColorSelect(R.color.theme_primary);
        indicator.setWithViewPager(viewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onClick(int id, View view) {
        if (id == R.id.imageView) {
            if (viewPager.getCurrentItem() == 3) {
                Prefser prefser = new Prefser(AppTools.getSharePreferences());
                prefser.put("isFirstRunApplication", false);
                startMainActivity();
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (BuildConfig.isDebug)
            intent.setClass(this, TestAty.class);
        else
            intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
