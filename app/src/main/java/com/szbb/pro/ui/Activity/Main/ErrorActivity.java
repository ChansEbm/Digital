package com.szbb.pro.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ErrorActivity extends BaseAty<BaseBean, BaseBean> {
    private String stackTraceFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNeedBackground = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        stackTraceFromIntent = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());
        networkModel.collect(stackTraceFromIntent);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_error;
    }

    @Override
    protected void onClick(int id, View view) {
        CustomActivityOnCrash.EventListener eventListenerFromIntent = CustomActivityOnCrash.getEventListenerFromIntent(getIntent());

        switch (id) {
            case R.id.btn_error_shutdown:
                CustomActivityOnCrash.closeApplication(this, eventListenerFromIntent);
                break;
            case R.id.btn_error_restart:
                Class<? extends Activity> restartActivityClassFromIntent = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
                CustomActivityOnCrash.restartApplicationWithIntent(this, new Intent(this, restartActivityClassFromIntent), eventListenerFromIntent);
                break;
            case R.id.btn_show_error_report:
                final MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.setTitle("错误详情").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageDialog.dismiss();
                    }
                }).setMessage(stackTraceFromIntent).show();
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
    }
}
