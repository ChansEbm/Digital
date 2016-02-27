package com.szbb.pro.ui.Activity.Orders.FittingOrders;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.CaptureActivityAnyOrientation;
import com.szbb.pro.FittingResendByWorkerLayout;
import com.szbb.pro.ItemFittingListDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Fittings.FittingDetailBean;
import com.szbb.pro.entity.Fittings.FittingResendBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Main.MainActivity;
import com.szbb.pro.widget.PopupWindow.TakePhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 配件申请订单详情-技工寄回-A模式
 */
public class FittingResendByWorkerActivity extends BaseAty<BaseBean, FittingDetailBean.DataEntity
        .AcceListEntity> implements OnWheelOptsSelectCallback, InputCallBack, RadioGroup
        .OnCheckedChangeListener, OnPhotoOptsSelectListener, OnAddPictureDoneListener {
    private FittingResendByWorkerLayout fittingResendByWorkerLayout;
    private FittingResendBean fittingResendBean = new FittingResendBean();
    private RecyclerView recyclerView;
    private WheelPopupWindow deliveryWheel;
    private InputDialog inputDialog;
    private ArrayList<String> alreadyAddPic = new ArrayList<>();
    private String acceId = "";

    private TakePhotoPopupWindow takePhotoPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingResendByWorkerLayout = (FittingResendByWorkerLayout) viewDataBinding;
        this.list = getIntent().getParcelableArrayListExtra("list");
        this.acceId = getIntent().getStringExtra("acceId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_fitting_apply);
        deliveryWheel = new WheelPopupWindow(this);
        takePhotoPopupWindow = new TakePhotoPopupWindow(this);
        recyclerView = fittingResendByWorkerLayout.recyclerView;

        commonBinderAdapter = new CommonBinderAdapter<FittingDetailBean.DataEntity
                .AcceListEntity>(this, R.layout.item_fitting_list_fitting_detail, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingDetailBean.DataEntity.AcceListEntity acceListEntity) {
                ItemFittingListDetailLayout detailLayout = (ItemFittingListDetailLayout)
                        viewDataBinding;
                detailLayout.simpleDraweeView.setImageURI(Uri.parse(acceListEntity.getAcce_thumb
                        ()));
                detailLayout.setDetail(acceListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        deliveryWheel.setCurvedData(getResources().getStringArray(R.array.delivery_way));
        deliveryWheel.setOptions(WheelOptions.STANDER);
        deliveryWheel.setPopupTitle(getString(R.string.delivery_way));
        deliveryWheel.setOnWheelOptsSelectCallback(this);

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fittingResendByWorkerLayout.radioGroup.setOnCheckedChangeListener(this);
        takePhotoPopupWindow.setOnPhotoOptsSelectListener(this);
        fittingResendByWorkerLayout.statusBar.setProgress(3);
        fittingResendByWorkerLayout.add.simpleDraweeView.setTag("add");
        fittingResendBean.setShippingPayType("2");
        fittingResendByWorkerLayout.setResend(fittingResendBean);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_resend_by_worker;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (result != null && !TextUtils.isEmpty(result.getContents())) {
            fittingResendBean.setShippingNum(result.getContents());
        }
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.simpleDraweeView:
                if (!TextUtils.isEmpty((CharSequence) view.getTag()))
                    takePhotoPopupWindow.showAtDefaultLocation();
                break;
            case R.id.flyt_shipment:
                deliveryWheel.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rylt_scan:
                inputDialog = new InputDialog(this, true);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.DONUT);
                inputDialog.setTitle(getString(R.string.delivery_number));
                inputDialog.show();
                break;
            case R.id.tv_scan:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
                break;
            case R.id.llyt_worker_report:
                inputDialog = new InputDialog(this);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.FROYO);
                inputDialog.setTitle(getString(R.string.fitting_worker_message));
                inputDialog.show();
                break;
            case R.id.flyt_cost:
                inputDialog = new InputDialog(this, true);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.CUPCAKE);
                inputDialog.setTitle(getString(R.string.shipment));
                inputDialog.show();
                break;
            case R.id.btn_submit:
                checkNecessary();
                break;
        }
    }

    private boolean checkNecessary() {
        FittingResendBean fittingResendBean = fittingResendByWorkerLayout.getResend();
        String shippingType = fittingResendBean.getShippingType();
        if (TextUtils.isEmpty(shippingType)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_input_shipping_type));
            return false;
        }
        String shippingNum = fittingResendBean.getShippingNum();
        if (TextUtils.isEmpty(shippingNum)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_input_shipping_num));
            return false;
        }
        String shippingCost = fittingResendBean.getShippingCost();
        if (TextUtils.isEmpty(shippingCost)) {
            AppTools.showNormalSnackBar(parentView, getString(R.string.please_input_shipping_cost));
            return false;
        }
        String remarks = fittingResendBean.getRemarks();
        progressUpload(shippingType, shippingNum, shippingCost, remarks);
        return true;
    }

    private void progressUpload(String shippingType, String shippingNum, String shippingCost,
                                String remarks) {
        networkModel.returnAcce(acceId, alreadyAddPic, remarks, shippingType, shippingNum,
                shippingType, shippingCost, NetworkParams.CUPCAKE);
    }


    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        fittingResendBean.setShippingType(selectData);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        switch (networkParams) {
            case CUPCAKE://means cost
                fittingResendBean.setShippingCost(word);
                break;
            case DONUT://means scan code
                fittingResendBean.setShippingNum(word);
                break;
            case FROYO://means remark
                fittingResendBean.setRemarks(word);
                break;
        }
        fittingResendByWorkerLayout.scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_prepaid:
                fittingResendBean.setShippingPayType("1");
                break;
            case R.id.rb_cash_on_delivery:
                fittingResendBean.setShippingPayType("2");
                break;
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        if (alreadyAddPic.size() == 5) {
            AppTools.showNormalSnackBar(parentView, "不可添加更多图片");
            return;
        }
        switch (opts) {
            case ALBUM:
                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(5).setSelected(alreadyAddPic).build();
                GalleryFinal.openGalleryMuti(AppKeyMap.CUPCAKE, functionConfig, this);
                break;
            case TAKE_PHOTO:
                GalleryFinal.openCamera(AppKeyMap.DONUT, this);
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        MarkPictureModel markPictureModel = new MarkPictureModel();
        markPictureModel.setOnAddPictureDoneListener(this);
        for (PhotoInfo photoInfo : resultList) {
            if (!alreadyAddPic.contains(photoInfo.getPhotoPath())) {
                markPictureModel.addSinglePictureInLinearLayoutByLocal(this,
                        fittingResendByWorkerLayout.llytUploadPic, photoInfo.getPhotoPath());
                alreadyAddPic.add(photoInfo.getPhotoPath());
            }
        }
    }

    @Override
    public void onAddPictureDone(View picParentView, int childViewCount) {

    }

    @Override
    public void onDeletePictureDone(View deleteView, int childViewCount, int childPosition, int
            tagPos) {
        for (String s : alreadyAddPic) {
            String tag = (String) deleteView.getTag();
            if (TextUtils.equals(s, tag)) {
                alreadyAddPic.remove(s);
                break;
            }
        }
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        final Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent
                .FLAG_ACTIVITY_CLEAR_TASK).setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
