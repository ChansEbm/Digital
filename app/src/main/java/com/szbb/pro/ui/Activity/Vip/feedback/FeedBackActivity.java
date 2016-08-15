package com.szbb.pro.ui.activity.vip.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.szbb.pro.FeedBackLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseAty<BaseBean, BaseBean> implements OnWheelOptsSelectCallback {
    private FeedBackLayout feedBackLayout;
    private EditText edtFeedBack;
    private int options = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedBackLayout = (FeedBackLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.vip_feedback);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, FeedbackHistoryActivity.class));
        return item.getItemId() == R.id.menu_feedback;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("options", options);
        outState.putString("feedback", feedBackLayout.edtFeed.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.options = savedInstanceState.getInt("options");
        feedBackLayout.edtFeed.setText(savedInstanceState.getString("feedback"));
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_classify:
                WheelPopupWindow wheelPopupWindow = new WheelPopupWindow(this);
                wheelPopupWindow.setOptions(WheelOptions.STANDER);
                wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array.feedback_options));
                wheelPopupWindow.setOnWheelOptsSelectCallback(this);
                wheelPopupWindow.showAtDefaultLocation();
                break;
            case R.id.button:
                String feedBack = edtFeedBack.getText().toString();
                if (feedBack.isEmpty()) {
                    AppTools.showNormalSnackBar(parentView, "请填写您的意见!");
                    return;
                }
                if (options == -1) {
                    AppTools.showNormalSnackBar(parentView, "请选择反馈类别!");
                    return;
                }
                networkModel.submitFeedback(feedBack, String.valueOf(options), NetworkParams.CUPCAKE);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        Toast.makeText(FeedBackActivity.this, "您的意见已成功提交!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        feedBackLayout.tvClassify.setText(selectData);
        this.options = index + 1;
    }
}
