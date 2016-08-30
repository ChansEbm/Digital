package com.szbb.pro.biz.order_detail;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.szbb.pro.tools.AppTools;

/**
 * Created by KenChan on 16/6/13.
 */
public class OrderLogicManager {

    public static boolean isReporting(AppCompatActivity appCompatActivity,
                                      String last_handle_type,
                                      String last_handle_status) {
        if (TextUtils.equals(last_handle_type,
                             "5") && TextUtils.equals(last_handle_status,
                                                      "0")) {
            AppTools.showNormalSnackBar(appCompatActivity.getWindow()
                                                         .getDecorView(),
                                        "工单产品信息修改中，请稍后操作工单");
            return true;
        }
        return false;
    }


}
