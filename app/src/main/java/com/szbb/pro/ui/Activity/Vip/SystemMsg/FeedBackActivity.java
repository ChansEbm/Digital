package com.szbb.pro.ui.Activity.Vip.SystemMsg;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.szbb.pro.FeedBackLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.eum.NetworkParams;

public class FeedBackActivity extends BaseAty {
    private FeedBackLayout feedBackLayout;
    private EditText edtFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedBackLayout = (FeedBackLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        edtFeedBack = feedBackLayout.edtFeed;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feed_back_actvity;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.button:
                String feedBack = edtFeedBack.getText().toString();
                if (!feedBack.isEmpty())
                    networkModel.submitFeedback(feedBack, NetworkParams.CUPCAKE);
                break;
        }
    }
}
