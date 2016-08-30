package com.szbb.pro.ui.fragment.order;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemNewOrderLayout;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.eventbus.NewOrderEvent;
import com.szbb.pro.entity.eventbus.SearchGod;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.DialListener;
import com.szbb.pro.manager.SearcherManager;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentClientActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.reflect.Field;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */

//新工单
public class NewOrderFragment
        extends OrderSearchBaseFragment<BaseBean, MyOrderBean.ListEntity>
        implements BGARefreshLayout.BGARefreshLayoutDelegate, DialListener {

    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private OrderBaseLayout orderBaseLayout;
    private MyOrderBean myOrderBean;
    private org.solovyev.android.views.llm.LinearLayoutManager linearLayoutManager;

    private int page = 0;
    private int pageSize = 100;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateUIBroadcast broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast,
                                   AppKeyMap.REFRESH_ALL,
                                   AppKeyMap.APPOINTMENT_CAN_NOT_CONTENT_CLIENT,
                                   AppKeyMap.GRAB_ACTION);
    }

    @Override
    protected void initViews () {
        orderBaseLayout = (OrderBaseLayout) viewDataBinding;
        recyclerView = orderBaseLayout.include.recyclerView;
        refreshLayout = orderBaseLayout.include.refreshLayout;
        linearLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        commonBinderAdapter = new CommonBinderAdapter<MyOrderBean.ListEntity>(getActivity(),
                                                                              R.layout.item_new_order,
                                                                              list) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity listEntity) {
                ItemNewOrderLayout layout = (ItemNewOrderLayout) viewDataBinding;
                layout.btnCall.setTag(R.id.tag_cupcake,
                                      listEntity.getTel());//保存号码
                layout.btnCall.setTag(R.id.tag_donut,
                                      position);//保存位置
                layout.btnCall.setOnClickListener(NewOrderFragment.this);
                layout.setOrder(listEntity);
            }
        };
    }

    @Override
    protected void initEvents () {
        commonBinderAdapter.setBinderOnItemClickListener(this);
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                                               .sizeResId(R.dimen.large_margin_15dp)
                                               .color(android.R.color.transparent)
                                               .build());
        //默认刷新样式
        AppTools.defaultRefresh(refreshLayout,
                                this);
        requestNetwork();
    }

    @Override
    protected void noNetworkStatus () {

    }

    private int clickPos = -1;

    @Override
    protected void onClick (int id, View view) {
        switch (id) {
            case R.id.btn_call:
                DialDialog dialDialog = new DialDialog(getActivity(),
                                                       this);
                String number = (String) view.getTag(R.id.tag_cupcake);//取出电话号码
                clickPos = (int) view.getTag(R.id.tag_donut);//取出在列表中的位置
                dialDialog.call(number);
                break;
        }
    }

    @Override
    protected int getContentView () {
        return R.layout.fgm_order_base;
    }

    @Override
    public void uiUpData (Intent intent) {
        String action = intent.getAction();
        if (action.equals(AppKeyMap.NO_NETWORK_ACTION)) {
            LogTools.i("no Net work");
            refreshLayout.endLoadingMore();
            refreshLayout.endRefreshing();
            return;
        }
        if (!TextUtils.isEmpty(action)) {
            LogTools.w("updata new order fragment");
            //重新执行访问后台刷新数据操作
            networkModel.myOrderList("1",
                                     "1",
                                     "100",
                                     NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onBinderItemClick (View view, int pos) {
        MyOrderBean.ListEntity listEntity = list.get(pos);
        String unread = listEntity.getUnread();
        final String orderId = listEntity.getOrderid();
        Intent intent = new Intent(getActivity(),
                                   AppointmentClientActivity.class).putExtra(
                "orderId",
                orderId)
                                                                   .putExtra("unread",
                                                                             unread)
                                                                   .putExtra("identifier",
                                                                             listEntity
                                                                                     .getIdentifier());
        startActivity(intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing (BGARefreshLayout bgaRefreshLayout) {
        page = 1;
        pageSize = 100;
        requestNetwork();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore (BGARefreshLayout bgaRefreshLayout) {
        if (myOrderBean.getIsNext() == 1) {
            pageSize += 10;
            networkModel.myOrderList("1",
                                     page + "",
                                     pageSize + "",
                                     NetworkParams.FROYO);
        } else {
            AppTools.showNormalSnackBar(orderBaseLayout.getRoot(),
                                        getString(R.string.no_more));
            return false;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess (BaseBean baseBean, NetworkParams paramsCode) {
        myOrderBean = (MyOrderBean) baseBean;
        switch (paramsCode) {
            case CUPCAKE://the first time load
            case DONUT://the refresh action
            case FROYO://load more action
                this.list.clear();
                break;
        }
        final List<MyOrderBean.ListEntity> list = myOrderBean.getList();
        if (list.isEmpty() && this.list.isEmpty()) {
            orderBaseLayout.include.emptyView2.setVisibility(View.VISIBLE);
        } else {
            orderBaseLayout.include.emptyView2.setVisibility(View.GONE);
        }
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
        //保存列表到本地
        saveListToLocal(NEWORDERKEY);
    }

    @Override
    public void dial () {
        if (clickPos != -1) {
            String orderId = list.get(clickPos)
                                 .getOrderid();
            startActivity(new Intent(getContext(),
                                     AppointmentClientActivity.class).putExtra("orderId",
                                                                               orderId));
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault()
                .unregister(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onDetach () {
        super.onDetach();
        try {
            Field field = Fragment.class.getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this,
                      null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onEvent (NewOrderEvent newOrderEvent) {
        requestNetwork();
    }

    public void onEvent (SearchGod searchGod) {
        if (searchGod.isSelf(this)) {
            SearcherManager.searchItems(this,
                                        getLocalList(NEWORDERKEY),
                                        searchGod.getSearchFields());
        }
    }

    private void requestNetwork () {
        networkModel.myOrderList("1",
                                 "",
                                 "",
                                 NetworkParams.CUPCAKE);
    }

    @Override
    public void searchResult (List<MyOrderBean.ListEntity> list) {
        this.list.clear();
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
    }


}
