package com.szbb.pro.ui.activity.orders.operating;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.DealProductLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.ButtonType;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.biz.order_detail.ButtonEventManager;
import com.szbb.pro.biz.order_detail.ErrorReportManager;
import com.szbb.pro.biz.order_detail.OrderManager;
import com.szbb.pro.biz.order_detail.OrderReportDescriptManager;
import com.szbb.pro.biz.order_detail.ServiceObjManager;
import com.szbb.pro.biz.order_detail.ServiceTypeManager;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;

import java.util.List;
import java.util.Set;

/**
 * Created by KenChan on 16/6/14.
 */
public class DealProductActivity
        extends BaseAty
        implements OnWheelMultiOptsCallback, DeleterHandlerCallback {
    private DealProductLayout dealProductLayout;
    private String orderId = "";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealProductLayout = (DealProductLayout) viewDataBinding;
    }

    @Override
    protected void initViews () {
        defaultTitleBar(this).setTitle("服务报告提交");
        fillActivityData();
    }

    /**
     * 填充页面数据
     */
    private void fillActivityData () {
        if (getIntent() == null) {
            AppTools.removeSingleActivity(this);
        }
        OrderDetailBean.ListEntity product = getIntent().getParcelableExtra("product");
        if (!TextUtils.isEmpty(product.getProduct_thumb())) {
            Glide.with(this)
                 .load(product.getProduct_thumb())
                 .into(dealProductLayout.ivContactItemImage);
        }
        orderId = getIntent().getStringExtra("orderId");
        dealProductLayout.setDetail(product);

        new OrderManager().changeStatusByLastService(dealProductLayout,
                                                     product);
    }

    @Override
    protected void initEvents () {
        dealProductLayout.deleterScrollLayout.setDeleterHandlerCallback(this);
    }

    @Override
    protected int getContentView () {
        return R.layout.deal_product_activity;
    }

    @Override
    protected void onClick (int id,
                            View view) {
        OrderDetailBean.ListEntity detail = dealProductLayout.getDetail();
        switch (id) {
            case R.id.tv_error_product:
                new ErrorReportManager().showErrorPopupWindow(this,
                                                              detail);
                break;
            case R.id.flyt_service_obj://服务项目
                if (new ServiceObjManager().isCanSelectServiceObj(this,
                                                                  detail)) {
                    new ServiceObjManager().openServiceObjActivity(this,
                                                                   detail);
                } else {
                    new ServiceObjManager().showErrorDialog(this,
                                                            detail);
                }
                break;
            case R.id.flyt_service_result://本次服务结果
                new ServiceTypeManager().serviceResultLogic(this,
                                                            detail);
                break;

            case R.id.llyt_report:
                new OrderReportDescriptManager().dealWithReport(this,
                                                                detail);
                break;
            case R.id.button:
                new ButtonEventManager().progressButtonEvent(this,
                                                             orderId,
                                                             dealProductLayout.button,
                                                             detail);
                break;
        }
    }

    @Override
    protected void onActivityResult (int requestCode,
                                     int resultCode,
                                     Intent data) {
        super.onActivityResult(requestCode,
                               resultCode,
                               data);
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://服务项目
                if (resultCode == RESULT_OK && data != null) {
                    String serviceName = data.getStringExtra("serviceName");
                    dealProductLayout.getDetail()
                                     .setService_name(serviceName);
                    String serviceId = data.getStringExtra("serviceId");
                    dealProductLayout.getDetail()
                                     .setService_id(serviceId);
                    AppTools.sendBroadcast(new Bundle(),
                                           AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
                }
                break;
        }
    }

    @Override
    public void onWheelSelect (String selectData,
                               WheelOptions wheelOptions,
                               int params,
                               int index) {
        OrderDetailBean.ListEntity detail = dealProductLayout.getDetail();
        if (params == AppKeyMap.DONUT) {//服务结果
            detail.setThis_service_name(selectData);
            new ButtonEventManager().changeButtonTag(this,
                                                     index,
                                                     dealProductLayout.button,
                                                     detail)
                                    .buttonLogic((ButtonType) dealProductLayout.button.getTag(),
                                                 dealProductLayout);
        }
    }

    @Override
    public void onJsonObjectSuccess (BaseBean baseBean,
                                     NetworkParams paramsCode) {
        AppTools.sendBroadcast(new Bundle(),
                               AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
        AppTools.removeSingleActivity(this);
    }

    @Override
    public void success (Set<Integer> keySet,
                         List<String> photoPaths) {
        dealProductLayout.getDetail()
                         .getAddPics()
                         .clear();
        dealProductLayout.getDetail()
                         .getAddPics()
                         .addAll(photoPaths);
    }
}
