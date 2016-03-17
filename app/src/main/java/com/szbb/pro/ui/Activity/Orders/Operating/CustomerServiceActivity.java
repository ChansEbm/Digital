package com.szbb.pro.ui.Activity.Orders.Operating;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.CustomerServiceLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.tools.AppTools;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 联系客服
 */
public class CustomerServiceActivity extends BaseAty implements BGARefreshLayout
        .BGARefreshLayoutDelegate {

    private CustomerServiceLayout customerServiceLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerServiceLayout = (CustomerServiceLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        recyclerView = customerServiceLayout.include.recyclerView;
    }

    @Override
    protected void initEvents() {
        AppTools.defaultRefresh(customerServiceLayout.include.refreshLayout, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }
}
