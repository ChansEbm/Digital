package com.szbb.pro.biz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.DealProductLayout;
import com.szbb.pro.ItemOrderDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.biz.order_detail.OrderManager;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.orders.operating.ServiceObjActivity;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/5.
 */
public class OrderBiz {

    private AppCompatActivity appCompatActivity;

    public OrderBiz (AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void addLabel(@NonNull LinearLayout linearLayout,
                         @NonNull String label) {
        OrderManager.addLabel(appCompatActivity,
                              linearLayout,
                              label);
    }

    /**
     * 服务项目滚轮
     *
     * @param appCompatActivity 上下文
     * @return 是否可以选择服务项目
     */
    public boolean serviceObjWindow(AppCompatActivity appCompatActivity,
                                    OrderDetailBean.ListEntity
                                            listEntity) {
        String last_handle_type = listEntity.getLast_handle_type();
        String last_handle_status = listEntity.getLast_handle_status();
        if (isReporting(appCompatActivity,
                        last_handle_type,
                        last_handle_status)) {
            return true;
        }
        if ((TextUtils.equals(last_handle_type,
                              "0"))
                ||
                (TextUtils.equals(last_handle_type,
                                  "5") && TextUtils.equals(last_handle_status,
                                                           "1"))
                ||
                (TextUtils.equals(last_handle_type,
                                  "5") && (TextUtils.equals(last_handle_status,
                                                            "2")))
                ) {
            appCompatActivity.startActivityForResult(new Intent().putExtra("listEntity",
                                                                           listEntity)
                                                                 .setClass
                                                                         (appCompatActivity,
                                                                          ServiceObjActivity.class),
                                                     AppKeyMap
                                                             .CUPCAKE);
            return true;
        }
        return false;
    }

    public boolean isReporting(AppCompatActivity appCompatActivity,
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


    /**
     * 本次服务结果的滚轮
     *
     * @param appCompatActivity        上下文
     * @param onWheelMultiOptsCallback 结果回调
     * @return 滚轮控件
     */
    public WheelPopupWindow thisServiceType(AppCompatActivity appCompatActivity,
                                            OnWheelMultiOptsCallback onWheelMultiOptsCallback) {
        WheelPopupWindow wheelPopupWindow = new WheelPopupWindow(appCompatActivity);
        wheelPopupWindow.setOnWheelMultiOptsCallback(onWheelMultiOptsCallback);
        wheelPopupWindow.setPopupTitle(appCompatActivity.getString(R.string
                                                                           .order_detail_service_result));
        wheelPopupWindow.setOptions(WheelOptions.STANDER);
        wheelPopupWindow.setParams(AppKeyMap.DONUT);
        wheelPopupWindow.setCurvedData(appCompatActivity.getResources()
                                                        .getStringArray(R.array.service_result));

        return wheelPopupWindow;
    }

    public void setOrderEvents(DealProductLayout dealProductLayout,
                               int position,
                               OrderDetailBean.ListEntity listEntity,
                               View.OnClickListener onClickListener) {
        dealProductLayout.flytServiceObj.setOnClickListener(onClickListener);
        dealProductLayout.flytServiceObj.setTag(position);
        dealProductLayout.flytServiceResult.setOnClickListener(onClickListener);
        dealProductLayout.flytServiceResult.setTag(position);
        dealProductLayout.tvErrorProduct.setTag(position);
        dealProductLayout.tvErrorProduct.setOnClickListener(onClickListener);
        dealProductLayout.llytReport.setOnClickListener(onClickListener);
        dealProductLayout.llytReport.setTag(position);
        dealProductLayout.button.setTag(R.id.tag_cupcake,
                                        position);
        dealProductLayout.button.setTag(R.id.tag_donut,
                                        listEntity.getButtonType());
        dealProductLayout.button.setOnClickListener(onClickListener);
    }

//    /**
//     * 根据上次服务结果选择隐藏或者显示某些项
//     *
//     * @param dealProductLayout parent view
//     * @param listEntity        entity
//     */
//    public boolean changeStatusByLastService(DealProductLayout dealProductLayout,
//                                             OrderDetailBean
//                                                     .ListEntity listEntity) {
//        if (TextUtils.equals(listEntity.getLast_handle_type(),
//                             "3") || TextUtils.equals
//                (listEntity.getLast_handle_type(),
//                 "4")) {
//            dealProductLayout.flytServiceResult.setVisibility(View.GONE);
//            dealProductLayout.button.setVisibility(View.GONE);
//            dealProductLayout.tvIcon.setVisibility(View.GONE);
//            dealProductLayout.tvErrorProduct.setVisibility(View.GONE);
//            dealProductLayout.llytReport.setVisibility(View.VISIBLE);
//            dealProductLayout.llytReport.setEnabled(false);
//
//            dealProductLayout.ivResult.setVisibility(View.VISIBLE);
//            if (TextUtils.equals(listEntity.getLast_handle_type(),
//                                 "3")) {
//                dealProductLayout.ivResult.setImageResource(R.mipmap.ic_already_done);
//            } else if (TextUtils.equals(listEntity.getLast_handle_type(),
//                                        "4")) {
//                dealProductLayout.ivResult.setImageResource(R.mipmap.ic_cannt_be_done);
//            }
//
//            return true;
//        }
//        return false;
//    }

    public void displayFinishSign(ItemOrderDetailLayout itemOrderDetailLayout,
                                  OrderDetailBean.ListEntity listEntity) {
        itemOrderDetailLayout.ivResult.setVisibility(View.VISIBLE);
        if (TextUtils.equals(listEntity.getLast_handle_type(),
                             "3")) {
            itemOrderDetailLayout.ivResult.setImageResource(R.mipmap.ic_already_done);
            itemOrderDetailLayout.treatmentImmediately.setVisibility(View.GONE);
        } else if (TextUtils.equals(listEntity.getLast_handle_type(),
                                    "4")) {
            itemOrderDetailLayout.ivResult.setImageResource(R.mipmap.ic_cannt_be_done);
            itemOrderDetailLayout.treatmentImmediately.setVisibility(View.GONE);
        } else {
            itemOrderDetailLayout.ivResult.setVisibility(View.GONE);
            itemOrderDetailLayout.treatmentImmediately.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据本次服务结果 显示或者隐藏图片上传项
     *
     * @param dealProductLayout dealProductLayout
     * @param listEntity        listEntity
     */
    public void changeStatusByService(DealProductLayout dealProductLayout,
                                      OrderDetailBean.ListEntity listEntity,
                                      int pos,
                                      View.OnClickListener onClickListener) {
        switch (listEntity.getButtonType()) {
            case POSITIVE_REPORT:
            case NEGATIVE_REPORT:
                dealProductLayout.llytUploadPicParent.setVisibility(View.VISIBLE);
                dealProductLayout.button.setText(R.string.submit_report);
                dealProductLayout.llytReport.setVisibility(View.VISIBLE);
                dealProductLayout.button.setVisibility(View.VISIBLE);
                break;
            case FITTING:
            case EXPENSES:
                dealProductLayout.llytUploadPicParent.setVisibility(View.GONE);
                dealProductLayout.button.setVisibility(View.VISIBLE);
                dealProductLayout.button.setText(R.string.next);
                dealProductLayout.llytReport.setVisibility(View.GONE);
                break;
            case NAN:
                MarkPictureBiz markPictureModel = new MarkPictureBiz();
//                if (listEntity.getLast_handle_type().equals("3") || listEntity
//                        .getLast_handle_type().equals("4")) {
//                    dealProductLayout.llytUploadPicParent.setVisibility(View.VISIBLE);
//                    dealProductLayout.llytReport.setVisibility(View.VISIBLE);
//                    addPic(listEntity.getComplete_photos(), dealProductLayout.llytUploadPic);
//                } else {
//                    markPictureModel.removeAllPicture(dealProductLayout.llytUploadPic, pos,
//                            onClickListener);
//                }
                dealProductLayout.button.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 当上次结果是完成状态 则添加返回的图片
     *
     * @param picPaths  pic source
     * @param picLayout layout
     */
    public void addPic(List<OrderDetailBean.ListEntity.CompletePhotosEntity> picPaths,
                       LinearLayout picLayout) {
        MarkPictureBiz markPictureModel = new MarkPictureBiz();
        markPictureModel.setIsNeedDeleteAnimation(false);
        markPictureModel.setIsNeedDeleteIcon(false);
        picLayout.removeAllViews();

        for (OrderDetailBean.ListEntity.CompletePhotosEntity entity : picPaths) {
            markPictureModel.savePicturePath(entity.getUrl());
        }
        markPictureModel.addSinglePictureInLinearLayout(picLayout.getContext(),
                                                        picLayout,
                                                        true);
        for (int i = 0;
             i < picLayout.getChildCount();
             i++) {
            picLayout.getChildAt(i)
                     .setClickable(false);
        }
    }

    /**
     * 手动添加图片
     *
     * @param picPaths     pic source
     * @param linearLayout layout
     */
    public void addPicInHandler(List<String> picPaths,
                                LinearLayout linearLayout,
                                int position,
                                OnAddPictureDoneListener onAddPictureDoneListener) {
        MarkPictureBiz markPictureModel = new MarkPictureBiz();
        markPictureModel.setIsNeedDeleteAnimation(false);
        markPictureModel.setIsNeedDeleteIcon(true);
        markPictureModel.setTagPos(position);
        markPictureModel.setAutoAddPics(false);
        markPictureModel.setOnAddPictureDoneListener(onAddPictureDoneListener);
        for (String path : picPaths) {
            markPictureModel.savePicturePath(path);
        }
        markPictureModel.addSinglePictureInLinearLayout(linearLayout.getContext(),
                                                        linearLayout,
                                                        false);
    }

}
