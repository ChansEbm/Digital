package com.szbb.pro.model.order_detail;

import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

/**
 * Created by KenChan on 16/6/15.
 */
public class ServiceTypeManager {

    public void serviceResultLogic(AppCompatActivity appCompatActivity,
                                   OrderDetailBean.ListEntity
                                           detail) {
        if (OrderLogicManager.isReporting(appCompatActivity,
                                          detail.getLast_handle_type(),
                                          detail
                                                  .getLast_handle_status())) {
            return;
        }
        WheelPopupWindow wheelPopupWindow = new WheelPopupWindow(appCompatActivity);
        if (appCompatActivity instanceof OnWheelMultiOptsCallback) {
            wheelPopupWindow.setOnWheelMultiOptsCallback((OnWheelMultiOptsCallback)
                                                                 appCompatActivity);
        }
        wheelPopupWindow.setPopupTitle(appCompatActivity.getString(R.string
                                                                           .order_detail_service_result));
        wheelPopupWindow.setOptions(WheelOptions.STANDER);
        wheelPopupWindow.setParams(AppKeyMap.DONUT);
        wheelPopupWindow.setCurvedData(appCompatActivity.getResources()
                                                        .getStringArray(R.array.service_result));
        wheelPopupWindow.showAtDefaultLocation();
    }
}
