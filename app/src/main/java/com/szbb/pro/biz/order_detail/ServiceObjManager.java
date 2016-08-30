package com.szbb.pro.biz.order_detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.ui.activity.orders.operating.ServiceObjActivity;

/**
 * Created by KenChan on 16/6/15.
 */
public class ServiceObjManager {
    public boolean isCanSelectServiceObj(AppCompatActivity appCompatActivity,
                                         OrderDetailBean.ListEntity detail) {
        String lastHandleType = detail.getLast_handle_type();
        String lastHandleStatus = detail.getLast_handle_status();
        //如果该产品正在进行产品错误报告,或者正在进行审核 则返回false 否则true
        return !OrderLogicManager.isReporting(appCompatActivity,
                                              lastHandleType,
                                              lastHandleStatus) &&
                canBeSelectByLastHandleTypeAndLastHandleStatus
                        (lastHandleType,
                         lastHandleStatus);
    }

    private boolean canBeSelectByLastHandleTypeAndLastHandleStatus(String lastHandleType,
                                                                   String lastHandleStatus) {

        return (TextUtils.equals(lastHandleType,
                                 "0"))
                ||
                (TextUtils.equals(lastHandleType,
                                  "5") && TextUtils.equals(lastHandleStatus,
                                                           "1"))
                ||
                (TextUtils.equals(lastHandleType,
                                  "5") && (TextUtils.equals(lastHandleStatus,
                                                            "2")));
    }

    public void openServiceObjActivity(AppCompatActivity appCompatActivity,
                                       OrderDetailBean.ListEntity detail) {
        appCompatActivity.startActivityForResult(new Intent().putExtra("listEntity",
                                                                       detail)
                                                             .setClass(appCompatActivity,
                                                                       ServiceObjActivity.class),
                                                 AppKeyMap.CUPCAKE);
    }

    public boolean hasSelectServiceId(OrderDetailBean.ListEntity detail) {
        return !detail.getService_id()
                      .equals("0");
    }

    public void showErrorDialog(AppCompatActivity appCompatActivity,
                                OrderDetailBean.ListEntity detail) {
        String serviceName = detail.getService_name();
        String serviceDesc = detail.getService_desc();
        final MessageDialog dialog = new MessageDialog(appCompatActivity);
        dialog.setTitle(serviceName)
              .setMessage(serviceDesc)
              .setPositiveButton
                      (appCompatActivity.getString(R.string.positive),
                       new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.dismiss();
                           }
                       })
              .show();
    }

}
