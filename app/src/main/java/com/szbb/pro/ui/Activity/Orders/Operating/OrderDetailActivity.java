package com.szbb.pro.ui.activity.orders.operating;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.jungly.gridpasswordview.GridPasswordView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemOrderDetailLayout;
import com.szbb.pro.OrderDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.ItemOrderDetailGuideLayout;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.AlterPopupOpts;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnAlterPopupWindowOptsClickListener;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.biz.OrderBiz;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.PermissionTools;
import com.szbb.pro.tools.ViewUtils;
import com.szbb.pro.ui.activity.expenses.ExpensesDetailActivity;
import com.szbb.pro.ui.activity.expenses.ExpensesResultActivity;
import com.szbb.pro.ui.activity.locate.TagLocationActivity;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentAlterActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentHistoryActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentReturnActivity;
import com.szbb.pro.widget.PopupWindow.AlterPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by ChanZeeBm on 2016/1/11.
 * 工单详情
 */
public class OrderDetailActivity
        extends BaseAty<BaseBean, OrderDetailBean.ListEntity>
        implements
        BDLocationListener,
        OnAlterPopupWindowOptsClickListener, OnWheelMultiOptsCallback {
    private RecyclerView recyclerView;
    private RecyclerView guideRecyclerView;
    private OrderDetailLayout orderDetailLayout;

    private Button btnSignAppointment;//预约签到按钮
    private Button btnSignAgain;//再次签到按钮
    private GridPasswordView gridPasswordView;//输入服务码控件
    private AlterPopupWindow alterPopupWindow;
    private WheelPopupWindow wheelPopupWindow;

    private String orderId = "";

    private LinearLayoutManager linearLayoutManager;
    private CommonBinderAdapter<OrderDetailBean.DataEntity.AcceCostListEntity> guideAdapter;
    private List<OrderDetailBean.DataEntity.AcceCostListEntity> guideList = new ArrayList<>();
    private BGABadgeTextView bgaBadgeTextView;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailLayout = (OrderDetailLayout) viewDataBinding;
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
        }
        AppTools.registerBroadcast(new RefreshDataBroadcast(),
                                   AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
    }

    @Override
    protected void initViews () {
        defaultTitleBar(this).setTitle(R.string.order_detail_title);
        wheelPopupWindow = new WheelPopupWindow(this);
        linearLayoutManager = new LinearLayoutManager(this);
        alterPopupWindow = new AlterPopupWindow(this);
        gridPasswordView = orderDetailLayout.gridPasswordView;
        btnSignAppointment = orderDetailLayout.btnSignAppointment;
        btnSignAgain = orderDetailLayout.btnAppointmentAgain;
        recyclerView = orderDetailLayout.recyclerView;
        guideRecyclerView = orderDetailLayout.recyclerViewGuide;
        setAdapter();
        guideAdapter = new CommonBinderAdapter<OrderDetailBean.DataEntity.AcceCostListEntity>
                (this,
                 R.layout.item_order_detail_guide,
                 guideList) {

            @Override
            public void onBind (ViewDataBinding viewDataBinding,
                                CommonBinderHolder holder,
                                int position,
                                OrderDetailBean.DataEntity.AcceCostListEntity acceCostListEntity) {
                ItemOrderDetailGuideLayout layout = (ItemOrderDetailGuideLayout) viewDataBinding;
                layout.setGuide(acceCostListEntity);
            }
        };
    }

    private void setAdapter () {
        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity>(this,
                                                                                  R.layout.item_order_detail,
                                                                                  list) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding,
                                CommonBinderHolder holder,
                                int position,
                                OrderDetailBean.ListEntity listEntity) {
                listEntity.setProductNum("产品:" + (position + 1));
                listEntity.setServicePage(AppKeyMap.CUPCAKE);//标记服务项目显示在工单详情页
                ItemOrderDetailLayout itemOrderDetail = (ItemOrderDetailLayout) viewDataBinding;
                itemOrderDetail.sdvContactItemImage.setImageURI(Uri.parse(listEntity
                                                                                  .getProduct_thumb()));
                new OrderBiz(OrderDetailActivity.this).displayFinishSign(itemOrderDetail,
                                                                         listEntity);
                itemOrderDetail.setDetail(listEntity);
            }
        };
        commonBinderAdapter.setBinderOnItemClickListener(this);
    }

    @Override
    protected void initEvents () {
        btnSignAppointment.setOnClickListener(this);
        alterPopupWindow.setOnAlterPopupWindowOptsClickListener(this);
        wheelPopupWindow.setOnWheelMultiOptsCallback(this);
        guideAdapter.setBinderOnItemClickListener(this);

        initRecyclerView();
        progressNewData();

    }

    private void initRecyclerView () {
        guideRecyclerView.setAdapter(guideAdapter);
        guideRecyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        guideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                                               .colorResId(R.color.color_bg_gravy)
                                               .sizeResId(R.dimen.large_margin_15dp)
                                               .build());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commonBinderAdapter);
    }

    @Override
    protected void noNetworkStatus () {
        ViewUtils.endCountDown();
    }

    @Override
    protected void onStop () {
        super.onStop();
        ViewUtils.endCountDown();
    }

    @Override
    protected int getContentView () {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void onClick (int id,
                            View view) {
        final OrderDetailBean.DataEntity detail = orderDetailLayout.getDetail();
        if (detail == null) {
            return;
        }
        String orderId = detail.getOrderid();
        switch (id) {
            case R.id.btn_sign_appointment://签到
                if (AppTools.isNetworkConnected()) {
                    AppTools.showNormalSnackBar(parentView,
                                                getString(R.string.main_locating));
                    AppTools.locate(this);
                } else {
                    AppTools.showSettingSnackBar(parentView,
                                                 getString(R.string
                                                                   .no_network_is_detected));
                }
                break;
            case R.id.btn_edit_appointment://修改预约
                alterPopupWindow.showAtLocation(parentView,
                                                Gravity.BOTTOM,
                                                0,
                                                0);
                break;
            case R.id.btn_appointment_again://再次预约
                appointAgain();
                break;
            case R.id.btn_confirm_done://完成工单
                completeAcce();
                break;
            case R.id.btn_engineer:
                contactEngineer(detail);
                break;
            case R.id.btn_user:
                contactUser(detail);
                break;
            case R.id.tv_location:
                locationActivityLogic(detail);
                break;
            case R.id.btn_track:
                startActivity(new Intent().putExtra("orderId",
                                                    orderId)
                                          .setClass(this,
                                                    OrderTrackingActivity.class));
                break;
            case R.id.textView://联系客服

                contactService(orderId);
                BGABadgeTextView bt = (BGABadgeTextView) view;
                bt.hiddenBadge();
                break;
            case R.id.btn_expenses:
                startActivity(new Intent(this,
                                         ExpensesDetailActivity.class).putExtra("orderId",
                                                                                orderId));
                break;
            case R.id.btn_msg://短信转发
                if (PermissionTools.alreadyHasPermission(this,
                                                         AppKeyMap.GINGERBREAD,
                                                         Manifest
                                                                 .permission.SEND_SMS)) {
                    smsForword();
                }
                break;
        }
    }

    private void smsForword () {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                                   Uri.parse("smsto:"));
        intent.putExtra("sms_body",
                        orderDetailLayout
                                .getDetail()
                                .getSms_template());
        startActivity(intent);
    }

    private void contactService (String orderId) {
        String servicePhone = orderDetailLayout
                .getDetail()
                .getCustomer_service_phone();
        String cs_identifier = orderDetailLayout.getDetail()
                                                .getCS_identifier();
        startActivity(new Intent(this,
                                 CustomerActivity.class).putExtra("orderId",
                                                                  orderId)
                                                        .putExtra("servicePhone",
                                                                  servicePhone)
                                                        .putExtra("identifier",
                                                                  cs_identifier));

//        startActivity(new Intent(this, CustomerActivity.class));
    }


    //再次预约
    private void appointAgain () {
        WheelPopupWindow popupWindow = new WheelPopupWindow(this);
        popupWindow.setParams(AppKeyMap.FROYO);
        popupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
        popupWindow.setOnWheelMultiOptsCallback(this);
        popupWindow.showAtDefaultLocation();
    }

    //联系用户
    private void contactUser (OrderDetailBean.DataEntity detail) {
        final String tel = detail.getTel();
        DialDialog dialDialog = new DialDialog(this,
                                               null);
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.DONUT,
                                                 Manifest.permission.CALL_PHONE)) {
            dialDialog.call(tel);
        } else {
            permissionMap.put("user",
                              tel);
        }
    }

    //联系厂家
    private void contactEngineer (OrderDetailBean.DataEntity detail) {
        final String factory_technology_tel = detail.getFactory_technology_tel();
        DialDialog dialDialog = new DialDialog(this,
                                               null);
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.CUPCAKE,
                                                 Manifest.permission.CALL_PHONE)) {
            dialDialog.call(factory_technology_tel);
        } else {
            permissionMap.put("engineer",
                              factory_technology_tel);
        }
    }

    //打开百度地图逻辑
    private void locationActivityLogic (OrderDetailBean.DataEntity detail) {
        final double lat = Double.parseDouble(detail.getLat());
        final double lng = Double.parseDouble(detail.getLng());
        String address = detail.getAddress();
        if (PermissionTools.alreadyHasPermission(this,
                                                 AppKeyMap.FROYO,
                                                 Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationActivity(lat,
                                  lng,
                                  address);
        } else {
            permissionMap.put("lat",
                              lat);
            permissionMap.put("lng",
                              lng);
            permissionMap.put("address",
                              address);
        }
    }

    private void startLocationActivity (double lat, double lng, String address) {
        startActivity(new Intent()
                              .putExtra("flag",
                                        AppKeyMap.DONUT)
                              .putExtra("lat",
                                        lat)
                              .putExtra("lng",
                                        lng)
                              .putExtra("title",
                                        "用户所在地址")
                              .putExtra("address",
                                        address)
                              .setClass(this,
                                        TagLocationActivity.class));
    }

    private void completeAcce () {
        String pwd = gridPasswordView.getPassWord();
        if (TextUtils.isEmpty(pwd)) {
            AppTools.showNormalSnackBar(parentView,
                                        getString(R.string
                                                          .please_input_service_code));
        } else {
            networkModel.completeOrder(orderId,
                                       gridPasswordView.getPassWord(),
                                       NetworkParams.ICECREAMSANDWICH);
        }
    }

    @Override
    protected void onPause () {
        super.onPause();
        AppTools.stopLocate();
    }

    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent(intent);
        progressNewData();
    }

    @Override
    public void onBinderItemClick (View view,
                                   int pos) {
        if (view
                .getParent()
                .equals(guideRecyclerView)) {
            guideLogic(pos);
        } else if (view
                .getParent()
                .equals(recyclerView)) {
            dealLogic(pos);
        }
    }

    private void dealLogic (int pos) {
        OrderDetailBean.ListEntity listEntity = list.get(pos);
        listEntity.setServicePage(AppKeyMap.FROYO);//标记为服务项目显示在工单操作页
        startActivity(new Intent(this,
                                 DealProductActivity.class)
                              .putExtra("orderId",
                                        orderId)
                              .putExtra("product",
                                        listEntity));
    }

    private void guideLogic (int pos) {
        final OrderDetailBean.DataEntity.AcceCostListEntity acceCostListEntity = guideList
                .get(pos);

        Class<?> cls = acceCostListEntity.getIntentFlag();
        String acid = acceCostListEntity.getAcid();
        Intent intent = new Intent();
        if (cls.equals(ExpensesResultActivity.class)) {
            intent.putExtra("costId",
                            acid);
        } else {
            intent.putExtra("acceId",
                            acid);
            intent.putExtra("flag",
                            AppKeyMap.CUPCAKE);//代表如果步骤走到配件回寄的时候,提交完毕直接返回到本Activity
        }
        startActivity(intent.setClass(this,
                                      cls));
    }


    @Override
    public void onJsonObjectSuccess (BaseBean o,
                                     NetworkParams paramsCode) {
        //首次进来获取数据
        if (paramsCode == NetworkParams.CUPCAKE) {//获取全局数据
            OrderDetailBean orderDetailBean = (OrderDetailBean) o;
            final OrderDetailBean.DataEntity data = orderDetailBean.getData();
            fillProductList(orderDetailBean,
                            data);
            fillGuideList(data);
            orderDetailLayout.setDetail(data);
        } else if (paramsCode == NetworkParams.DONUT) {//预约签到的返回
            AppTools.showNormalSnackBar(parentView,
                                        getString(R.string
                                                          .order_detail_already_sign_in));
            networkModel.orderDetail(orderId,
                                     "1",
                                     NetworkParams.CUPCAKE);
        } else if (paramsCode == NetworkParams.ICECREAMSANDWICH) {
            orderDetailLayout.llytEvaluate.setVisibility(View.GONE);
            orderDetailLayout.llytEvaluation.setVisibility(View.VISIBLE);
            AppTools.sendBroadcast(null,
                                   AppKeyMap.WAITING_COST_ACTION);
            Intent intent = new Intent()
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                            .FLAG_ACTIVITY_SINGLE_TOP)
                    .setClass(this,
                              MainActivity.class);
            startActivity(intent);
            AppTools.sendBroadcast(new Bundle(),
                                   AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);//意在刷新
        } else if (paramsCode == NetworkParams.LOLLIPOP) {
            start(MainActivity.class);
            AppTools.sendBroadcast(new Bundle(),
                                   AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);//意在刷新
        } else if (paramsCode == NetworkParams.HONEYCOMB) {//未读清除成功
            AppTools.sendBroadcast(new Bundle(),
                                   AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);//意在刷新
        }
    }

    private void fillGuideList (OrderDetailBean.DataEntity data) {
        guideList.clear();
        guideList.addAll(data.getAcce_cost_list());
        guideAdapter.notifyDataSetChanged();
    }

    private void fillProductList (OrderDetailBean orderDetailBean,
                                  OrderDetailBean.DataEntity data) {
        boolean isSignIn = data.isSignIn();//是否已签到
        boolean isAllRepair = data.isAllRepair();//是否全部产品维修完毕
        //该工单是否全部完毕,并且已完成提交
        boolean isAllComplete = data.getIs_submit_complete();
        //如果已经完结工单,则隐藏菜单栏上的bgaTextView
        if (bgaBadgeTextView != null) {
            bgaBadgeTextView.setVisibility(isAllComplete ? View.GONE : View.VISIBLE);
        }
        List<OrderDetailBean.ListEntity> listEntities = orderDetailBean.getList();
        boolean isTimeOut = data.getIs_sign_in()
                                .equals("3") || data.getIs_sign_in()
                                                    .equals("2");
        changeButtonState(isSignIn,
                          isAllRepair,
                          isAllComplete,
                          isTimeOut);
        notifyAdapter(listEntities);
    }

    /**
     * 根据签到情况改变按钮状态
     *
     * @param isSignIn is sign in or not
     */
    private void changeButtonState (boolean isSignIn,
                                    boolean isAllRepair,
                                    boolean isAllComplete,
                                    boolean isTimeOut) {
        btnSignAgain.setVisibility(isSignIn ? View.VISIBLE : View.GONE);
        if (isTimeOut) {
            btnSignAgain.setVisibility(View.VISIBLE);
            orderDetailLayout.btnEditAppointment.setVisibility(View.GONE);
        }
        if (isAllRepair) {
            btnSignAgain.setVisibility(View.GONE);
            orderDetailLayout.btnEditAppointment.setVisibility(View.GONE);
            orderDetailLayout.llytEvaluate.setVisibility(View.VISIBLE);
        }

        if (isAllComplete) {
            orderDetailLayout.llytEvaluate.setVisibility(View.GONE);
            orderDetailLayout.llytEvaluation.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 填充数据集
     *
     * @param listEntities source data
     */
    private void notifyAdapter (List<OrderDetailBean.ListEntity> listEntities) {
        this.list.clear();
        this.list.addAll(listEntities);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiveLocation (BDLocation bdLocation) {
        //定位成功后,执行预约签到
        if (bdLocation != null) {
            networkModel.signAppoint(orderId,
                                     bdLocation.getLatitude(),
                                     bdLocation.getLongitude(),
                                     NetworkParams.DONUT);
        }
    }

    @Override
    public void onOptsClick (AlterPopupOpts opts) {
        Intent intent = new Intent().putExtra("orderId",
                                              orderId);
        switch (opts) {
            case ALTER:
                startActivity(intent.setClass(this,
                                              AppointmentAlterActivity.class));
                break;
            case HISTORY:
                startActivity(intent.setClass(this,
                                              AppointmentHistoryActivity.class));
                break;
            case RETURN:
                startActivity(intent.setClass(this,
                                              AppointmentReturnActivity.class));
                break;
        }
    }

    private void progressNewData () {
        networkModel.orderDetail(orderId,
                                 "1",
                                 NetworkParams.CUPCAKE);
    }


    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_service,
                                  menu);
        final View actionView = menu
                .findItem(R.id.custom_service)
                .getActionView();
        bgaBadgeTextView = (BGABadgeTextView) actionView
                .findViewById(R.id.textView);

        String unread = getIntent().getStringExtra("unread");
        if (!(TextUtils.isEmpty(unread) || TextUtils.equals("0",
                                                            unread))) {
            bgaBadgeTextView.showTextBadge(unread);
        }
        bgaBadgeTextView.setOnClickListener(this);
        titleBarTools.getToolbar()
                     .setPadding(0,
                                 0,
                                 15,
                                 0);
        return true;
    }

    @Override
    public void onWheelSelect (String selectData,
                               WheelOptions wheelOptions,
                               int params,
                               int index) {
        //再次签到
        if (params == AppKeyMap.FROYO) {
            networkModel.appointAgain(orderId,
                                      selectData,
                                      NetworkParams.LOLLIPOP);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                                         permissions,
                                         grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case AppKeyMap.CUPCAKE://联系厂家
                    String engineer = (String) permissionMap.get("engineer");
                    if (!TextUtils.isEmpty(engineer)) {
                        AppTools.CALL(engineer);
                    }
                    break;
                case AppKeyMap.DONUT://联系用户
                    String user = (String) permissionMap.get("user");
                    if (!TextUtils.isEmpty(user)) {
                        AppTools.CALL(user);
                    }
                    break;
                case AppKeyMap.FROYO://地址
                    double lat = (double) permissionMap.get("lat");
                    double lng = (double) permissionMap.get("lng");
                    String address = (String) permissionMap.get("address");
                    if (lat != 0.0d && lng != 0.0d && !TextUtils.isEmpty(address)) {
                        startLocationActivity(lat,
                                              lng,
                                              address);
                    }
                    break;
                case AppKeyMap.GINGERBREAD://短信转发
                    smsForword();
                    break;

            }
        }
    }

    /**
     * scroll to special position
     */
//    private void scrollToSpecialPosition() {
//        final View childAt = linearLayoutManager.getChildAt(servicePos);
//        int[] location = new int[2];
//        childAt.getLocationInWindow(location);
//        final int offSet = orderDetailLayout.scrollView.getScrollY() + location[1] -
//                titleBarTools.getToolBarHeight() - AppTools.getStatusBarHeight();
//        orderDetailLayout.scrollView.postDelayed(new Runnable() {
//                                                     @Override
//                                                     public void run() {
//                                                         orderDetailLayout.scrollView
//                                                                 .smoothScrollTo(0,
//                                                                                 offSet);
//                                                     }
//                                                 },
//                                                 300);
//    }

    public class RefreshDataBroadcast
            extends BroadcastReceiver {

        @Override
        public void onReceive (Context context,
                               Intent intent) {
            if (intent.getAction()
                      .equals(AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE)) {
                progressNewData();
            }
        }
    }

}
