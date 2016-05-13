package com.szbb.pro.ui.activity.orders.operating;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.order.OrderDetailBean;
import com.szbb.pro.eum.AlterPopupOpts;
import com.szbb.pro.eum.ButtonType;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnAlterPopupWindowOptsClickListener;
import com.szbb.pro.impl.OnErrorProductCallback;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.model.OrderModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ViewUtils;
import com.szbb.pro.ui.activity.expenses.ExpensesApplyActivity;
import com.szbb.pro.ui.activity.expenses.ExpensesDetailActivity;
import com.szbb.pro.ui.activity.expenses.ExpensesResultActivity;
import com.szbb.pro.ui.activity.locate.TagLocationActivity;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentAlterActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentHistoryActivity;
import com.szbb.pro.ui.activity.orders.appointment.AppointmentReturnActivity;
import com.szbb.pro.widget.PopupWindow.AlterPopupWindow;
import com.szbb.pro.widget.PopupWindow.ErrorProductPopupWindow;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ChanZeeBm on 2016/1/11.
 * 工单详情
 */
public class OrderDetailActivity extends BaseAty<BaseBean, OrderDetailBean.ListEntity> implements
        BDLocationListener,
        OnAlterPopupWindowOptsClickListener, OnWheelMultiOptsCallback,
        OnPhotoOptsSelectListener, OnAddPictureDoneListener, InputCallBack, OnErrorProductCallback {
    private RecyclerView recyclerView;
    private RecyclerView guideRecyclerView;
    private OrderDetailLayout orderDetailLayout;

    private Button btnSignAppointment;//预约签到按钮
    private Button btnSignAgain;//再次签到按钮
    private GridPasswordView gridPasswordView;//输入服务码控件
    private AlterPopupWindow alterPopupWindow;
    private PhotoPopupWindow photoPopupWindow;
    private WheelPopupWindow wheelPopupWindow;

    private String orderId = "";
    private OrderModel orderModel;
    private int servicePos = -1;//点击了本次服务结果后保存点击了的item的位置
    private int picPos = -1;//点击了添加图片后保存点击了的item的位置
    private int reportPos = -1;//点击了备注后保存item位置
    private int serviceObjPos = -1;

    private ArrayList<String> picArr = new ArrayList<>();//保存添加的图片
    private LinearLayoutManager linearLayoutManager;

    private CommonBinderAdapter<OrderDetailBean.DataEntity.AcceCostListEntity> guideAdapter;
    private List<OrderDetailBean.DataEntity.AcceCostListEntity> guideList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailLayout = (OrderDetailLayout) viewDataBinding;
        if (getIntent() != null)
            orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.order_detail_title);
        wheelPopupWindow = new WheelPopupWindow(this);
        photoPopupWindow = new PhotoPopupWindow(this);
        orderModel = new OrderModel(this);
        linearLayoutManager = new LinearLayoutManager(this);
        alterPopupWindow = new AlterPopupWindow(this);
        gridPasswordView = orderDetailLayout.gridPasswordView;
        btnSignAppointment = orderDetailLayout.btnSignAppointment;
        btnSignAgain = orderDetailLayout.btnAppointmentAgain;
        recyclerView = orderDetailLayout.recyclerView;
        guideRecyclerView = orderDetailLayout.recyclerViewGuide;

        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity>(this, R.layout
                .item_order_detail, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderDetailBean.ListEntity listEntity) {
                listEntity.setProductNum("产品:" + (position + 1));
                ItemOrderDetailLayout itemOrderDetail = (ItemOrderDetailLayout) viewDataBinding;
                String faultLabel = listEntity.getFault_lable();//故障标签
                LinearLayout linearLayout = itemOrderDetail.llytLabel;
                orderModel.addLabel(linearLayout, faultLabel);
                itemOrderDetail.sdvContactItemImage.setImageURI(Uri.parse(listEntity
                        .getProduct_thumb()));

                //根据上次服务结果选择隐藏或者显示某些项
                if (orderModel.changeStatusByLastService(itemOrderDetail, listEntity)) {
                    //如果是已完单 则添加后台图片到图片放置处
                    orderModel.addPic(listEntity.getComplete_photos(), itemOrderDetail
                            .llytUploadPic);
                } else {
                    //如果上次还没完单,则控制可以手动添加照片
                    orderModel.addPicInHandler(listEntity.getAddPics(), itemOrderDetail
                            .llytUploadPic, position, OrderDetailActivity.this);
                }
                //根据本次服务结果选择隐藏或者显示某些项
                orderModel.changeStatusByService(itemOrderDetail, listEntity, position,
                        OrderDetailActivity.this);
                //初始化事件
                orderModel.setOrderEvents(itemOrderDetail, position, listEntity,
                        OrderDetailActivity.this);

                ((ItemOrderDetailLayout) viewDataBinding).setDetail(listEntity);
            }
        };

        guideAdapter = new CommonBinderAdapter<OrderDetailBean.DataEntity.AcceCostListEntity>
                (this, R.layout.item_order_detail_guide, guideList) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderDetailBean.DataEntity.AcceCostListEntity acceCostListEntity) {
                ItemOrderDetailGuideLayout layout = (ItemOrderDetailGuideLayout) viewDataBinding;
                layout.setGuide(acceCostListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        btnSignAppointment.setOnClickListener(this);
        alterPopupWindow.setOnAlterPopupWindowOptsClickListener(this);
        photoPopupWindow.setOnPhotoOptsSelectListener(this);
        wheelPopupWindow.setOnWheelMultiOptsCallback(this);
        guideAdapter.setBinderOnItemClickListener(this);

        initRecyclerView();
        progressNewData();
    }

    private void initRecyclerView() {
        guideRecyclerView.setAdapter(guideAdapter);
        guideRecyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        guideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R
                        .color.color_bg_gravy).sizeResId(R.dimen.large_margin_15dp).build());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commonBinderAdapter);
    }

    @Override
    protected void noNetworkStatus() {
        ViewUtils.endCountDown();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewUtils.endCountDown();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_detail;
    }

    private ErrorProductPopupWindow errorProductPopupWindow;//产品不对对话框

    @Override
    protected void onClick(int id, View view) {
        final OrderDetailBean.DataEntity detail = orderDetailLayout.getDetail();
        if (detail == null)
            return;
        String orderId = detail.getOrderid();
        switch (id) {
            case R.id.tv_error_product://产品错误报告
                int errorPos = (int) view.getTag();
                OrderDetailBean.ListEntity errorListEntity = list.get
                        (errorPos);
                if (orderModel.isReporting(this, errorListEntity.getLast_handle_type(),
                        errorListEntity.getLast_handle_status())) {
                    return;
                }
                String detailId = errorListEntity.getDetailid();
                errorProductPopupWindow = new ErrorProductPopupWindow(this);
                errorProductPopupWindow.setDetailId(detailId);
                errorProductPopupWindow.setOnErrorProductCallback(this);
                errorProductPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_sign_appointment://签到
                if (AppTools.isNetworkConnected()) {
                    AppTools.showNormalSnackBar(parentView, getString(R.string.main_locating));
                    AppTools.locate(this);
                } else {
                    AppTools.showSettingSnackBar(parentView, getString(R.string
                            .no_network_is_detected));
                }
                break;
            case R.id.btn_edit_appointment://修改预约
                alterPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_appointment_again://再次预约
                WheelPopupWindow popupWindow = new WheelPopupWindow(this);
                popupWindow.setParams(AppKeyMap.FROYO);
                popupWindow.setOptions(WheelOptions.DATE_WITH_TIME);
                popupWindow.setOnWheelMultiOptsCallback(this);
                popupWindow.showAtDefaultLocation();
                break;
            case R.id.flyt_service_obj://服务项目
                serviceObjPos = (int) view.getTag();
                OrderDetailBean.ListEntity listEntity = list.get
                        (serviceObjPos);
                if (!orderModel.serviceObjWindow(this, listEntity)) {
                    String serviceName = listEntity.getService_name();
                    String serviceDesc = listEntity.getService_desc();
                    final MessageDialog dialog = new MessageDialog(this);
                    dialog.setTitle(serviceName).setMessage(serviceDesc).setPositiveButton(getString(R.string.positive), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }).show();
//                    );

                }
                break;
            case R.id.flyt_service_result://服务结果
                servicePos = (int) view.getTag();
                OrderDetailBean.ListEntity listEntity1 = list.get(servicePos);
                if (orderModel.isReporting(this, listEntity1.getLast_handle_type(), listEntity1
                        .getLast_handle_status())) {
                    return;
                }
                orderModel.thisServiceType(this, this).showAtLocation(parentView, Gravity.BOTTOM,
                        0, 0);
                break;
            case R.id.btn_add_pic://添加图片
                picPos = (int) view.getTag();
                this.picArr = new ArrayList<>();
                this.picArr = (ArrayList<String>) (list.get(picPos))
                        .getAddPics();
                photoPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.llyt_report://填写备注
                reportPos = (int) view.getTag();
                InputDialog inputDialog = new InputDialog(this);
                inputDialog.setInputCallBack(this);
                inputDialog.setTitle(getString(R.string.note));
                inputDialog.setParams(NetworkParams.CUPCAKE);
//                OrderDetailBean.ListEntity reportEntity = list.get
//                        (reportPos);
                inputDialog.show();
                break;
            case R.id.button://每个产品的按钮
                progressButtonEvent(view);
                break;
            case R.id.btn_resend_code://重发验证码
                networkModel.getServiceCode(orderId, NetworkParams.HONEYCOMB);
                ViewUtils.startCountDown(orderDetailLayout.btnResendCode, getString(R.string.reg_verification_code_again), getString(R.string
                        .reg_reCode), 60000);
                break;
            case R.id.btn_confirm_done://完成工单
                completeAcce();
                break;
            case R.id.btn_expenses://费用详情
                startActivity(new Intent().setClass(this, ExpensesDetailActivity.class).putExtra
                        ("orderId", orderId));
                break;
            case R.id.btn_engineer:
                final String factory_technology_tel = detail.getFactory_technology_tel();
                DialDialog dialDialog = new DialDialog(this, null);
                dialDialog.call(factory_technology_tel);
                break;
            case R.id.btn_user:
                final String tel = detail.getTel();
                dialDialog = new DialDialog(this, null);
                dialDialog.call(tel);
                break;
            case R.id.tv_location:
                final double lat = Double.parseDouble(detail.getLat());
                final double lng = Double.parseDouble(detail.getLng());
                String address = detail.getAddress();
                startActivity(new Intent().putExtra("flag", AppKeyMap.DONUT).putExtra("lat", lat)
                        .putExtra("lng", lng).putExtra("title", "用户所在地址").putExtra("address", address).setClass(this,
                                TagLocationActivity.class));
                break;
            case R.id.btn_track:
                startActivity(new Intent().putExtra("orderId", orderId).setClass(this,
                        OrderTrackingActivity.class));
                break;
            case R.id.textView://联系客服
                String servicePhone = orderDetailLayout.getDetail().getCustomer_service_phone();
                startActivity(new Intent().putExtra("orderId", orderId).setClass(this,
                        CustomerServiceActivity.class).putExtra("servicePhone", servicePhone));
                break;
        }
    }

    private void completeAcce() {
        String pwd = gridPasswordView.getPassWord();
        if (TextUtils.isEmpty(pwd)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .please_input_service_code));
        } else
            networkModel.completeOrder(orderId, gridPasswordView.getPassWord(),
                    NetworkParams.ICECREAMSANDWICH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppTools.stopLocate();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        progressNewData();
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        final OrderDetailBean.DataEntity.AcceCostListEntity acceCostListEntity = guideList.get(pos);
        Class<?> cls = acceCostListEntity.getIntentFlag();
        String acid = acceCostListEntity.getAcid();
        Intent intent = new Intent();
        if (cls.equals(ExpensesResultActivity.class)) {
            intent.putExtra("costId", acid);
        } else {
            intent.putExtra("acceId", acid);
            intent.putExtra("flag", AppKeyMap.CUPCAKE);//代表如果步骤走到配件回寄的时候,提交完毕直接返回到本Activity
        }
        startActivity(intent.setClass(this, cls));

    }


    @Override
    public void onJsonObjectSuccess(BaseBean o, NetworkParams paramsCode) {
        //首次进来获取数据
        if (paramsCode == NetworkParams.CUPCAKE) {//获取全局数据
            OrderDetailBean orderDetailBean = (OrderDetailBean) o;
            final OrderDetailBean.DataEntity data = orderDetailBean.getData();
            fillProductList(orderDetailBean, data);
            fillGuideList(data);
            orderDetailLayout.setDetail(data);
        } else if (paramsCode == NetworkParams.DONUT) {//预约签到的返回
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .order_detail_already_sign_in));
            networkModel.orderDetail(orderId, "1", NetworkParams.CUPCAKE);
        } else if (paramsCode == NetworkParams.FROYO) {//完成产品处理
            progressNewData();//操作完成以后再获取一次数据,
            // 用于再次判断是否全部工单已完成
        } else if (paramsCode == NetworkParams.ICECREAMSANDWICH) {
            orderDetailLayout.llytEvaluate.setVisibility(View.GONE);
            orderDetailLayout.llytEvaluation.setVisibility(View.VISIBLE);
            AppTools.sendBroadcast(null, AppKeyMap.WAITING_COST_ACTION);
            Intent intent = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                    .FLAG_ACTIVITY_SINGLE_TOP).setClass(this, MainActivity.class);
            startActivity(intent);
            AppTools.sendBroadcast(new Bundle(), AppKeyMap.APPOINTMENT_CLIENT_ACTION);//意在刷新
        } else if (paramsCode == NetworkParams.GINGERBREAD) {
            Toast.makeText(OrderDetailActivity.this, "产品信息已提交，请等厂家修改产品信息后再操作工单", Toast
                    .LENGTH_LONG).show();
            start(MainActivity.class);
            AppTools.sendBroadcast(new Bundle(), AppKeyMap.APPOINTMENT_CLIENT_ACTION);//意在刷新
            errorProductPopupWindow.dismiss();
        } else if (paramsCode == NetworkParams.LOLLIPOP) {
            start(MainActivity.class);
            AppTools.sendBroadcast(new Bundle(), AppKeyMap.APPOINTMENT_CLIENT_ACTION);//意在刷新
        }
    }

    private void fillGuideList(OrderDetailBean.DataEntity data) {
        guideList.clear();
        guideList.addAll(data.getAcce_cost_list());
        guideAdapter.notifyDataSetChanged();
    }

    private void fillProductList(OrderDetailBean orderDetailBean, OrderDetailBean.DataEntity data) {
        boolean isSignIn = data.isSignIn();//是否已签到
        boolean isAllRepair = data.isAllRepair();//是否全部产品维修完毕
        //该工单是否全部完毕,并且已完成提交
        boolean isAllComplete = data.getIs_submit_complete();
        List<OrderDetailBean.ListEntity> listEntities = orderDetailBean.getList();
        boolean isTimeOut = data.getIs_sign_in().equals("3") || data.getIs_sign_in().equals("2");
        changeButtonState(isSignIn, isAllRepair, isAllComplete, isTimeOut);
        notifyAdapter(listEntities);
    }

    /**
     * 根据签到情况改变按钮状态
     *
     * @param isSignIn is sign in or not
     */
    private void changeButtonState(boolean isSignIn, boolean isAllRepair, boolean isAllComplete, boolean isTimeOut) {
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
    private void notifyAdapter(List<OrderDetailBean.ListEntity> listEntities) {
        this.list.clear();
        this.list.addAll(listEntities);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //定位成功后,执行预约签到
        if (bdLocation != null) {
            networkModel.signAppoint(orderId, bdLocation.getLatitude(), bdLocation.getLongitude(), NetworkParams.DONUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://服务项目
                if (resultCode == RESULT_OK) {
                    OrderDetailBean.ListEntity listEntity = list.get(serviceObjPos);
                    listEntity.setButtonType(ButtonType.NAN);
                    progressNewData();
                }
                break;
        }
    }

    @Override
    public void onOptsClick(AlterPopupOpts opts) {
        Intent intent = new Intent().putExtra("orderId", orderId);
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

    private void progressNewData() {
        networkModel.orderDetail(orderId, "1", NetworkParams.CUPCAKE);
    }

    /**
     * 处理button点击事件
     *
     * @param button the button which need to be progressed
     */
    private void progressButtonEvent(View button) {
        int pos = (int) button.getTag(R.id.tag_cupcake);
        ButtonType buttonType = (ButtonType) button.getTag(R.id.tag_donut);
        OrderDetailBean.ListEntity listEntity = list.get(pos);
        if (listEntity.getService_id().equals("0")) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .order_detail_please_enter_service_option));
            return;
        }
        if (buttonType != ButtonType.NAN) {
            switch (buttonType) {
                case POSITIVE_REPORT://complete on site
                    report(listEntity, "1");
                    break;
                case NEGATIVE_REPORT://can not be repaired
                    report(listEntity, "2");
                    break;
                case FITTING://apply fitting
                    String serviceId = listEntity.getServiceid();
                    String detailId = listEntity.getDetailid();
                    String accId = listEntity.getAcce_exe_type();
                    startActivity(new Intent().putExtra("orderId", orderId).putExtra("serviceId",
                            serviceId).putExtra("accId", accId).putExtra("detailId", detailId)
                            .setClass(this, FittingAdditionalActivity.class));
                    ViewUtils.startCountDown((Button) button, getString(R.string.fitting_unlock), getString(R.string.next), 5000);
                    break;
                case EXPENSES://apply expenses
                    startActivity(new Intent().putExtra("detailid", listEntity.getDetailid())
                            .setClass(this, ExpensesApplyActivity.class));
                    break;
            }
        } else {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .order_detail_please_enter_service_result));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_service, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final View actionView = menu.findItem(R.id.custom_service).getActionView();
        BGABadgeTextView textView = (BGABadgeTextView) actionView.findViewById(R.id.textView);
        textView.showCirclePointBadge();
        textView.setOnClickListener(this);
        titleBarTools.getToolbar().setPadding(0, 0, 15, 0);
        return true;
    }

    /**
     * report to server
     *
     * @param listEntity data source
     * @param isComplete completed or not, 1 means already complete,2 otherwise
     */
    private void report(OrderDetailBean.ListEntity listEntity, String isComplete) {
        List<String> thumbs = new ArrayList<>();
        thumbs.addAll(listEntity.getAddPics());
        String report = listEntity.getReport();
        String detailId = listEntity.getDetailid();
        networkModel.submitReport(detailId, isComplete, thumbs, report, NetworkParams.FROYO);
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        switch (opts) {
            case ALBUM:
                FunctionConfig functionConfig = new FunctionConfig.Builder().setSelected(picArr)
                        .setMutiSelectMaxSize(8).build();
                GalleryFinal.openGalleryMuti(AppKeyMap.CUPCAKE, functionConfig, this);
                break;
            case TAKE_PHOTO:
                GalleryFinal.openCamera(AppKeyMap.DONUT, this);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        super.onHanlderSuccess(requestCode, resultList);
        final List<String> addPics = (list.get(picPos))
                .getAddPics();
        for (PhotoInfo photoInfo : resultList) {
            if (!addPics.contains(photoInfo.getPhotoPath()))
                addPics.add(photoInfo.getPhotoPath());
        }
        (list.get(picPos)).setAddPics(addPics);
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddPictureDone(View picParentView, int childViewCount) {

    }

    @Override
    public void onDeletePictureDone(View deleteView, int childViewCount, int childPosition, int
            tagPos) {
        if (tagPos != -1) {
            OrderDetailBean.ListEntity listEntity = list.get(tagPos);
            List<String> addPic = listEntity.getAddPics();
            addPic.remove(childPosition);
            listEntity.setAddPics(addPic);
            commonBinderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        if (reportPos != -1) {
            OrderDetailBean.ListEntity listEntity = list.get
                    (reportPos);
            listEntity.setReport(word);
            commonBinderAdapter.notifyDataSetChanged();
        }
    }

    //error product callback
    @Override
    public void onSubmit(@NonNull String detailId, @NonNull String info, @NonNull List<String>
            filePaths) {
        networkModel.wrongProduct(detailId, info, filePaths, NetworkParams.GINGERBREAD);
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int params, int index) {
        OrderDetailBean.ListEntity listEntity;
        if (params == AppKeyMap.DONUT) {//means service result
            listEntity = list.get
                    (servicePos);
            listEntity.setThis_service_name(selectData);
            switch (index + 1) {
                case 1:
                    listEntity.setButtonType(ButtonType.POSITIVE_REPORT);
                    break;
                case 4:
                    listEntity.setButtonType(ButtonType.NEGATIVE_REPORT);
                    break;
                case 2:
                    if (listEntity.getLast_handle_type().equals("2")) {
                        Toast.makeText(OrderDetailActivity.this,
                                "本工单已申请过一次配件，相关受理进度可在产品信息上方的\"工单申请记录\"中查询", Toast.LENGTH_SHORT)
                                .show();
                    }
                    listEntity.setButtonType(ButtonType.FITTING);
                    break;
                case 3:
                    listEntity.setButtonType(ButtonType.EXPENSES);
                    break;
            }
            commonBinderAdapter.notifyDataSetChanged();
            scrollToSpecialPosition();
        } else if (params == AppKeyMap.FROYO) {
            networkModel.appointAgain(orderId, selectData, NetworkParams.LOLLIPOP);
        }
    }

    /**
     * scroll to special position
     */
    private void scrollToSpecialPosition() {
        final View childAt = linearLayoutManager.getChildAt(servicePos);
        int[] location = new int[2];
        childAt.getLocationInWindow(location);
        final int offSet = orderDetailLayout.scrollView.getScrollY() + location[1] -
                titleBarTools.getToolBarHeight() - AppTools.getStatusBarHeight();
        orderDetailLayout.scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                orderDetailLayout.scrollView.smoothScrollTo(0, offSet);
            }
        }, 300);
    }

}
