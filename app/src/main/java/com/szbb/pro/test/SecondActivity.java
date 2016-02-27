package com.szbb.pro.test;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class SecondActivity extends BaseAty implements BDLocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    protected void initEvents() {
        AppTools.locate(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_second;
    }

    @Override
    protected void onClick(int id, View view) {
        finish();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //定位成功后,执行预约签到
        if (bdLocation != null) {
            networkModel.signAppoint("193", 24d, 25d
                    , NetworkParams.DONUT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
