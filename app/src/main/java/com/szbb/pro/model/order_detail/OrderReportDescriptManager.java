package com.szbb.pro.model.order_detail;

import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.R;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;

/**
 * Created by KenChan on 16/6/17.
 */
public class OrderReportDescriptManager {

    public void dealWithReport(AppCompatActivity appCompatActivity,
                               final OrderDetailBean.ListEntity listEntity) {
        InputDialog inputDialog = new InputDialog(appCompatActivity);
        inputDialog.setInputCallBack(new InputCallBack() {
            @Override
            public void inputWord(String word,
                                  NetworkParams networkParams) {
                listEntity.setReport(word);
            }
        });
        inputDialog.setTitle(appCompatActivity.getString(R.string.note));
        inputDialog.setParams(NetworkParams.CUPCAKE);
//                OrderDetailBean.ListEntity reportEntity = list.get
//                        (reportPos);
        inputDialog.show();
    }

}
