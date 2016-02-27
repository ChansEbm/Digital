package com.szbb.pro.ui.Activity.Vip.SystemMsg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.SystemMsgLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Vip.SystemMsgBean;
import com.szbb.pro.eum.NetworkParams;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class SystemMsgActivity extends BaseAty<SystemMsgBean, SystemMsgBean.ListEntity>
        implements BGARefreshLayout
        .BGARefreshLayoutDelegate {
    private SystemMsgLayout systemMsgLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemMsgLayout = (SystemMsgLayout) viewDataBinding;
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
            case R.id.llyt_order_msg:
                startActivity(new Intent().putExtra("type", "1").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_acce_msg:
                startActivity(new Intent().putExtra("type", "2").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_trade_msg:
                startActivity(new Intent().putExtra("type", "3").setClass(this, OrderHintActivity
                        .class));
                break;
            case R.id.llyt_business_msg:
                startActivity(new Intent().putExtra("type", "1").setClass(this, AccountCementActivity
                        .class));
                break;
            case R.id.llyt_guide:
                startActivity(new Intent().putExtra("type", "2").setClass(this, AccountCementActivity
                        .class));
                break;
            case R.id.llyt_act_msg:
                startActivity(new Intent().putExtra("type", "3").setClass(this, AccountCementActivity
                        .class));
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return true;
    }

    @Override
    public void onJsonObjectSuccess(SystemMsgBean systemMsgBean, NetworkParams paramsCode) {
        list.addAll(systemMsgBean.getList());
    }
}
