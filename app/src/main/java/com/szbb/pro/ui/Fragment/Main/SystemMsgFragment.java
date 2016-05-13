package com.szbb.pro.ui.fragment.main;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.SystemMsgLayout;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.base.Events;
import com.szbb.pro.ui.activity.vip.system_msg.AccountCementActivity;
import com.szbb.pro.ui.activity.vip.system_msg.OrderHintActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemMsgFragment extends BaseFgm {

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        SystemMsgLayout layout = (SystemMsgLayout) viewDataBinding;
        Events events = new Events();
        events.setOnClickListener(this);
        layout.setEvents(events);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_system_msg;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.llyt_order_msg://工单消息
                startActivity(new Intent().putExtra("type", "1").setClass(getActivity(), OrderHintActivity
                        .class));
                break;
            case R.id.llyt_acce_msg://配件消息
                startActivity(new Intent().putExtra("type", "2").setClass(getActivity(), OrderHintActivity
                        .class));
                break;
            case R.id.llyt_trade_msg://交易通知
                startActivity(new Intent().putExtra("type", "3").setClass(getActivity(), OrderHintActivity
                        .class));
                break;
            case R.id.llyt_business_msg://业务通告
                startActivity(new Intent().putExtra("type", "1").setClass(getActivity(),
                        AccountCementActivity
                                .class));
                break;
            case R.id.llyt_guide://接单必读
                startActivity(new Intent().putExtra("type", "2").setClass(getActivity(),
                        AccountCementActivity
                                .class));
                break;
            case R.id.llyt_act_msg://活动消息
                startActivity(new Intent().putExtra("type", "3").setClass(getActivity(),
                        AccountCementActivity
                                .class));
                break;
        }
    }

}
