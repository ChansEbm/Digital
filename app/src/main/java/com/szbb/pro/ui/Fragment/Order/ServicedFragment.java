package com.szbb.pro.ui.Fragment.Order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemServicedLayout;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.entity.Order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Orders.Operating.OrderDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
//待处理
public class ServicedFragment extends BaseFgm<MyOrderBean, MyOrderBean.ListEntity> implements
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

        commonBinderAdapter = new CommonBinderAdapter<MyOrderBean.ListEntity>(getActivity(), R
                .layout.item_serviced, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity listEntity) {
                ItemServicedLayout layout = (ItemServicedLayout) viewDataBinding;
                layout.setOrder(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager
                (getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.large_margin_15dp).color(android.R.color.transparent)
                .build());
        AppTools.defaultRefresh(refreshLayout, this);
        networkModel.myOrderList("2", "", "", NetworkParams.CUPCAKE);
        commonBinderAdapter.setBinderOnItemClickListener(this);
    }

    @Override
    protected void noNetworkStatus() {
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
//            case R.id.btn_pending_operation_order:
//                int pos = (int) view.getTag();
//                MyOrderBean.ListEntity listEntity = list.get(pos);
//                startActivity(new Intent().putExtra("orderId", listEntity.getOrderid()).setClass
//                        (getActivity(), OrderDetailActivity.class));
//                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_order_base;
    }


    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        MyOrderBean.ListEntity listEntity = list.get(pos);

        final String orderId = listEntity.getOrderid();
        startActivity(new Intent().putExtra("orderId", orderId).setClass(getActivity(),
                OrderDetailActivity.class));
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
        final List<MyOrderBean.ListEntity> list = myOrderBean.getList();
        if (list.isEmpty() && this.list.isEmpty())
            orderBaseLayout.include.emptyView.setVisibility(View.VISIBLE);
        else
            orderBaseLayout.include.emptyView.setVisibility(View.GONE);
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }
}