package com.szbb.pro.ui.Activity.Orders.Operating;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Order.OrderDetailBean;
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
import com.szbb.pro.ui.Activity.Expenses.ExpensesApplyActivity;
import com.szbb.pro.ui.Activity.Expenses.ExpensesDetailActivity;
import com.szbb.pro.ui.Activity.Main.MainActivity;
import com.szbb.pro.ui.Activity.Orders.Appointment.AppointmentAlterActivity;
import com.szbb.pro.ui.Activity.Orders.Appointment.AppointmentClientActivity;
import com.szbb.pro.ui.Activity.Orders.Appointment.AppointmentHistoryActivity;
import com.szbb.pro.ui.Activity.Orders.Appointment.AppointmentReturnActivity;
import com.szbb.pro.widget.PopupWindow.AlterPopupWindow;
import com.szbb.pro.widget.PopupWindow.ErrorProductPopupWindow;
import com.szbb.pro.widget.PopupWindow.TakePhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

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
    private OrderDetailLayout orderDetailLayout;

    private Button btnSignAppointment;//预约签到按钮
    private Button btnSignAgain;//再次签到按钮
    private GridPasswordView gridPasswordView;//输入服务码控件
    private AlterPopupWindow alterPopupWindow;
    private TakePhotoPopupWindow takePhotoPopupWindow;
    private WheelPopupWindow wheelPopupWindow;

    private String orderId = "";
    private OrderModel orderModel;
    private DialDialog dialDialog;
    private int servicePos = -1;//点击了本次服务结果后保存点击了的item的位置
    private int picPos = -1;//点击了添加图片后保存点击了的item的位置
    private int reportPos = -1;//点击了备注后保存item位置
    private int serviceObjPos = -1;

    private ArrayList<String> picArr = new ArrayList<>();//保存添加的图片
    private LinearLayoutManager linearLayoutManager;

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
        takePhotoPopupWindow = new TakePhotoPopupWindow(this);
        orderModel = new OrderModel(this);
        linearLayoutManager = new LinearLayoutManager(this);
        alterPopupWindow = new AlterPopupWindow(this);
        gridPasswordView = orderDetailLayout.gridPasswordView;
        btnSignAppointment = orderDetailLayout.btnSignAppointment;
        btnSignAgain = orderDetailLayout.btnSignAgain;
        recyclerView = orderDetailLayout.recyclerView;

        commonBinderAdapter = new CommonBinderAdapter<OrderDetailBean.ListEntity>(this, R.layout
                .item_order_detail, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, OrderDetailBean.ListEntity listEntity) {
                ItemOrderDetailLayout itemOrderDetail = (ItemOrderDetailLayout) viewDataBinding;
                String faultLabel = listEntity.getFault_lable();//故障标签
                LinearLayout linearLayout = itemOrderDetail.llytLabel;
                orderModel.addLabel(linearLayout, faultLabel);
                itemOrderDetail.sdvContactItemImage.setImageURI(Uri.parse(listEntity
                        .getProduct_thumb()));

                //根据上次服务结果选择隐藏或者显示某些项
                if (orderModel.changeStatusByLastService(itemOrderDetail, listEntity)) {
                    //如果是已完单 则添加后台图片到图片放置处
//                    orderModel.addPic(listEntity.getComplete_photos(), itemOrderDetail
//                            .llytUploadPic);
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
    }

    @Override
    protected void initEvents() {
        btnSignAppointment.setOnClickListener(this);
        orderDetailLayout.tvTechnologyHotLine.setOnClickListener(this);
        alterPopupWindow.setOnAlterPopupWindowOptsClickListener(this);
        takePhotoPopupWindow.setOnPhotoOptsSelectListener(this);
        wheelPopupWindow.setOnWheelMultiOptsCallback(this);

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R
                        .color.color_bg_gravy).sizeResId(R.dimen.large_margin_15dp).build());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commonBinderAdapter);
        progressNewData();
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.tv_call:
                dialDialog = new DialDialog(this, null);
                dialDialog.show(orderDetailLayout.getDetail().getTel());
                break;
            case R.id.tv_technology_hot_line:
                dialDialog = new DialDialog(this, null);
                dialDialog.show(orderDetailLayout.getDetail().getFactory_technology_tel());
                break;
            case R.id.tv_error_product:
                int errorPos = (int) view.getTag();
                OrderDetailBean.ListEntity errorListEntity = list.get
                        (errorPos);
                String detailId = errorListEntity.getDetailid();
                ErrorProductPopupWindow errorProductPopupWindow = new ErrorProductPopupWindow(this);
                errorProductPopupWindow.setDetailId(detailId);
                errorProductPopupWindow.setOnErrorProductCallback(this);
                errorProductPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_sign_appointment:
                if (AppTools.isNetworkConnected()) {
                    AppTools.showNormalSnackBar(parentView, getString(R.string.main_locating));
                    AppTools.locate(this);
                } else {
                    AppTools.showSettingSnackBar(parentView, getString(R.string
                            .no_network_is_detected));
                }
                break;
            case R.id.btn_edit_appointment:
                alterPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_sign_again:
                startActivity(new Intent().putExtra("orderid", orderId).setClass(this,
                        AppointmentClientActivity.class));
                break;
            case R.id.flyt_service_obj:
                serviceObjPos = (int) view.getTag();
                OrderDetailBean.ListEntity listEntity = list.get
                        (serviceObjPos);
                if (!orderModel.serviceObjWindow(this, listEntity)) {
                    AppTools.showNormalSnackBar(parentView, getString(R.string.can_not_be_changed)
                    );
                }
                break;
            case R.id.flyt_service_result:
                servicePos = (int) view.getTag();
                orderModel.thisServiceType(this, this).showAtLocation(parentView, Gravity.BOTTOM,
                        0, 0);
                break;
            case R.id.btn_add_pic:
                picPos = (int) view.getTag();
                this.picArr = new ArrayList<>();
                this.picArr = (ArrayList<String>) (list.get(picPos))
                        .getAddPics();
                takePhotoPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.llyt_report:
                reportPos = (int) view.getTag();
                InputDialog inputDialog = new InputDialog(this);
                inputDialog.setInputCallBack(this);
                inputDialog.setTitle(getString(R.string.note));
                inputDialog.setParams(NetworkParams.CUPCAKE);
