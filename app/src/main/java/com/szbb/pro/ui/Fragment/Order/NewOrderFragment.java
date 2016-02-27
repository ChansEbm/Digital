package com.szbb.pro.ui.Fragment.Order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemOrderCommonLayout;
import com.szbb.pro.ItemOrderSimpleLayout;
import com.szbb.pro.OrderBaseLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.adapters.MultiAdapter;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.Order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.DialListener;
import com.szbb.pro.model.OrderModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Activity.Orders.Appointment.AppointmentClientActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
//新工单
public class NewOrderFragment extends BaseFgm implements BGARefreshLayout
        .BGARefreshLayoutDelegate,
        DialListener {
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;
    private UpdateUIBroadcast broadcast;
    private OrderBaseLayout orderBaseLayout;
    private MyOrderBean myOrderBean;
    private OrderModel orderModel;
    private DialDialog dialDialog;

    private int page = 0;
    private int pageSize = 20;

    private int position = -1;//记录拨打电话的item的位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast, AppKeyMap.APPOINTMENT_CLIENT_ACTION);
    }

    @Override
    protected void initViews() {
        orderBaseLayout = (OrderBaseLayout) viewDataBinding;
        recyclerView = orderBaseLayout.include.recyclerView;
        LogTools.e(recyclerView.getId());
        refreshLayout = orderBaseLayout.include.refreshLayout;
        orderModel = new OrderModel((AppCompatActivity) getActivity());
        simpleAdapter();
    }

    @Override
    protected void initEvents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    @Override
    protected void onClick(int id, View view) {

        switch (id) {
            case R.id.btn_new_order_contact_client:
                dialDialog = new DialDialog(getActivity(), this);

                position = (int) view.getTag();
                dialDialog.show(((MyOrderBean.ListEntity) list
                        .get(position)).getTel());
                break;
        }
    }

    protected void simpleAdapter() {
        multiAdapter = new MultiAdapter<MyOrderBean.ListEntity>(getActivity(), list,
                R.layout.item_new_order_simple, R.layout.item_new_order_common) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, MyOrderBean.ListEntity listEntity) {
                if (viewDataBinding instanceof ItemOrderSimpleLayout) {
                    ItemOrderSimpleLayout itemOrderSimpleLayout = ((ItemOrderSimpleLayout)
                            viewDataBinding);
                    itemOrderSimpleLayout.btnNewOrderContactClient.setTag(position);
                    itemOrderSimpleLayout.btnNewOrderContactClient.setOnClickListener
                            (NewOrderFragment.this);
                    ((ItemOrderSimpleLayout) viewDataBinding).setOrderSimple(listEntity);
                } else if (viewDataBinding instanceof ItemOrderCommonLayout) {
                    ItemOrderCommonLayout commonLayout = (ItemOrderCommonLayout) viewDataBinding;
                    LinearLayout linearLayout = commonLayout.llytLabel;
                    String label = listEntity.getDetail_list().get(0).getFault_lable();
                    orderModel.addLabel(linearLayout, label);
                    commonLayout.btnNewOrderContactClient.setTag(position);
                    commonLayout.btnNewOrderContactClient.setOnClickListener(NewOrderFragment.this);
                    ((ItemOrderCommonLayout) viewDataBinding).setOrderCommon(listEntity);
                }
            }

            @Override
            public int getItemViewType(int position) {
                return list.get(position).isCommon() ? SECOND_LAYOUT : FIRST_LAYOUT;
            }
        };
        recyclerView.setAdapter(multiAdapter);
        multiAdapter.setBinderOnItemClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_order_base;
    }

    @Override
    public void uiUpData(Intent intent) {
        super.uiUpData(intent);
        if (intent.getAction().equals(AppKeyMap.APPOINTMENT_CLIENT_ACTION)) {
            //重新执行访问后台刷新数据操作
            networkModel.myOrderList("1", "1", "20", NetworkParams.CUPCAKE);
        }
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        final ViewParent parent = view.getParent();
        LogTools.w(parent);
        MyOrderBean.ListEntity listEntity = (MyOrderBean.ListEntity) list.get(pos);
        boolean isCommon = listEntity.isCommon();
        if (pos != -1) {
            listEntity.setIsCommon(!isCommon);
            multiAdapter.notifyItemChanged(pos);
        }
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
            page++;
            pageSize += 10;
            networkModel.myOrderList("1", page + "", pageSize + "", NetworkParams.FROYO);
        } else {
            AppTools.showNormalSnackBar(orderBaseLayout.getRoot(), getString(R.string.no_more));
            return false;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess(Object o, NetworkParams paramsCode) {
        myOrderBean = (MyOrderBean) o;
        switch (paramsCode) {
            case CUPCAKE://the first time load
            case DONUT://the refresh action
                this.list.clear();
                break;
            case FROYO://load more action
                break;
        }
        this.list.addAll(myOrderBean.getList());
        multiAdapter.notifyDataSetChanged();
        refreshLayout.endLoadingMore();
        refreshLayout.endRefreshing();
    }

    @Override
    public void dial() {
        Intent intent = new Intent().putExtra("orderid", ((MyOrderBean.ListEntity) list
                .get(position)).getOrderid()).setClass(getActivity(),
                AppointmentClientActivity.class);
        startActivity(intent);
    }
}
