package com.szbb.pro.ui.activity.vip.wallet;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemPerformanceBondLayout;
import com.szbb.pro.PerformanceBondLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.vip.PerformanceBondBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by Administrator on 2016/8/26.
 */
public class PerformanceBondActivity extends BaseAty<PerformanceBondBean, PerformanceBondBean
        .ListBean> {
    private RecyclerView recyclerView;

    @Override protected void initViews () {
        defaultTitleBar(this).setTitle(getString(R.string.title_performance_bond));
        PerformanceBondLayout layout = (PerformanceBondLayout) viewDataBinding;
        recyclerView = layout.recyclerView;
        commonBinderAdapter = new CommonBinderAdapter<PerformanceBondBean.ListBean>(this,
                                                                                    R.layout.item_performance_bond,
                                                                                    list) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding, CommonBinderHolder holder,
                                int position,
                                PerformanceBondBean.ListBean listBean) {
                ((ItemPerformanceBondLayout) viewDataBinding).setPerformance(listBean);
            }
        };
    }

    @Override protected void initEvents () {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                                               .sizeResId(R.dimen.large_margin_15dp)
                                               .colorResId(R.color.color_transparent)
                                               .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonBinderAdapter.setBinderOnItemClickListener(this);
        networkModel.performanceBondLists(NetworkParams.CUPCAKE);
    }

    @Override public void onBinderItemClick (View view, int pos) {
        super.onBinderItemClick(view, pos);
        PerformanceBondBean.ListBean listBean = list.get(pos);
        String order_id = listBean.getOrder_id();
        startActivity(new Intent(this, OrderDetailActivity.class).putExtra("orderId", order_id));
    }

    @Override protected int getContentView () {
        return R.layout.activity_performance_bond;
    }

    @Override protected void onClick (int id, View view) {

    }

    @Override public void onJsonObjectSuccess (PerformanceBondBean performanceBondBean,
                                               NetworkParams paramsCode) {
        this.list.clear();
        this.list.addAll(performanceBondBean.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }
}
