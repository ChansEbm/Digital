package com.szbb.pro.biz.order_detail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.szbb.pro.DealProductLayout;
import com.szbb.pro.R;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.widget.deleter.DeleterConfigs;

import java.util.ArrayList;

/**
 * Created by KenChan on 16/6/13.
 */
public class OrderManager {

    public static void addLabel(AppCompatActivity appCompatActivity,
                                @NonNull LinearLayout
                                        linearLayout,
                                @NonNull String label) {
        new LabelManager(appCompatActivity).addLabel(linearLayout,
                                                     label);
    }

    public void changeStatusByLastService(DealProductLayout dealProductLayout,
                                          OrderDetailBean.ListEntity listEntity) {
        if (TextUtils.equals(listEntity.getLast_handle_type(),
                             "3") || TextUtils.equals
                (listEntity.getLast_handle_type(),
                 "4")) {

            changeAttrThrowLastHandleType(dealProductLayout);
            visiblePhotos(dealProductLayout);
            displayFinishSign(dealProductLayout,
                              listEntity);

        }
    }

    private void displayFinishSign(DealProductLayout dealProductLayout,
                                   OrderDetailBean.ListEntity listEntity) {
        if (TextUtils.equals(listEntity.getLast_handle_type(),
                             "3")) {
            dealProductLayout.ivResult.setImageResource(R.mipmap.ic_already_done);
        } else if (TextUtils.equals(listEntity.getLast_handle_type(),
                                    "4")) {
            dealProductLayout.ivResult.setImageResource(R.mipmap.ic_cannt_be_done);
        }
    }

    private void changeAttrThrowLastHandleType(DealProductLayout dealProductLayout) {
        dealProductLayout.flytServiceResult.setVisibility(View.GONE);
        dealProductLayout.button.setVisibility(View.GONE);
        dealProductLayout.tvIcon.setVisibility(View.GONE);
        dealProductLayout.tvErrorProduct.setVisibility(View.GONE);
        dealProductLayout.llytReport.setVisibility(View.VISIBLE);
        dealProductLayout.llytReport.setEnabled(false);
        dealProductLayout.ivResult.setVisibility(View.VISIBLE);

    }

    private void visiblePhotos(DealProductLayout dealProductLayout) {
        dealProductLayout.llytUploadPicParent.setVisibility(View.VISIBLE);
        dealProductLayout.llytReport.setVisibility(View.VISIBLE);
        ArrayList<String> urls = getCompletePhotoUrls(dealProductLayout);
        dealProductLayout.deleterScrollLayout.setConfigs(dealProductLayout.deleterScrollLayout
                                                                 .getConfigsBuilder()
                                                                 .setMode(DeleterConfigs.MODE_VIEW)
                                                                 .setNetworkUrls(urls)
                                                                 .createDeleterConfigs());
    }


    @NonNull
    private ArrayList<String> getCompletePhotoUrls(DealProductLayout dealProductLayout) {
        ArrayList<String> urls = new ArrayList<>();
        for (OrderDetailBean.ListEntity.CompletePhotosEntity completePhotosEntity :
                dealProductLayout.getDetail()
                                 .getComplete_photos()) {
            urls.add(completePhotosEntity.getUrl());
        }
        return urls;
    }


}