//                OrderDetailBean.ListEntity reportEntity = list.get
//                        (reportPos);
                inputDialog.show();
                break;
            case R.id.button:
                progressButtonEvent(view);
                break;
            case R.id.btn_resend_code:
                networkModel.getServiceCode(orderId, NetworkParams.HONEYCOMB);
                break;
            case R.id.btn_confirm_done:
                completeAcce();
                break;
            case R.id.btn_expenses:
                startActivity(new Intent().setClass(this, ExpensesDetailActivity.class).putExtra
                        ("orderId", orderId));
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
                    NetworkParams
                            .ICECREAMSANDWICH);
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
    public void onJsonObjectSuccess(BaseBean o, NetworkParams paramsCode) {
        //首次进来获取数据
        if (paramsCode == NetworkParams.CUPCAKE) {//获取全局数据
            OrderDetailBean orderDetailBean = (OrderDetailBean) o;
            boolean isSignIn = orderDetailBean.getData().isSignIn();//是否已签到
            boolean isAllRepair = orderDetailBean.getData().isAllRepair();//是否全部产品维修完毕
            //该工单是否全部完毕,并且已完成提交
            boolean isAllComplete = orderDetailBean.getData().getIs_submit_complete();
            List<OrderDetailBean.ListEntity> listEntities = orderDetailBean.getList();
            changeButtonState(isSignIn, isAllRepair, isAllComplete);
            notifyAdapter(listEntities);
            orderDetailLayout.setDetail(orderDetailBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {//预约签到的返回
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .order_detail_already_sign_in));
            btnSignAppointment.setText(getString(R.string.order_detail_already_sign_in));
            btnSignAppointment.setEnabled(false);
        } else if (paramsCode == NetworkParams.FROYO) {//完成产品处理
            progressNewData();//操作完成以后再获取一次数据,
            // 用于再次判断是否全部工单已完成
        } else if (paramsCode == NetworkParams.ICECREAMSANDWICH) {
            orderDetailLayout.llytEvaluate.setVisibility(View.GONE);
            orderDetailLayout.btnExpenses.setVisibility(View.VISIBLE);
            AppTools.sendBroadcast(null, AppKeyMap.WAITING_COST_ACTION);
            Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent
                    .FLAG_ACTIVITY_SINGLE_TOP).setClass(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 根据签到情况改变按钮状态
     *
     * @param isSignIn is sign in or not
     */
    private void changeButtonState(boolean isSignIn, boolean isAllRepair, boolean isAllComplete) {
        btnSignAppointment.setText(isSignIn ? getString(R.string
                .order_detail_already_sign_in) : getString(R.string.order_detail_sign_in));
        btnSignAppointment.setEnabled(!isSignIn);
        btnSignAgain.setVisibility(isSignIn ? View.VISIBLE : View.GONE);

        if (isAllRepair) {
            btnSignAgain.setVisibility(View.GONE);
            orderDetailLayout.btnEditAppointment.setVisibility(View.GONE);
            orderDetailLayout.llytEvaluate.setVisibility(View.VISIBLE);
        }

        if (isAllComplete) {
            orderDetailLayout.llytEvaluate.setVisibility(View.GONE);
            orderDetailLayout.btnExpenses.setVisibility(View.VISIBLE);
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
            networkModel.signAppoint(orderId, bdLocation.getLatitude(), bdLocation.getLongitude()
                    , NetworkParams.DONUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://服务项目
                if (resultCode == RESULT_OK) {
                    OrderDetailBean.ListEntity listEntity = list.get
                            (serviceObjPos);
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
                            .setClass
                                    (this, FittingWareHouseActivity.class));
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
    public void onHanlderSuccess(int requestCode, List resultList) {
        List<PhotoInfo> photoInfos = new ArrayList<>();
        photoInfos.clear();
        photoInfos.addAll(resultList);
        final List<String> addPics = (list.get(picPos))
                .getAddPics();
        for (PhotoInfo photoInfo : photoInfos) {
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
                    listEntity.setButtonType(ButtonType.FITTING);
                    break;
                case 3:
                    listEntity.setButtonType(ButtonType.EXPENSES);
                    break;
            }
            commonBinderAdapter.notifyDataSetChanged();
            scrollToSpecialPosition();
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
