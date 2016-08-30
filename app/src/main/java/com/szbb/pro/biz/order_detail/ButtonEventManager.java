package com.szbb.pro.biz.order_detail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.szbb.pro.DealProductLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.ButtonType;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ViewUtils;
import com.szbb.pro.ui.activity.expenses.ExpensesApplyActivity;
import com.szbb.pro.ui.activity.orders.operating.FittingAdditionalActivity;
import com.szbb.pro.widget.deleter.DeleterConfigs;
import com.szbb.pro.widget.deleter.DeleterConfigsBuilder;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KenChan on 16/6/13.`
 */
public class ButtonEventManager
        implements View.OnClickListener {

    public void buttonLogic(ButtonType buttonType,
                            DealProductLayout dealProductLayout) {
        switch (buttonType) {
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
                OrderDetailBean.ListEntity detail = dealProductLayout.getDetail();
                if (isProductHasLastResult(detail)) {//该产品已有最终结果
                    dealProductLayout.llytUploadPicParent.setVisibility(View.VISIBLE);
                    dealProductLayout.llytReport.setVisibility(View.VISIBLE);
                    ArrayList<String> urls = getCompletePhotoUrls(dealProductLayout);
                    dealProductLayout.deleterScrollLayout.setConfigs(new DeleterConfigsBuilder()
                                                                             .setMode(DeleterConfigs.MODE_VIEW)
                                                                             .setNetworkUrls(urls)
                                                                             .createDeleterConfigs());
                }
                dealProductLayout.button.setVisibility(View.GONE);
                break;
        }
    }

    //获得完工图片的所有url
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


    //判断该产品是否有最终结果
    private boolean isProductHasLastResult(@NotNull OrderDetailBean.ListEntity detail) {
        return detail.getLast_handle_type()
                     .equals("3") || detail.getLast_handle_type()
                                           .equals("4");
    }


    public ButtonEventManager changeButtonTag(Context context,
                                              int wheelPos,
                                              Button button,
                                              OrderDetailBean.ListEntity detail) {
        switch (wheelPos + 1) {
            case 1:
                button.setTag(ButtonType.POSITIVE_REPORT);
                break;
            case 4:
                button.setTag(ButtonType.NEGATIVE_REPORT);
                break;
            case 2:
                button.setTag(ButtonType.FITTING);
                showFittingToast(context,
                                 detail);
                break;
            case 3:
                button.setTag(ButtonType.EXPENSES);
                break;
        }
        return this;
    }

    private void showFittingToast(Context context,
                                  OrderDetailBean.ListEntity detail) {
        if (detail.getLast_handle_type()
                  .equals("2")) {
            Toast.makeText(context,
                           "本工单已申请过一次配件，相关受理进度可在产品信息上方的\"工单申请记录\"中查询",
                           Toast.LENGTH_SHORT)
                 .show();
        }
    }


    public void progressButtonEvent(BaseAty baseAty,
                                    String orderId,
                                    Button button,
                                    OrderDetailBean.ListEntity detail) {
        if (!(new ServiceObjManager().hasSelectServiceId(detail))) {
            AppTools.showNormalSnackBar(baseAty.getWindow()
                                               .getDecorView(),
                                        baseAty.getString(R.string
                                                                  .order_detail_please_enter_service_option));
            return;
        }
        ButtonType buttonType = (ButtonType) button.getTag();
        if (buttonType != ButtonType.NAN) {
            switch (buttonType) {
                case POSITIVE_REPORT://complete on site
                    report(baseAty,
                           detail,
                           "1");
                    break;
                case NEGATIVE_REPORT://can not be repaired
                    report(baseAty,
                           detail,
                           "2");
                    break;
                case FITTING://apply fitting
                    String serviceId = detail.getServiceid();
                    String detailId = detail.getDetailid();
                    String accId = detail.getAcce_exe_type();
                    button.getContext()
                          .startActivity(new Intent().putExtra("orderId",
                                                               orderId)
                                                     .putExtra("serviceId",
                                                               serviceId)
                                                     .putExtra("accId",
                                                               accId)
                                                     .putExtra("detailId",
                                                               detailId)
                                                     .setClass(button.getContext(),
                                                               FittingAdditionalActivity.class));

                    ViewUtils.startCountDown(button,
                                             button.getContext()
                                                   .getString(R.string.fitting_unlock),
                                             button.getContext()
                                                   .getString(R.string.next),
                                             5000);
                    break;
                case EXPENSES://apply expenses
                    button.getContext()
                          .startActivity(new Intent().putExtra("detailid",
                                                               detail.getDetailid())
                                                     .setClass(button.getContext(),
                                                               ExpensesApplyActivity.class));
                    break;
            }
        }
    }

    private void report(BaseAty baseAty,
                        OrderDetailBean.ListEntity listEntity,
                        String isComplete
                       ) {
        List<String> thumbs = new ArrayList<>();
        thumbs.addAll(listEntity.getAddPics());
        String report = listEntity.getReport();
        String detailId = listEntity.getDetailid();

        baseAty.networkModel.submitReport(detailId,
                                          isComplete,
                                          thumbs,
                                          report,
                                          NetworkParams.FROYO);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


}
