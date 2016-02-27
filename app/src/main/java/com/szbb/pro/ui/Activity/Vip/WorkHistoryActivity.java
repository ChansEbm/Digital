package com.szbb.pro.ui.Activity.Vip;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemWorkHistoryLayout;
import com.szbb.pro.R;
import com.szbb.pro.WorkHistoryLayout;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Vip.WorkHistoryBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

import java.util.List;

public class WorkHistoryActivity extends BaseAty<WorkHistoryBean, WorkHistoryBean.ListEntity> {

    private WorkHistoryLayout workHistoryLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workHistoryLayout = (WorkHistoryLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_work_history);
        recyclerView = workHistoryLayout.recyclerView;

        commonBinderAdapter = new CommonBinderAdapter<WorkHistoryBean.ListEntity>(this, R.layout
                .item_work_history, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, WorkHistoryBean.ListEntity listEntity) {
                ((ItemWorkHistoryLayout) viewDataBinding).setHistory(listEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        networkModel.orderHistory(NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_work_history;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(WorkHistoryBean workHistoryBean, NetworkParams paramsCode) {
        final List<WorkHistoryBean.ListEntity> list = workHistoryBean.getList();
        this.list.addAll(list);
        commonBinderAdapter.notifyDataSetChanged();
        if (list.isEmpty())
            workHistoryLayout.imageView.setVisibility(View.VISIBLE);
    }
}
