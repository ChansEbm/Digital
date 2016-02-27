package com.szbb.pro.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemOrderDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Order.OrderDetailBean;
import com.szbb.pro.entity.Order.ServiceObjActivity;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/5.
 */
public class OrderModel implements View.OnClickListener {

    private MessageDialog messageDialog;
    private String[] labels;
    private AppCompatActivity appCompatActivity;

    public OrderModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void addLabel(@NonNull LinearLayout linearLayout, @NonNull String label) {
        Context context = linearLayout.getContext();
        if (label.isEmpty())
            return;

        labels = label.split(",");

        linearLayout.removeAllViews();
        if (labels.length == 1) {
            TextView textView = getLabelTextView(context);
            textView.setText(label);
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.CUPCAKE);
            linearLayout.addView(textView, 0);
            return;
        }
        int count = labels.length > 3 ? 3 : labels.length;
        for (int i = 0; i < count; i++) {
            TextView textView = getLabelTextView(context);
            textView.setText(labels[i]);
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.CUPCAKE);
            linearLayout.addView(textView, i);
        }
        if (labels.length > 3) {
            TextView textView = getLabelTextView(context);
            textView.setText("...");
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.DONUT);
            textView.setTag(R.id.tag_donut, labels);
            linearLayout.addView(textView, linearLayout.getChildCount());
        }
    }

    private TextView getLabelTextView(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppTools.dip2px
                (56), AppTools.dip2px(22));
        layoutParams.setMarginEnd(AppTools.dip2px((int) context.getResources().getDimension(R
                .dimen.small_margin_5dp)));

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.bg_orange_frame);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(3, 0, 3, 0);
        textView.setSingleLine(true);
        textView.setMaxEms(4);
        textView.setTextColor(context.getResources().getColor(R.color.color_orange_light));
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);

        return textView;
    }

    @Override
    public void onClick(View v) {
        initMessageDialog();
        int flag = (int) v.getTag(R.id.tag_cupcake);
        TextView textView = (TextView) v;
        if (flag == AppKeyMap.CUPCAKE) {//not more label
            messageDialog.setMessage(textView.getText().toString());
        } else if (flag == AppKeyMap.DONUT) {//more label
            StringBuilder stringBuilder = new StringBuilder();
            String[] labels = (String[]) v.getTag(R.id.tag_donut);
            for (int i = 3; i < labels.length; i++) {
                stringBuilder.append(labels[i]);
                stringBuilder.append(",");
                LogTools.i(stringBuilder.toString());
            }
            messageDialog.setMessage(stringBuilder.toString());
        }
        messageDialog.show();
    }

    private void initMessageDialog() {
        messageDialog = new MessageDialog(appCompatActivity);
        messageDialog.setTitle(appCompatActivity.getString(R.string.label));
    }

    /**
     * 点击工单详情的服务项目
     *
     * @param listEntity entity
     * @return 是否可以点击
     */
    public String[] operateServiceObj(OrderDetailBean.ListEntity listEntity) {
        List<OrderDetailBean.ListEntity.ServiceListEntity> serviceListEntities = listEntity
                .getService_list();
        String[] serviceObs = new String[serviceListEntities.size()];
        for (int i = 0; i < serviceListEntities.size(); i++) {
            serviceObs[i] = serviceListEntities.get(i).getService_name();
        }
        return serviceObs;
    }

    /**
     * 服务项目滚轮
     *
     * @param appCompatActivity 上下文
     * @return 是否可以选择服务项目
     */
    public boolean serviceObjWindow(AppCompatActivity appCompatActivity, OrderDetailBean.ListEntity
            listEntity) {
        String last_handle_type = listEntity.getLast_handle_type();
        String last_handle_status = listEntity.getLast_handle_status();

        if ((TextUtils.equals(last_handle_type, "0")) || (TextUtils.equals(last_handle_type, "5")
                && TextUtils.equals(last_handle_status, "1")) || TextUtils.equals
                (last_handle_type, "5") && (TextUtils.equals
                (last_handle_status, "2"))) {
            appCompatActivity.startActivityForResult(new Intent().putExtra("listEntity",
                    listEntity).setClass(appCompatActivity, ServiceObjActivity.class), AppKeyMap
                    .CUPCAKE);
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
        wheelPopupWindow.setCurvedData(appCompatActivity.getResources().getStringArray(R.array
                .service_result));

        return wheelPopupWindow;
    }

    public void setOrderEvents(ItemOrderDetailLayout itemOrderDetail, int position, OrderDetailBean
            .ListEntity listEntity, View.OnClickListener onClickListener) {
        itemOrderDetail.flytServiceObj.setOnClickListener(onClickListener);
        itemOrderDetail.flytServiceObj.setTag(position);
        itemOrderDetail.flytServiceResult.setOnClickListener(onClickListener);
        itemOrderDetail.flytServiceResult.setTag(position);
        itemOrderDetail.tvErrorProduct.setTag(position);
        itemOrderDetail.tvErrorProduct.setOnClickListener(onClickListener);
        itemOrderDetail.include.btnAddPic.setOnClickListener(onClickListener);
        itemOrderDetail.include.btnAddPic.setTag(position);
        itemOrderDetail.llytReport.setOnClickListener(onClickListener);
        itemOrderDetail.llytReport.setTag(position);
        itemOrderDetail.button.setTag(R.id.tag_cupcake, position);
        itemOrderDetail.button.setTag(R.id.tag_donut, listEntity.getButtonType());
        itemOrderDetail.button.setOnClickListener(onClickListener);
    }

    /**
     * 根据上次服务结果选择隐藏或者显示某些项
     *
     * @param itemOrderDetail parent view
     * @param listEntity      entity
     */
    public boolean changeStatusByLastService(ItemOrderDetailLayout itemOrderDetail, OrderDetailBean
            .ListEntity listEntity) {
        if (TextUtils.equals(listEntity.getLast_handle_type(), "3") || TextUtils.equals
                (listEntity.getLast_handle_type(), "4")) {
            itemOrderDetail.flytServiceResult.setVisibility(View.GONE);
            itemOrderDetail.button.setVisibility(View.GONE);
            itemOrderDetail.llytReport.setVisibility(View.VISIBLE);
            itemOrderDetail.llytReport.setEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * 根据本次服务结果 显示或者隐藏图片上传项
     *
     * @param itemOrderDetail itemOrderDetail
     * @param listEntity      listEntity
     */
    public void changeStatusByService(ItemOrderDetailLayout itemOrderDetail, OrderDetailBean
            .ListEntity listEntity, int pos, View.OnClickListener onClickListener) {
        switch (listEntity.getButtonType()) {
            case POSITIVE_REPORT:
            case NEGATIVE_REPORT:
                itemOrderDetail.llytUploadPicParent.setVisibility(View.VISIBLE);
                itemOrderDetail.button.setText(R.string.submit_report);
                itemOrderDetail.llytReport.setVisibility(View.VISIBLE);
                itemOrderDetail.button.setVisibility(View.VISIBLE);
                break;
            case FITTING:
            case EXPENSES:
                itemOrderDetail.llytUploadPicParent.setVisibility(View.GONE);
                itemOrderDetail.button.setVisibility(View.VISIBLE);
                itemOrderDetail.button.setText(R.string.next);
                itemOrderDetail.llytReport.setVisibility(View.GONE);
                break;
            case NAN:
                MarkPictureModel markPictureModel = new MarkPictureModel();
                if (listEntity.getLast_handle_type().equals("3") || listEntity
                        .getLast_handle_type().equals("4")) {
                    itemOrderDetail.llytUploadPicParent.setVisibility(View.VISIBLE);
                    itemOrderDetail.llytReport.setVisibility(View.VISIBLE);
                    addPic(listEntity.getComplete_photos(), itemOrderDetail.llytUploadPic);
                } else {
                    markPictureModel.removeAllPicture(itemOrderDetail.llytUploadPic, pos,
                            onClickListener);
                }
                itemOrderDetail.button.setVisibility(View.GONE);
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
        MarkPictureModel markPictureModel = new MarkPictureModel();
        markPictureModel.setIsNeedDeleteAnimation(false);
        markPictureModel.setIsNeedDeleteIcon(false);
        picLayout.removeAllViews();
        for (OrderDetailBean.ListEntity.CompletePhotosEntity entity : picPaths) {
            markPictureModel.addSinglePictureInLinearLayoutByNetwork(picLayout.getContext(),
                    picLayout, entity.getUrl());
        }
        for (int i = 0; i < picLayout.getChildCount(); i++) {
            picLayout.getChildAt(i).setClickable(false);
        }
    }

    /**
     * 手动添加图片
     *
     * @param picPaths     pic source
     * @param linearLayout layout
     */
    public void addPicInHandler(List<String> picPaths, LinearLayout linearLayout, int position,
                                OnAddPictureDoneListener onAddPictureDoneListener) {
        MarkPictureModel markPictureModel = new MarkPictureModel();
        markPictureModel.setIsNeedDeleteAnimation(false);
        markPictureModel.setIsNeedDeleteIcon(true);
        markPictureModel.setTagPos(position);
        markPictureModel.setOnAddPictureDoneListener(onAddPictureDoneListener);
        for (String path : picPaths) {
            markPictureModel.addSinglePictureInLinearLayoutByLocal(linearLayout.getContext(),
                    linearLayout, path);
        }
    }

    /**
     * check all products
     *
     * @return if products all be done or not
     */
    public boolean checkAllProduct(OrderDetailBean orderDetailBean) {
        List<OrderDetailBean.ListEntity> listEntitys = orderDetailBean.getList();
        for (OrderDetailBean.ListEntity listEntity : listEntitys) {
            boolean isDone = TextUtils.equals("3", listEntity.getLast_handle_type()) || TextUtils
                    .equals("4", listEntity.getLast_handle_type());
            if (!isDone)
                return false;
        }
        return true;
    }
}
