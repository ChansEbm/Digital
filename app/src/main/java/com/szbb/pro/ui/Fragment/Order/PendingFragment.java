package com.szbb.pro.ui.Fragment.Order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.BR;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.databinding.ItemPendingCommonBinding;
import com.szbb.pro.databinding.ItemPendingSimpleBinding;
import com.szbb.pro.entity.Order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Orders.Operating.OrderDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
//待处理
public class PendingFragment extends BaseFgm<MyOrderBean, MyOrderBean.ListEntity> implements
        BGARefreshLayout
        .BGARefreshLayoutDelegate, UpdateUIListener {
    private RecyclerView recyclerView;
    private OrderBaseLayout orderBaseLayout;
    private BGARefreshLayout refreshLayout;
    private UpdateUIBroadcast broadcast;
    private int page = 1;
    private int pageSize = 20;
    private MyOrderBean myOrderBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        //接收点击预约客户后的广播
        AppTools.registerBroadcast(broadcast, AppKeyMap.APPOINTMENT_CLIENT_ACTION);
    }

    @Override
    protected void initViews() {
        orderBaseLayout = (OrderBaseLayout) viewDataBinding;
        recyclerView = orderBaseLayout.include.recyclerView;
        refreshLayout = orderBaseLayout.include.refreshLayout;
        simpleAdapter();
    }

    @Override
    protected void initEvents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.large_margin_15dp).color(android.R.color.transparent)
                .build());
        AppTools.defaultRefresh(refreshLayout, this);
        networkModel.myOrderList("2", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_pending_operation_order:
                int pos = (int) view.getTag();
                MyOrderBean.ListEntity listEntity = (MyOrderBean.ListEntity) list.get(pos);
                startActivity(new Intent().putExtra("orderId", listEntity.getOrderid()).setClass
                        (getActivity(), OrderDetailActivity.class));
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_order_base;
    }

    protected void simpleAdapter() {
        multiAdapter = new MultiAdapter<MyOrderBean.ListEntity>(getActivity(), list,
                R.layout.item_pending_simple, R.layout.item_pending_common) {

            @Override
            public int getItemViewType(int position) {
                return list.get(position).isCommon() ? SECOND_LAYOUT : FIRST_LAYOUT;
            }

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity pendingBean) {
                if (viewDataBinding instanceof ItemPendingSimpleBinding) {
                    ((ItemPendingSimpleBinding) viewDataBinding).btnPendingOperationOrder
                            .setOnClickListener(PendingFragment.this);
                    ((ItemPendingSimpleBinding) viewDataBinding).btnPendingOperationOrder.setTag
                            (position);
                    viewDataBinding.setVariable(BR.pendingSimple, pendingBean);
                } else if (viewDataBinding instanceof ItemPendingCommonBinding) {
                    ((ItemPendingCommonBinding) viewDataBinding).btnPendingOperationOrder
                            .setOnClickListener(PendingFragment.this);
                    ((ItemPendingCommonBinding) viewDataBinding).btnPendingOperationOrder.setTag
                            (position);
                    viewDataBinding.setVariable(BR.pendingCommon, pendingBean);
                }
                viewDataBinding.executePendingBindings();
            }
        };
        recyclerView.setAdapter(multiAdapter);
        multiAdapter.setBinderOnItemClickListener(this);
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        MyOrderBean.ListEntity listEntity = (MyOrderBean.ListEntity) list.get(pos);
        boolean isCommon = listEntity.isCommon();
        if (pos != -1) {
            listEntity.setIsCommon(!isCommon);
            multiAdapter.notifyItemChanged(pos);
        }
    }

    @Override
    public void uiUpData(Intent intent) {
        super.uiUpData(intent);
        final String action = intent.getAction();
        if (action.equals(AppKeyMap.APPOINTMENT_CLIENT_ACTION) || action.equals(AppKeyMap
                .WAITING_COST_ACTION)) {
            //重新访问后台执行刷新操作
            networkModel.myOrderList("2", "", "", NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppTools.unregisterBroadcast(broadcast);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        page = 1;
        pageSize = 20;
        networkModel.myOrderList("2", page + "", pageSize + "", NetworkParams.DONUT);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        if (myOrderBean.getIsNext() == 1) {
            page++;
            pageSize += 10;
            networkModel.myOrderList("2", page + "", pageSize + "", NetworkParams.FROYO);
        } else {
            AppTools.showNormalSnackBar(orderBaseLayout.getRoot(), getString(R.string.no_more)
            );
            return false;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess(MyOrderBean myOrderBean, NetworkParams paramsCode) {
        this.myOrderBean = myOrderBean;
        switch (paramsCode) {
            case CUPCAKE://the first time load
            case DONUT://the refresh action
                this.list.clear();
                break;
            case FROYO://load more action
                break;
        }
        this.list.addAll(this.myOrderBean.getList());
        multiAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }
}
