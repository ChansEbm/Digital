package com.szbb.pro.ui.Activity.Orders.Appointment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.AppointmentCancelLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Orders.Operating.OrderDetailActivity;

/**
 * 退回工单
 */
public class AppointmentReturnActivity extends BaseAty implements InputCallBack {
    private AppointmentCancelLayout appointmentCancelLayout;
    private String operations = "";
    private String orderId = "";
    private String desc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentCancelLayout = (AppointmentCancelLayout) viewDataBinding;
        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        else
            orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_cancel);
    }

    @Override
    protected void initEvents() {
        appointmentCancelLayout.textInputLayout.setHint(getResources().getString(R.string
                .note));
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_appointment_cancel;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.frameLayout:
                InputDialog inputDialog = new InputDialog(this);
                inputDialog.setParams(NetworkParams.CUPCAKE);
                inputDialog.setTitle(getString(R.string.cancel_reason));
                inputDialog.setInputCallBack(this);
                inputDialog.show();
                break;
            case R.id.button:
                if (checkInfo()) {
                    networkModel.returnOrder(orderId, operations, desc, NetworkParams.CUPCAKE
                    );
                }
                //确定提交
                break;
        }
    }

    private boolean checkInfo() {
        desc = appointmentCancelLayout.textInputLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(operations)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                            .please_input_return_back_reason)
            );
            return false;
        }
        return true;
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        operations = word;
        appointmentCancelLayout.tvRemarks.setText(word);
    }

    @Override
    public void onJsonObjectSuccess(Object o, NetworkParams paramsCode) {
        start(OrderDetailActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent
                .FLAG_ACTIVITY_SINGLE_TOP);
    }
}
