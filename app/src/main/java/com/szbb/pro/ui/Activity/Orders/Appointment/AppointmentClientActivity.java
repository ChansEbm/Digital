package com.szbb.pro.ui.Activity.Orders.Appointment;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.AppointmentClientLayout;
import com.szbb.pro.ItemAppointmentLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Order.OrderDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.model.OrderModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Main.MainActivity;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import org.solovyev.android.views.llm.LinearLayoutManager;

/**
 * Created by ChanZeeBm on 2015/10/26.
 * 预约客户
 */
public class AppointmentClientActivity extends BaseAty<BaseBean, OrderDetailBean.ListEntity>
        implements OnWheelOptsSelectCallback,
        InputCallBack {
    private AppointmentClientLayout appointmentClientLayout;
    private RecyclerView recyclerView;
    private OrderModel orderModel;
    private String orderId = "";
    private WheelPopupWindow wheelPopupWindow;
    private FrameLayout flytAppointmentTime;
    private RelativeLayout ryltAppointmentRemark;
    private TextView tvRemark;
    private TextView tvAppointmentTime;
    private TextView tvAppointmentResult;
    private InputDialog inputDialog;
    private TextView tvAddress;

    private String result = "";
    private String appointmentTime = "";
    private String reMark = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentClientLayout = (AppointmentClientLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        titleBarTools(this).defaultToolBar(this).setTitle(R.string.title_client_appointment);
        recyclerView = appointmentClientLayout.recyclerView;
        flytAppointmentTime = appointmentClientLayout.flytAppointmentTime;
        ryltAppointmentRemark = appointmentClientLayout.ryltAppointmentRemark;
        tvRemark = appointmentClientLayout.tvAppointmentRemark;
        tvAppointmentTime = appointmentClientLayout.tvAppointmentTime;
        tvAppointmentResult = appointmentClientLayout.tvAppointmentResult;

        orderModel = new OrderModel(this);
        wheelPopupWindow = new WheelPopupWindow(this);
        initAdapter();
    }

    @Override
    protected void initEvents() {
        wheelPopupWindow.setOnWheelOptsSelectCallback(this);
        appointmentClientLayout.flytClientAppointmentChooseResult.setOnClickListener(this);
        appointmentClientLayout.btnClientAppointmentSubmit.setOnClickListener(this);
        appointmentClientLayout.ryltAppointmentRemark.setOnClickListener(this);
        appointmentClientLayout.tvClientAppointmentClientAddress.setOnClickListener(this);
        flytAppointmentTime.setOnClickListener(this);
        ryltAppointmentRemark.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setAdapter(commonBinderAdapter);

        appointmentClientLayout.scrollView.smoothScrollTo(0, 0);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderid");
            networkModel.orderDetail(orderId, "", NetworkParams
                    .CUPCAKE);
        }
    }

    @Override
    protected void noNetworkStatus() {

    }

    private void initAdapter() {

        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity>(this, R.layout
                .item_appointment, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderDetailBean.ListEntity listEntity) {
                ItemAppointmentLayout itemAppointmentLayout = (ItemAppointmentLayout)
                        viewDataBinding;
                String label = listEntity.getFault_lable();
                LinearLayout linearLayout = itemAppointmentLayout.llytLabel;
                orderModel.addLabel(linearLayout, label);
                itemAppointmentLayout.sdvContactItemImage.setImageURI(Uri.parse(listEntity
                        .getProduct_thumb()));
                itemAppointmentLayout.setAppointment(listEntity);
            }
        };
    }

    @Override
    protected int getContentView() {
        return R.layout.aty_appointment_client;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_client_appointment_choose_result:
                wheelPopupWindow.setOptions(WheelOptions.STANDER);
                wheelPopupWindow.setPopupTitle(getString(R.string.choose_result));
                wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array
                        .appointment_results));
                wheelPopupWindow.setOnWheelOptsSelectCallback(this);
                wheelPopupWindow.showAtLocation(appointmentClientLayout.getRoot(), Gravity
                        .BOTTOM, 0, 0);
                break;
            case R.id.btn_client_appointment_submit:
                if (checkParams()) {
                    networkModel.appointOrder(orderId, result, appointmentTime, reMark,
                            NetworkParams.DONUT);
                }
                break;
            case R.id.flyt_appointment_time:
                wheelPopupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
                wheelPopupWindow.setPopupTitle(getString(R.string.choose_time));
                wheelPopupWindow.showAtLocation(appointmentClientLayout.getRoot(), Gravity
                        .BOTTOM, 0, 0);
                break;
            case R.id.rylt_appointment_remark:
                inputDialog = new InputDialog(this);
                inputDialog.setInputCallBack(this);
                inputDialog.setTitle(getString(R.string.note));
                inputDialog.setParams(NetworkParams.CUPCAKE);
                inputDialog.show();
                break;
            case R.id.tv_client_appointment_client_address:
                MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.setTitle(getString(R.string.address));
                messageDialog.setMessage(appointmentClientLayout.getAppointment().getAddress());
                messageDialog.show();
                break;
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean o, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(o, paramsCode);
        if ((o instanceof OrderDetailBean) && paramsCode == NetworkParams.CUPCAKE) {//means get
            // data when activity is active
            OrderDetailBean orderDetailBean = (OrderDetailBean) o;
            appointmentClientLayout.setAppointment(orderDetailBean.getData());

            list.clear();
            list.addAll(orderDetailBean.getList());
            commonBinderAdapter.notifyDataSetChanged();
        } else if (paramsCode == NetworkParams.DONUT) {//means submit
            // the data to server
            start(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent
                    .FLAG_ACTIVITY_SINGLE_TOP);
            AppTools.sendBroadcast(new Bundle(), AppKeyMap.APPOINTMENT_CLIENT_ACTION);
        }
    }


    private boolean checkParams() {
        if (result.isEmpty()) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_choose_result)
            );
            return false;
        }
        if (TextUtils.equals(result, "1")) {
            if (appointmentTime.isEmpty()) {
                AppTools.showNormalSnackBar(parentView, getString(R.string
                        .please_choose_appointment_time));
                return false;
            }
        }
        reMark = tvRemark.getText().toString();
        return true;
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        switch (wheelOptions) {
            case STANDER://means choose appointment result
                this.result = (index + 1) + "";
                tvAppointmentResult.setText(selectData);
                if (index == 0) {
                    flytAppointmentTime.setVisibility(View.VISIBLE);
                    ryltAppointmentRemark.setVisibility(View.VISIBLE);
                } else {
                    flytAppointmentTime.setVisibility(View.GONE);
                    ryltAppointmentRemark.setVisibility(View.VISIBLE);
                    appointmentTime = "";
                    tvAppointmentTime.setText("");
                }
                break;
            case DATE_WITH_TIME:
                tvAppointmentTime.setText(selectData);
                this.appointmentTime = selectData;
                break;
        }
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        this.tvRemark.setText(word);//set the remark into right position
        appointmentClientLayout.scrollView.fullScroll(View.FOCUS_DOWN);//set scroll view scroll
        // to bottom
    }
}
