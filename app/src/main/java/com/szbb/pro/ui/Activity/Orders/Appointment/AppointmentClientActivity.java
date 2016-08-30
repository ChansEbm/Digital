package com.szbb.pro.ui.activity.orders.appointment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.AppointmentClientLayout;
import com.szbb.pro.ItemAppointmentLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.biz.OrderBiz;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.PermissionTools;
import com.szbb.pro.ui.activity.locate.TagLocationActivity;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.orders.operating.CustomerActivity;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import org.solovyev.android.views.llm.LinearLayoutManager;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by ChanZeeBm on 2015/10/26.
 * 预约客户
 */
public class AppointmentClientActivity
        extends BaseAty<BaseBean, OrderDetailBean.ListEntity>
        implements OnWheelOptsSelectCallback,
        InputCallBack {
    private AppointmentClientLayout appointmentClientLayout;
    private RecyclerView recyclerView;
    private OrderBiz orderModel;
    private String orderId = "";
    private WheelPopupWindow wheelPopupWindow;
    private FrameLayout flytAppointmentTime;
    private RelativeLayout ryltAppointmentRemark;
    private TextView tvRemark;
    private TextView tvAppointmentTime;
    private TextView tvAppointmentResult;

    private String result = "";
    private String appointmentTime = "";
    private String reMark = "";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentClientLayout = (AppointmentClientLayout) viewDataBinding;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_service,
                                  menu);
        final View actionView = menu.findItem(R.id.custom_service)
                                    .getActionView();
        BGABadgeTextView textView = (BGABadgeTextView) actionView.findViewById(R.id.textView);
        String unread = getIntent().getStringExtra("unread");
        if (!TextUtils.equals(unread,
                              "0")) {
            textView.showTextBadge(unread);
        } else {
            textView.hiddenBadge();
        }
        textView.setOnClickListener(this);
        titleBarTools.getToolbar()
                     .setPadding(0,
                                 0,
                                 15,
                                 0);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        return true;
    }

    @Override
    protected void initViews () {
        titleBarTools(this).defaultToolBar(this)
                           .setTitle(R.string.title_client_appointment);
        recyclerView = appointmentClientLayout.recyclerView;
        flytAppointmentTime = appointmentClientLayout.flytAppointmentTime;
        ryltAppointmentRemark = appointmentClientLayout.ryltAppointmentRemark;
        tvRemark = appointmentClientLayout.tvAppointmentRemark;
        tvAppointmentTime = appointmentClientLayout.tvAppointmentTime;
        tvAppointmentResult = appointmentClientLayout.tvAppointmentResult;

        orderModel = new OrderBiz(this);
        wheelPopupWindow = new WheelPopupWindow(this);
        initAdapter();
    }

    @Override
    protected void initEvents () {
        wheelPopupWindow.setOnWheelOptsSelectCallback(this);
        appointmentClientLayout.flytClientAppointmentChooseResult.setOnClickListener(this);
        appointmentClientLayout.btnClientAppointmentSubmit.setOnClickListener(this);
        appointmentClientLayout.ryltAppointmentRemark.setOnClickListener(this);
        flytAppointmentTime.setOnClickListener(this);
        ryltAppointmentRemark.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setAdapter(commonBinderAdapter);

        appointmentClientLayout.scrollView.smoothScrollTo(0,
                                                          0);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            networkModel.orderDetail(orderId,
                                     "",
                                     NetworkParams.CUPCAKE);
        }
    }

    @Override
    protected void noNetworkStatus () {

    }

    private void initAdapter () {

        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity>(this,
                                                                                  R.layout.item_appointment,
                                                                                  list) {

            @Override
            public void onBind (ViewDataBinding viewDataBinding,
                                CommonBinderHolder holder,
                                int
                                        position,
                                OrderDetailBean.ListEntity listEntity) {
                listEntity.setProductNum("产品" + (position + 1));
                ItemAppointmentLayout itemAppointmentLayout = (ItemAppointmentLayout)
                        viewDataBinding;
                String label = listEntity.getFault_lable();
                LinearLayout linearLayout = itemAppointmentLayout.llytLabel;
                orderModel.addLabel(linearLayout,
                                    label);
                itemAppointmentLayout.sdvContactItemImage.setImageURI(Uri.parse(listEntity
                                                                                        .getProduct_thumb()));
                itemAppointmentLayout.setAppointment(listEntity);
            }
        };
    }

    @Override
    protected int getContentView () {
        return R.layout.aty_appointment_client;
    }

    @Override
    protected void onClick (int id,
                            View view) {
        final OrderDetailBean.DataEntity appointment = appointmentClientLayout.getAppointment();
        switch (id) {
            case R.id.flyt_client_appointment_choose_result:
                appointmentResultAction();
                break;
            case R.id.btn_client_appointment_submit:
                appointSubmit();
                break;
            case R.id.flyt_appointment_time:
                appointTime();
                break;
            case R.id.rylt_appointment_remark:
                showInputDialog();
                break;
            case R.id.btn_engineer:
                contactEngineer(appointment);
                break;
            case R.id.btn_user:
                contactUser(appointment);
                break;
            case R.id.tv_location:
                locationActivityLogic(appointment);
                break;
            case R.id.textView://联系客服
                BGABadgeTextView btv = (BGABadgeTextView) view;
                btv.hiddenBadge();
                startActivity(new Intent(this,
                                         CustomerActivity.class).putExtra("orderId",
                                                                          orderId)
                                                                .putExtra("servicePhone",
                                                                          appointment
                                                                                  .getCustomer_service_phone())
                                                                .putExtra("identifier",
                                                                          appointment
                                                                                  .getCS_identifier()));
                break;
        }
    }

    private void locationActivityLogic (OrderDetailBean.DataEntity appointment) {
        final double lng = Double.parseDouble(appointment.getLng());
        final double lat = Double.parseDouble(appointment.getLat());
        String address = appointment.getAddress();
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.FROYO,
                                                 Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationActivity(lng,
                                  lat,
                                  address);
        } else {
            permissionMap.put("lng",
                              lng);
            permissionMap.put("lat",
                              lat);
            permissionMap.put("address",
                              address);
        }
    }

    private void startLocationActivity (double lng, double lat, String address) {
        startActivity(new Intent().putExtra("lat",
                                            lat)
                                  .putExtra("lng",
                                            lng)
                                  .putExtra
                                          ("address",
                                           address)
                                  .setClass(this,
                                            TagLocationActivity.class));
    }

    private void appointTime () {
        wheelPopupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
        wheelPopupWindow.setPopupTitle(getString(R.string.choose_time));
        wheelPopupWindow.showAtLocation(appointmentClientLayout.getRoot(),
                                        Gravity.BOTTOM,
                                        0,
                                        0);
    }

    private void appointSubmit () {
        if (checkParams()) {
            if (!result.equals("3")) {
                networkModel.appointOrder(orderId,
                                          result,
                                          appointmentTime,
                                          reMark,
                                          NetworkParams.DONUT);
            } else {
                recallDialog();
            }
        }
    }

    private void contactUser (
            OrderDetailBean.DataEntity appointment) {
        DialDialog dialDialog = new DialDialog(this,
                                               null);
        final String tel = appointment.getTel();
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.DONUT,
                                                 Manifest.permission.CALL_PHONE)) {
            dialDialog.call(tel);
        } else {
            permissionMap.put("user",
                              tel);
        }
    }

    private void contactEngineer (
            OrderDetailBean.DataEntity appointment) {
        DialDialog dialDialog = new DialDialog(this,
                                               null);
        final String factory_technology_tel = appointment
                .getFactory_technology_tel();
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.CUPCAKE,
                                                 Manifest.permission
                                                         .CALL_PHONE)) {

            dialDialog.call(factory_technology_tel);
        } else {
            permissionMap.put("engineer",
                              factory_technology_tel);
        }
    }

    private void showInputDialog () {
        InputDialog inputDialog = new InputDialog(this);
        inputDialog.setInputCallBack(this);
        inputDialog.setTitle(getString(R.string.note));
        inputDialog.setParams(NetworkParams.CUPCAKE);
        inputDialog.show();
    }

    private void appointmentResultAction () {
        wheelPopupWindow.setOptions(WheelOptions.STANDER);
        wheelPopupWindow.setPopupTitle(getString(R.string.choose_result));
        wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array
                                                                             .appointment_results));
        wheelPopupWindow.setOnWheelOptsSelectCallback(this);
        wheelPopupWindow.showAtLocation(appointmentClientLayout.getRoot(),
                                        Gravity
                                                .BOTTOM,
                                        0,
                                        0);
    }

    private void recallDialog () {
        final MessageDialog dialog = new MessageDialog(this);
        dialog.setMessage("工单将被退回，是否确认?")
              .setPositiveButton(getString(R.string
                                                   .positive),
                                 new View.OnClickListener() {
                                     @Override
                                     public void onClick (View v) {
                                         networkModel.appointOrder(orderId,
                                                                   result,
                                                                   appointmentTime,
                                                                   reMark,
                                                                   NetworkParams
                                                                           .DONUT);
                                     }
                                 })
              .setNegativeButton(getString(R.string.negative),
                                 new View
                                         .OnClickListener() {

                                     @Override
                                     public void onClick (View v) {
                                         dialog.dismiss();
                                     }
                                 })
              .show();
    }

    @Override
    public void onJsonObjectSuccess (BaseBean o,
                                     NetworkParams paramsCode) {
        super.onJsonObjectSuccess(o,
                                  paramsCode);
        if ((o instanceof OrderDetailBean) && paramsCode == NetworkParams.CUPCAKE) {//means get
            // data when activity is active
            OrderDetailBean orderDetailBean = (OrderDetailBean) o;
            appointmentClientLayout.setAppointment(orderDetailBean.getData());

            list.clear();
            list.addAll(orderDetailBean.getList());
            commonBinderAdapter.notifyDataSetChanged();
        } else if (paramsCode == NetworkParams.DONUT) {//means submit
            switch (result) {
                case "1":
                    Toast.makeText(AppointmentClientActivity.this,
                                   "预约记录提交中，请按时上门为用户服务",
                                   Toast
                                           .LENGTH_SHORT)
                         .show();
                    AppTools.sendBroadcast(new Bundle(),
                                           AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
                    break;
                case "2":
                    Toast.makeText(AppointmentClientActivity.this,
                                   "工单延时3小时收回，请尽快与用户联系，及时填写预约记录",
                                   Toast.LENGTH_SHORT)
                         .show();
                case "3":
                case "4":
                    AppTools.sendBroadcast(new Bundle(),
                                           AppKeyMap
                                                   .APPOINTMENT_CAN_NOT_CONTENT_CLIENT);
                    break;
            }
            start(MainActivity.class,
                  Intent.FLAG_ACTIVITY_CLEAR_TOP,
                  Intent
                          .FLAG_ACTIVITY_SINGLE_TOP);

        }
    }


    private boolean checkParams () {
        if (result.isEmpty()) {
            AppTools.showNormalSnackBar(parentView,
                                        getString(R.string.please_choose_result)
                                       );
            return false;
        }
        if (TextUtils.equals(result,
                             "1")) {
            if (appointmentTime.isEmpty()) {
                AppTools.showNormalSnackBar(parentView,
                                            getString(R.string
                                                              .please_choose_appointment_time));
                return false;
            }
        }
        reMark = tvRemark.getText()
                         .toString();
        return true;
    }

    @Override
    public void onWheelSelect (String selectData,
                               WheelOptions wheelOptions,
                               int index) {
        switch (wheelOptions) {
            case STANDER://means choose appointment result
                this.result = (index + 1) + "";
                tvAppointmentResult.setText(selectData);
                if (index == 0) {
                    flytAppointmentTime.setVisibility(View.VISIBLE);
                    ryltAppointmentRemark.setVisibility(View.VISIBLE);
                    appointmentClientLayout.scrollView.fullScroll(View.FOCUS_DOWN);
                    //预约成功弹出时间选择
                    WheelPopupWindow wheelPopupWindow = new WheelPopupWindow(this);
                    wheelPopupWindow.setOnWheelOptsSelectCallback(this);
                    wheelPopupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
                    wheelPopupWindow.setPopupTitle(getString(R.string.choose_time));
                    wheelPopupWindow.showAtLocation(appointmentClientLayout.getRoot(),
                                                    Gravity.BOTTOM,
                                                    0,
                                                    0);
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
    public void inputWord (String word,
                           NetworkParams networkParams) {
        this.tvRemark.setText(word);//set the remark into right position
        appointmentClientLayout.scrollView.fullScroll(View.FOCUS_DOWN);//set scroll view scroll
        // to bottom
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case AppKeyMap.CUPCAKE://contact engineer
                    String engineer = (String) permissionMap.get("engineer");
                    if (!TextUtils.isEmpty(engineer)) {
                        AppTools.CALL(engineer);
                    }
                    break;
                case AppKeyMap.DONUT://contact user
                    String user = (String) permissionMap.get("user");
                    if (!TextUtils.isEmpty(user)) {
                        AppTools.CALL(user);
                    }
                    break;
                case AppKeyMap.FROYO://open location
                    double lat = (double) permissionMap.get("lat");
                    double lng = (double) permissionMap.get("lng");
                    String address = (String) permissionMap.get("address");
                    if (lat != 0.0d && lng != 0.0d && !TextUtils.isEmpty(address)) {
                        startLocationActivity(lng,
                                              lat,
                                              address);
                    }
                    break;
            }
        }
    }
}
