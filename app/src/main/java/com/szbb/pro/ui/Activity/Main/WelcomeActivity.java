package com.szbb.pro.ui.Activity.Main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.test.TestAty;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Login.LoginActivity;
import com.szbb.pro.ui.Activity.Vip.Wallet.AddBankCardActivity;

/**
 * Created by ChanZeeBm on 2015/9/9.
 */
public class WelcomeActivity extends BaseAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!AppKeyMap.IS_DEBUG)
                    start(LoginActivity.class);
                else
                    start(AddBankCardActivity.class);//TestAty MainActivity
                AppTools.removeSingleActivity(WelcomeActivity.this);
            }
        }, 300);
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
    protected void onClick(int id, View view) {

    }


}
