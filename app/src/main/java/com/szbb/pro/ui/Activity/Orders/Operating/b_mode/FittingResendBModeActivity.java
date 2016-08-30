package com.szbb.pro.ui.activity.orders.operating.b_mode;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.fittings.CustomerAddressBean;
import com.szbb.pro.entity.fittings.ExpressComBean;
import com.szbb.pro.entity.fittings.FittingResendBean;
import com.szbb.pro.entity.fittings.FittingWareHouseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.orders.operating.FittingReceiverActivity;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 申请配件同时回寄配件  B模式
 */

public class FittingResendBModeActivity extends BaseAty<BaseBean, FittingWareHouseBean
        .AcceListEntity>
        implements
        OnWheelOptsSelectCallback, RadioGroup.OnCheckedChangeListener, InputCallBack, DeleterHandlerCallback {

    private FittingResendLayout fittingResendLayout;
    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray = new
            SparseArray<>();
    private String orderId;

    private FittingResendBean fittingResendBean = new FittingResendBean();
    private ExpressComBean expressComBean = new ExpressComBean();//保存快递方式
    private RecyclerView recyclerView;
    private String detailId;
    private ArrayList<String> alreadyAdd = new ArrayList<>();//保存要提交的图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingResendLayout = (FittingResendLayout) viewDataBinding;

        if (getIntent() == null)
            AppTools.removeSingleActivity(this);
        acceListEntitySparseArray = getIntent().getExtras().getSparseParcelableArray
                ("acceListEntitySparseArray");
        FittingWareHouseBean.AcceListEntity acceListEntity = getIntent().getExtras()
                .getParcelable("acceListEntity");
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

    }

    @Override
    protected void initEvents() {
        fittingResendBean.setShippingPayType("1");//默认预付

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fittingResendLayout.radioGroup.setOnCheckedChangeListener(this);
        fittingResendLayout.resendDeleterScrollLayout.setPlacerImages(R.mipmap.ic_font_side, R.mipmap.ic_back_side, R.mipmap.ic_model);
        fittingResendLayout.resendDeleterScrollLayout.setDeleterHandlerCallback(this);

        networkModel.getFactoryAddress(orderId, NetworkParams.CUPCAKE);
        networkModel.getCustomerAddress(orderId, NetworkParams.DONUT);
        fittingResendLayout.setResend(fittingResendBean);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_resend;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_shipment:
                deliveriesData();
                break;
            case R.id.llyt_report:
                InputDialog inputDialog = new InputDialog(this, false);
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

    private void progressUpload() {
        if (!checkNecessary())
            return;
        List<String> acces = new ArrayList<>();
        List<String> otherAcces = new ArrayList<>();
        List<String> fileThumbs = new ArrayList<>();
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
        String shippingTypeCom = resendBean.getShippingTypeCom();
        String shippingNum = resendBean.getShippingNum();
        String shippingPayType = resendBean.getShippingPayType();
        String shippingCost = resendBean.getShippingCost();

        networkModel.applyAcce(detailId, acces, otherAcces, fileThumbs, remarks, shippingType,
                shippingNum, shippingTypeCom, shippingPayType, shippingCost, applicant, applicantTell, areaIds,
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
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE || paramsCode == NetworkParams.DONUT) {
            CustomerAddressBean customerAddressBean = (CustomerAddressBean) baseBean;
            if (paramsCode == NetworkParams.CUPCAKE) {//means factory info
                fittingResendLayout.setFactoryAddr(customerAddressBean.getData());
            } else {// means customer info
                fittingResendLayout.setCustomer(customerAddressBean.getData());
            }
        } else if (paramsCode == NetworkParams.FROYO) {
            Toast.makeText(FittingResendBModeActivity.this,
                    "配件申请已提交成功,神州联保会尽快安排寄发新配件,请在收到配件后再次预约客户继续服务",
                    Toast.LENGTH_LONG).show();
            start(MainActivity.class);
            AppTools.sendBroadcast(new Bundle(), AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
        } else if (paramsCode == NetworkParams.GINGERBREAD) {
            this.expressComBean = (ExpressComBean) baseBean;
            fittingResendBean.setShippingType("");
            fittingResendBean.setShippingTypeCom("");
            deliveriesData();
        }
    }

    private void deliveriesData() {
        List<ExpressComBean.ListBean> list = expressComBean.getList();
        if (list == null || list.isEmpty()) {
            Toast.makeText(FittingResendBModeActivity.this, "无法查询到快递公司,请仔细检查快递单号是否正确以及网络是否畅通!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list.size() == 0) {//如果只有一个 则直接录入
            fittingResendBean.setShippingType(list.get(0).getComName());
            fittingResendBean.setShippingTypeCom(list.get(0).getComCode());
            return;
        }
        String[] deliveries = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {//遍历返回的存在的多个快递公司
            deliveries[i] = list.get(i).getComName();
        }
        WheelPopupWindow deliveryWheel = new WheelPopupWindow(this);
        deliveryWheel.setCurvedData(deliveries);
        deliveryWheel.setOptions(WheelOptions.STANDER);
        deliveryWheel.setPopupTitle(getString(R.string.delivery_way));
        deliveryWheel.setOnWheelOptsSelectCallback(this);
        deliveryWheel.showAtDefaultLocation();
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
                String contents = result.getContents();
                fittingResendBean.setShippingNum(contents);
                fittingResendLayout.executePendingBindings();
                networkModel.getExpressCom(contents, NetworkParams.GINGERBREAD);//获取快递公司信息
            }
        }
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        String comCode = expressComBean.getList().get(index).getComCode();
        fittingResendBean.setShippingType(selectData);
        fittingResendBean.setShippingTypeCom(comCode);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_prepaid:
                fittingResendBean.setShippingPayType("1");
                break;
            case R.id.rb_cash_on_delivery:
                Toast.makeText(this, "根据厂家的要求，请您在返回配件时先预付运费，您预付的运费将和本工单维修费用一起结算，给您带来不便敬请谅解，谢谢您的支持！", Toast
                        .LENGTH_LONG).show();
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
                networkModel.getExpressCom(word, NetworkParams.GINGERBREAD);//获取快递公司信息
                break;
            case FROYO://means remark
                fittingResendBean.setRemarks(word);
                break;
        }
    }

    @Override
    public void success(Set<Integer> keySet, List<String> photoPaths) {
        alreadyAdd.clear();
        alreadyAdd.addAll(photoPaths);
    }
}