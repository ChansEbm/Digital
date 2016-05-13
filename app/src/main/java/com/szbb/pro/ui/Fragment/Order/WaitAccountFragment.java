package com.szbb.pro.ui.fragment.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemWaitingAccountLayout;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
//待结算
public class WaitAccountFragment extends BaseFgm<MyOrderBean, MyOrderBean.ListEntity> implements
        BGARefreshLayout
                .BGARefreshLayoutDelegate {
    private OrderBaseLayout orderBaseLayout;
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private int page = 1;
    private int pageSize = 20;
    private MyOrderBean myOrderBean;
    private UpdateUIBroadcast updateUIBroadcast = new UpdateUIBroadcast();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUIBroadcast.setListener(this);
        AppTools.registerBroadcast(updateUIBroadcast, AppKeyMap.WAITING_COST_ACTION);
    }

    @Override
    protected void initViews() {
        orderBaseLayout = (OrderBaseLayout) viewDataBinding;
        recyclerView = orderBaseLayout.include.recyclerView;
        refreshLayout = orderBaseLayout.include.refreshLayout;
        commonBinderAdapter = new CommonBinderAdapter<MyOrderBean.ListEntity>(getActivity(), R
                .layout.item_waiting_account, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity listEntity) {
                ItemWaitingAccountLayout waitingAccountLayout = (ItemWaitingAccountLayout)
                        viewDataBinding;
                waitingAccountLayout.setOrder(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.large_margin_15dp).color(android.R.color.transparent)
                .build());
        commonBinderAdapter.setBinderOnItemClickListener(this);
        AppTools.defaultRefresh(refreshLayout, this);
        networkModel.myOrderList("3", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_order_base;
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        final String orderid = list.get(pos).getOrderid();
        startActivity(new Intent().setClass(getActivity(), OrderDetailActivity.class).putExtra
                ("orderId", orderid));
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        page = 1;
        pageSize = 20;
        networkModel.myOrderList("3", page + "", pageSize + "", NetworkParams.DONUT);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        if (myOrderBean.getIsNext() == 1) {
            page++;
            pageSize += 10;
            networkModel.myOrderList("3", page + "", pageSize + "", NetworkParams.FROYO);
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
            case FROYO://load more action
                this.list.clear();
                break;
        }
        final List<MyOrderBean.ListEntity> list = myOrderBean.getList();
        if (list.isEmpty() && this.list.isEmpty())
            orderBaseLayout.include.emptyView2.setVisibility(View.VISIBLE);
        else
            orderBaseLayout.include.emptyView2.setVisibility(View.GONE);
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    public void uiUpData(Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppKeyMap.WAITING_COST_ACTION)) {
            networkModel.myOrderList("3", "", "", NetworkParams.CUPCAKE);
        }
    }
}
