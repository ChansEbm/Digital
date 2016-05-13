package com.szbb.pro.ui.activity.vip.system_msg;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemOrderHintLayout;
import com.szbb.pro.OrderHintLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.vip.OrderHintBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentClientActivity;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.szbb.pro.ui.activity.vip.wallet.TransactionDetailActivity;

import java.util.List;

public class OrderHintActivity extends BaseAty<OrderHintBean, OrderHintBean.ListEntity> {
    private OrderHintLayout hintLayout;
    private RecyclerView recyclerView;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hintLayout = (OrderHintLayout) viewDataBinding;
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this);
        switchTitle();
        recyclerView = hintLayout.include.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<OrderHintBean.ListEntity>(this, R.layout
                .item_order_hint, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderHintBean.ListEntity listEntity) {
                ((ItemOrderHintLayout) viewDataBinding).setHint(listEntity);
            }
        };
    }

    private void switchTitle() {
        switch (type) {
            case "1":
                titleBarTools.setTitle(R.string.label_sys_order_msg);
                break;
            case "2":
                titleBarTools.setTitle(R.string.label_sys_acce_msg);
                break;
            case "3":
                titleBarTools.setTitle(R.string.label_sys_trade_msg);
                break;
        }
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        commonBinderAdapter.setBinderOnItemClickListener(this);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        networkModel.orderHintList(type, NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_hint;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(OrderHintBean orderHintBean, NetworkParams paramsCode) {
        final List<OrderHintBean.ListEntity> list = orderHintBean.getList();
        if (list.isEmpty())
            hintLayout.include.emptyView.setVisibility(View.VISIBLE);
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        OrderHintBean.ListEntity listEntity = list.get(pos);
        final String orderId = listEntity.getOrderid();
        String isOrder = listEntity.getIs_order();
        switch (isOrder) {
            case "0":
                startActivity(new Intent(this, TransactionDetailActivity.class).putExtra("index", 1));
                break;
            case "1":
                startActivity(new Intent(this, OrderDetailActivity.class).putExtra("orderId", orderId));
                break;
            case "2":
                startActivity(new Intent(this, AppointmentClientActivity.class).putExtra("orderId", orderId));
                break;
        }

    }
}
