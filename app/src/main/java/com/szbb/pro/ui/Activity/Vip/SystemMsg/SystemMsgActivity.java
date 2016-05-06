package com.szbb.pro.ui.activity.vip.SystemMsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Vip.SystemMsgBean;
import com.szbb.pro.eum.NetworkParams;

public class SystemMsgActivity extends BaseAty<SystemMsgBean, SystemMsgBean.ListEntity> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.vip_system_msg);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_system_msg;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.llyt_order_msg://工单消息
                startActivity(new Intent().putExtra("type", "1").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_acce_msg://配件消息
                startActivity(new Intent().putExtra("type", "2").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_trade_msg://交易通知
                startActivity(new Intent().putExtra("type", "3").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_business_msg://业务通告
                startActivity(new Intent().putExtra("type", "1").setClass(this,
                        AccountCementActivity
                                .class));
                break;
            case R.id.llyt_guide://接单必读
                startActivity(new Intent().putExtra("type", "2").setClass(this,
                        AccountCementActivity
                                .class));
                break;
            case R.id.llyt_act_msg://活动消息
                startActivity(new Intent().putExtra("type", "3").setClass(this,
                        AccountCementActivity
                                .class));
                break;
        }
    }


    @Override
    public void onJsonObjectSuccess(SystemMsgBean systemMsgBean, NetworkParams paramsCode) {
        list.addAll(systemMsgBean.getList());
    }
}
