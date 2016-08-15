package com.szbb.pro.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.test.TestAty;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.login.LoginActivity;

/**
 * Created by ChanZeeBm on 2015/9/9.
 */
public class WelcomeActivity
        extends BaseAty
        implements Runnable {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        boolean isFirstRunApplication = new Prefser(AppTools.getSharePreferences()).get
                ("isFirstRunApplication",
                 Boolean.class,
                 true);
        if (isFirstRunApplication) {
//            start(TestAty.class);
            start(LaunchActivity.class);
            return;
        }
        handler.postDelayed(this,
                            5000);
    }

    @Override
    protected void initEvents() {
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_welcome;
    }

    @Override
    protected void onClick(int id,
                           View view) {
        if (id == R.id.imageView) {
            if (AppKeyMap.IS_DEBUG) {
                start(TestAty.class);
            } else {
                skipToLoginActivity();
            }
            handler.removeCallbacks(this);
        }
    }

    @Override
    public void onJsonObjectResponse(Object o,
                                     NetworkParams paramsCode) {

    }

    @Override
    public void run() {
        if (AppKeyMap.IS_DEBUG) {
            start(TestAty.class);
        } else {
            skipToLoginActivity();
        }
    }

    private void skipToLoginActivity() {
        Intent it = new Intent(WelcomeActivity.this,
                               LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                                     Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }


}
