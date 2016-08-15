package com.szbb.pro.ui.activity.vip.feedback;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.szbb.pro.FeedBackListLayout;
import com.szbb.pro.ItemFeedbackListLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.vip.FeedbackBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

public class FeedbackHistoryActivity extends BaseAty<FeedbackBean, FeedbackBean.ListBean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle("反馈列表");
        commonBinderAdapter = new CommonBinderAdapter<FeedbackBean.ListBean>(this,
                R.layout.item_feedback_list,
                list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int position, FeedbackBean.ListBean listBean) {
                ((ItemFeedbackListLayout) viewDataBinding).setFeedback(listBean);
            }
        };
    }

    @Override
    protected void initEvents() {
        FeedBackListLayout feedBackListLayout = (FeedBackListLayout) viewDataBinding;
        feedBackListLayout.recyclerView.setAdapter(commonBinderAdapter);
        feedBackListLayout.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedBackListLayout.recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        commonBinderAdapter.setBinderOnItemClickListener(this);

        networkModel.historyFeedback(NetworkParams.CUPCAKE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback_list;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectSuccess(FeedbackBean feedbackBean, NetworkParams paramsCode) {
        this.list.clear();
        this.list.addAll(feedbackBean.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        FeedbackBean.ListBean listBean = list.get(pos);
        startActivity(new Intent(this, FeedbackDetailActivity.class).putExtra("content", listBean));
    }
}
