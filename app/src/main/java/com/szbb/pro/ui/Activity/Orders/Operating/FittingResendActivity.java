package com.szbb.pro.ui.Activity.Orders.Operating;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.CaptureActivityAnyOrientation;
import com.szbb.pro.FittingResendLayout;
import com.szbb.pro.ItemFittingApplyLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Fittings.CustomerAddressBean;
import com.szbb.pro.entity.Fittings.FittingResendBean;
import com.szbb.pro.entity.Fittings.FittingWareHouseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.PopupWindow.TakePhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 申请配件同时回寄配件
 */
public class FittingResendActivity extends BaseAty<BaseBean, FittingWareHouseBean.AcceListEntity>
        implements OnPhotoOptsSelectListener, OnAddPictureDoneListener,
        OnWheelOptsSelectCallback, RadioGroup.OnCheckedChangeListener, InputCallBack {

    private FittingResendLayout fittingResendLayout;
    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray = new
            SparseArray<>();
    private String orderId;

    private FittingResendBean fittingResendBean = new FittingResendBean();
    private WheelPopupWindow deliveryWheel;
    private RecyclerView recyclerView;
    private TakePhotoPopupWindow takePhotoPopupWindow;
    private InputDialog inputDialog;
    private String detailId;
    private int flag;
    private ArrayList<String> alreadyAdd = new ArrayList<>();
    private SparseArray<String> picsPath = new SparseArray<>();
    private FittingWareHouseBean.AcceListEntity acceListEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingResendLayout = (FittingResendLayout) viewDataBinding;

        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        acceListEntitySparseArray = getIntent().getExtras().getSparseParcelableArray
                ("acceListEntitySparseArray");
        acceListEntity = getIntent().getExtras().getParcelable("acceListEntity");//获取从配件申请页面进来的值
        if (acceListEntitySparseArray == null && acceListEntity != null) {//如果是直接从配件申请进来 则执行该步骤
            acceListEntitySparseArray = new SparseArray<>();
            acceListEntitySparseArray.put(0, acceListEntity);
        }
        detailId = getIntent().getExtras().getString("detailId");
        orderId = getIntent().getExtras().getString("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.fitting_post_back_applied_add);
        fittingResendLayout.setResend(fittingResendBean);
        deliveryWheel = new WheelPopupWindow(this);

        fillSource();//填充数据源
        commonBinderAdapter = new CommonBinderAdapter<FittingWareHouseBean.AcceListEntity>(this,
                R.layout.item_fitting_list_ware_house, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingWareHouseBean.AcceListEntity acceListEntity) {
                ItemFittingApplyLayout itemShopCarLayout = (ItemFittingApplyLayout) viewDataBinding;
                itemShopCarLayout.simpleDraweeView.setImageURI(Uri.parse
                        (acceListEntity.getAcce_thumb()));
                itemShopCarLayout.setFitting(acceListEntity);
            }
        };
        recyclerView = fittingResendLayout.recyclerView;

        takePhotoPopupWindow = new TakePhotoPopupWindow(this);
    }

    @Override
    protected void initEvents() {
        fittingResendBean.setShippingPayType("2");

        deliveryWheel.setCurvedData(getResources().getStringArray(R.array.delivery_way));
        deliveryWheel.setOptions(WheelOptions.STANDER);
        deliveryWheel.setPopupTitle(getString(R.string.delivery_way));
        deliveryWheel.setOnWheelOptsSelectCallback(this);

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fittingResendLayout.radioGroup.setOnCheckedChangeListener(this);

        takePhotoPopupWindow.setOnPhotoOptsSelectListener(this);

        networkModel.getFactoryAddress(orderId, NetworkParams.CUPCAKE);
        networkModel.getCustomerAddress(orderId, NetworkParams.DONUT);
        fittingResendLayout.setResend(fittingResendBean);
        initAllSimpleDraweeView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_resend;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.simpleDraweeView:
                String tag = (String) view.getTag();
                caseByTag(tag);
                break;
            case R.id.flyt_shipment:
                deliveryWheel.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.llyt_report:
                inputDialog = new InputDialog(this, true);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.FROYO);
                inputDialog.setTitle(getString(R.string.fitting_leave_supplier_message));
                inputDialog.show();
                break;
            case R.id.tv_scan:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
                break;
            case R.id.rylt_scan:
                inputDialog = new InputDialog(this, true);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.DONUT);
                inputDialog.setTitle(getString(R.string.delivery_number));
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
                progressUpload();
                break;
            case R.id.btn_edit:
                startActivityForResult(new Intent().setClass(this, FittingReceiverActivity.class)
                        .putExtra
                                ("orderId", orderId), AppKeyMap.CUPCAKE);
                break;
        }
    }

    private void fillSource() {
        int allCount = 0;
        list.clear();
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            list.add(acceListEntitySparseArray.valueAt(i));
            allCount += acceListEntitySparseArray.valueAt(i).getCount();
        }
        fittingResendBean.setTotalCount(allCount + "");
    }

    private void initAllSimpleDraweeView() {
        fittingResendLayout.font.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_font_side);
        fittingResendLayout.font.simpleDraweeView.setTag("front");
        fittingResendLayout.back.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_back_side);
        fittingResendLayout.back.simpleDraweeView.setTag("back");
        fittingResendLayout.model.simpleDraweeView.getHierarchy().setPlaceholderImage(R.mipmap
                .ic_model);
        fittingResendLayout.model.simpleDraweeView.setTag("model");
        fittingResendLayout.add.simpleDraweeView.setOnClickListener(this);
        fittingResendLayout.add.simpleDraweeView.setTag("add");
    }

    private void caseByTag(String tag) {
        switch (tag) {
            case "front":
                flag = AppKeyMap.CUPCAKE;
                break;
            case "back":
                flag = AppKeyMap.DONUT;
                break;
            case "model":
                flag = AppKeyMap.FROYO;
                break;
            case "add":
                flag = AppKeyMap.GINGERBREAD;
                break;
            default:
                return;
        }
        takePhotoPopupWindow.showAtDefaultLocation();
    }

    private void progressUpload() {
        if (!checkNecessary())
            return;
        List<String> acces = new ArrayList<>();
        List<String> otherAcces = new ArrayList<>();
        List<String> fileThumbs = new ArrayList<>();
        for (int i = 0; i < picsPath.size(); i++) {
            alreadyAdd.add(picsPath.valueAt(i));
        }
        for (String str : alreadyAdd) {
            fileThumbs.add(str);
        }
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            final FittingWareHouseBean.AcceListEntity acceListEntity = acceListEntitySparseArray
                    .valueAt(i);
            String id = acceListEntity.getAcce_id();
            String number = acceListEntity.getCount() + "";
            String acce = id + "," + number;
            if (acceListEntity.isExtra()) {
                otherAcces.add(acce);
                continue;
            }
            acces.add(acce);
        }
        final CustomerAddressBean.DataEntity customer = fittingResendLayout.getCustomer();
        String applicant = customer.getNickname();
        String applicantTell = customer.getTelephone();
        String areaIds = customer.getArea_ids();
        String address = customer.getAddress();

        FittingResendBean resendBean = fittingResendLayout.getResend();
        String remarks = resendBean.getRemarks();
        String shippingType = resendBean.getShippingType();
        String shippingNum = resendBean.getShippingNum();
        String shippingPayType = resendBean.getShippingPayType();
        String shippingCost = resendBean.getShippingCost();

        networkModel.applyAcce(detailId, acces, otherAcces, fileThumbs, remarks, shippingType,
                shippingNum, shippingPayType, shippingCost, applicant, applicantTell, areaIds,
                address, NetworkParams.FROYO);

    }

    private boolean checkNecessary() {
        FittingResendBean fittingResendBean = fittingResendLayout.getResend();
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
        return true;
    }

    @Override
    public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
        String filePath;
        filePath = resultList.get(0).getPhotoPath();
        switch (requestCode) {
            case AppKeyMap.CUPCAKE://means  font side from album
                fittingResendLayout.font.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(0, filePath);
                break;
            case AppKeyMap.DONUT://means  back side from album
                fittingResendLayout.back.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(1, filePath);
                break;
            case AppKeyMap.FROYO://means  model side from album
                fittingResendLayout.model.simpleDraweeView.setImageURI(Uri.parse("file://" +
                        filePath));
                picsPath.put(2, filePath);
                break;
            default://means add pic
                MarkPictureModel markPictureModel = new MarkPictureModel();
                markPictureModel.setIsNeedDeleteIcon(true);
                markPictureModel.setOnAddPictureDoneListener(this);
                for (PhotoInfo photoInfo : resultList) {
                    markPictureModel.addSinglePictureInLinearLayoutByLocal(this,
                            fittingResendLayout.llytUploadPic, photoInfo.getPhotoPath());
                    if (!alreadyAdd.contains(photoInfo.getPhotoPath())) {
                        alreadyAdd.add(photoInfo.getPhotoPath());
                        picsPath.put(photoInfo.getPhotoId(), photoInfo.getPhotoPath());
                    }
                }

                break;
        }
    }

    @Override
    public void onOptsSelect(PhotoPopupOpts opts) {
        if (flag == -1)
            return;
        if (alreadyAdd.size() == 2) {
            AppTools.showNormalSnackBar(parentView, "不可添加更多图片");
            return;
        }
        switch (opts) {
            case ALBUM:
                if (flag == AppKeyMap.GINGERBREAD) {
                    FunctionConfig functionConfig = new FunctionConfig.Builder()
                            .setMutiSelectMaxSize(2).setSelected(alreadyAdd).build();
                    GalleryFinal.openGalleryMuti(flag, functionConfig, this);
                } else {
                    GalleryFinal.openGallerySingle(flag, this);
                }
                break;
            case TAKE_PHOTO:
                GalleryFinal.openCamera(flag, this);
                break;
        }
    }

    @Override
    public void onAddPictureDone(View picParentView, int childViewCount) {

    }

    @Override
    public void onDeletePictureDone(View deleteView, int childViewCount, int childPosition, int
            tagPos) {
        String tag = (String) deleteView.getTag();
        int index = picsPath.indexOfValue(tag);
        int key = picsPath.keyAt(index);
        alreadyAdd.remove(picsPath.get(key));
        picsPath.delete(key);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE || paramsCode == NetworkParams.DONUT) {
            CustomerAddressBean customerAddressBean = (CustomerAddressBean) baseBean;
            if (paramsCode == NetworkParams.CUPCAKE) {//means factory info
                fittingResendLayout.setFactoryAddr(customerAddressBean.getData());
            } else if (paramsCode == NetworkParams.DONUT) {// means customer info
                fittingResendLayout.setCustomer(customerAddressBean.getData());
            }
        } else if (paramsCode == NetworkParams.FROYO) {
            startActivity(new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent
                    .FLAG_ACTIVITY_SINGLE_TOP).setClass(this, OrderDetailActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppKeyMap.CUPCAKE && resultCode == RESULT_OK) {
            fittingResendLayout.setCustomer(data.<CustomerAddressBean
                    .DataEntity>getParcelableExtra("dataEntity"));
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                    data);
            if (result != null && !TextUtils.isEmpty(result.getContents())) {
                fittingResendBean.setShippingNum(result.getContents());
                fittingResendLayout.executePendingBindings();
            }
        }
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        fittingResendBean.setShippingType(selectData);
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
    }

}