package com.szbb.pro.ui.activity.orders.operating;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.OrderTrackingLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.ItemTrackLayout;
import com.szbb.pro.entity.order.OrderTrackingBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.FancyIndicator;

import org.solovyev.android.views.llm.LinearLayoutManager;

/**
 * 工单跟踪
 */
public class OrderTrackingActivity extends BaseAty<OrderTrackingBean, OrderTrackingBean
        .DataEntity.ProcessListEntity> {
    private OrderTrackingLayout trackingLayout;
    private RecyclerView recyclerView;
    private String orderId;
    private FancyIndicator fancyIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackingLayout = (OrderTrackingLayout) viewDataBinding;
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_order_tracking);
        recyclerView = trackingLayout.recyclerView;
        fancyIndicator = trackingLayout.fancyIndicator;

        commonBinderAdapter = new CommonBinderAdapter<OrderTrackingBean.DataEntity
                .ProcessListEntity>(this, R.layout.item_track, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderTrackingBean.DataEntity.ProcessListEntity processListEntity) {
                processListEntity.setPos(position + "");
                ItemTrackLayout layout = (ItemTrackLayout) viewDataBinding;
                layout.setTrack(processListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fancyIndicator.attachRecyclerView(recyclerView);
        fancyIndicator.setProgress(0);
        networkModel.orderTrace(orderId, NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_tracking;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(OrderTrackingBean orderTrackingBean, NetworkParams paramsCode) {
        final OrderTrackingBean.DataEntity data = orderTrackingBean.getData();
        trackingLayout.setTrack(data);
        this.list.clear();
        this.list.addAll(data.getProcess_list());
        commonBinderAdapter.notifyDataSetChanged();
    }
}
