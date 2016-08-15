package com.szbb.pro.ui.activity.orders.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.AppointmentAlterLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

/**
 * Created by ChanZeeBm on 2015/11/16.
 * 修改预约
 */
public class AppointmentAlterActivity extends BaseAty<BaseBean, BaseBean> implements
        OnWheelOptsSelectCallback {
    private AppointmentAlterLayout appointmentAlterLayout;
    private WheelPopupWindow wheelPopupWindow;
    private EditText edtRemark;

    private String appointTime = "";
    private String updateReason = "";
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentAlterLayout = (AppointmentAlterLayout) viewDataBinding;
        if (getIntent() != null)
            orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_alter);
        edtRemark = appointmentAlterLayout.textInputLayout.getEditText();
        wheelPopupWindow = new WheelPopupWindow(this);
    }

    @Override
    protected void initEvents() {
        defaultTitleBar(this).setTitle(R.string.order_detail_edit_appointment);
        appointmentAlterLayout.textInputLayout.setHint(getResources().getString(R.string.note));
        wheelPopupWindow.setOnWheelOptsSelectCallback(this);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_appointment_alter;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_reason:
                AppTools.hideSoftInputMethod(appointmentAlterLayout.textInputLayout);
                wheelPopupWindow.setOptions(WheelOptions.STANDER);
                wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array
                        .edit_appointment_reasons));
                wheelPopupWindow.setPopupTitle(getString(R.string.alter_reason));
                wheelPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.flyt_new_time:
                AppTools.hideSoftInputMethod(appointmentAlterLayout.textInputLayout);
                wheelPopupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
                wheelPopupWindow.setPopupTitle(getString(R.string.alter_new_time));
                wheelPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.button:
                String remarks = edtRemark.getText().toString();
                if (checkNecessary())
                    networkModel.changeAppoint(orderId, appointTime, updateReason, remarks, null
                    );
                break;
        }
    }


    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        switch (wheelOptions) {
            case STANDER:
                updateReason = String.valueOf(index + 1);
                appointmentAlterLayout.tvReason.setText(selectData);
                break;
            case DATE_WITH_TIME:
                appointTime = selectData;
                appointmentAlterLayout.tvTime.setText(appointTime);
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        Toast.makeText(AppointmentAlterActivity.this, "修改预约成功!", Toast.LENGTH_SHORT).show();
        AppTools.sendBroadcast(new Bundle(), AppKeyMap.REFRESH_ORDER_ACTION);
        start(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    private boolean checkNecessary() {
        if (appointTime.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_choose_time)
            );
            return false;
        }
        if (updateReason.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_choose_reason));
            return false;
        }
        return true;
    }
}
