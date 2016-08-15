package com.szbb.pro.ui.activity.vip.feedback;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.szbb.pro.FeedbackDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.vip.FeedbackBean;
import com.szbb.pro.eum.NetworkParams;

public class FeedbackDetailActivity extends BaseAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle("反馈详情");
        Parcelable content = getIntent().getParcelableExtra("content");
        if (content == null) {
            finish();
        }
        FeedbackDetailLayout layout = (FeedbackDetailLayout) viewDataBinding;
        layout.setDetail((FeedbackBean.ListBean) content);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback_detail;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onJsonObjectResponse(Object o, NetworkParams paramsCode) {

    }
}
