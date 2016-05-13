package com.szbb.pro.ui.fragment.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemNewOrderLayout;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.DialListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentClientActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
//新工单
public class NewOrderFragment extends BaseFgm<BaseBean, MyOrderBean.ListEntity> implements
        BGARefreshLayout
                .BGARefreshLayoutDelegate, DialListener {
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private OrderBaseLayout orderBaseLayout;
    private MyOrderBean myOrderBean;
    private org.solovyev.android.views.llm.LinearLayoutManager linearLayoutManager;

    private int page = 0;
    private int pageSize = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateUIBroadcast broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast, AppKeyMap.APPOINTMENT_CLIENT_ACTION, AppKeyMap
                .APPOINTMENT_CAN_NOT_CONTENT_CLIENT);

    }

    @Override
    protected void initViews() {
        orderBaseLayout = (OrderBaseLayout) viewDataBinding;
        recyclerView = orderBaseLayout.include.recyclerView;
        refreshLayout = orderBaseLayout.include.refreshLayout;
        linearLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        commonBinderAdapter = new CommonBinderAdapter<MyOrderBean.ListEntity>(getActivity(), R
                .layout.item_new_order, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity listEntity) {
                ItemNewOrderLayout layout = (ItemNewOrderLayout) viewDataBinding;
                layout.btnCall.setTag(R.id.tag_cupcake, listEntity.getTel());//保存号码
                layout.btnCall.setTag(R.id.tag_donut, position);//保存位置
                layout.btnCall.setOnClickListener(NewOrderFragment.this);
                layout.setOrder(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        commonBinderAdapter.setBinderOnItemClickListener(this);
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.large_margin_15dp).color(android.R.color.transparent)
                .build());
        //默认刷新样式
        AppTools.defaultRefresh(refreshLayout, this);
        networkModel.myOrderList("1", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {
        LogTools.i("no Net work");
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    private int clickPos = -1;

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_call:
                DialDialog dialDialog = new DialDialog(getActivity(), this);
                String number = (String) view.getTag(R.id.tag_cupcake);//取出电话号码
                clickPos = (int) view.getTag(R.id.tag_donut);//取出在列表中的位置
                dialDialog.call(number);
                break;
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.fgm_order_base;
    }

    @Override
    public void uiUpData(Intent intent) {
        String action = intent.getAction();
        if (action.equals(AppKeyMap.APPOINTMENT_CLIENT_ACTION) || action.equals(AppKeyMap
                .APPOINTMENT_CAN_NOT_CONTENT_CLIENT)) {
            LogTools.w("updata new order fragment");
            //重新执行访问后台刷新数据操作
            networkModel.myOrderList("1", "1", "20", NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        MyOrderBean.ListEntity listEntity = list.get(pos);
        final String orderId = listEntity.getOrderid();
        Intent intent = new Intent().putExtra("orderId", orderId).setClass(getActivity(),
                AppointmentClientActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        page = 1;
        pageSize = 20;
        networkModel.myOrderList("1", page + "", pageSize + "", NetworkParams.DONUT);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        if (myOrderBean.getIsNext() == 1) {
            pageSize += 10;
            networkModel.myOrderList("1", page + "", pageSize + "", NetworkParams.FROYO);
        } else {
            AppTools.showNormalSnackBar(orderBaseLayout.getRoot(), getString(R.string.no_more));
            return false;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        myOrderBean = (MyOrderBean) baseBean;
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
    public void dial() {
        if (clickPos != -1) {
            String orderId = list.get(clickPos).getOrderid();
            startActivity(new Intent(getContext(), AppointmentClientActivity.class).putExtra("orderId", orderId));
        }
    }
}
