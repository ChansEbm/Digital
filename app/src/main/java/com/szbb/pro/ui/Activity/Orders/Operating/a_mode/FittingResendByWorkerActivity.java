package com.szbb.pro.ui.activity.orders.operating.a_mode;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.fittings.CustomerAddressBean;
import com.szbb.pro.entity.fittings.ExpressComBean;
import com.szbb.pro.entity.fittings.FittingDetailBean;
import com.szbb.pro.entity.fittings.FittingResendBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 配件申请订单详情-技工寄回-A模式-填写回寄信息
 */
public class FittingResendByWorkerActivity extends BaseAty<BaseBean, FittingDetailBean.DataEntity
        .AcceListEntity> implements OnWheelOptsSelectCallback, InputCallBack, RadioGroup
        .OnCheckedChangeListener, DeleterHandlerCallback {
    private FittingResendByWorkerLayout fittingResendByWorkerLayout;
    private FittingResendBean fittingResendBean = new FittingResendBean();
    private RecyclerView recyclerView;
    private WheelPopupWindow deliveryWheel;
    private ArrayList<String> alreadyAddPic = new ArrayList<>();
    private String acceId = "";
    private ExpressComBean expressComBean = new ExpressComBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingResendByWorkerLayout = (FittingResendByWorkerLayout) viewDataBinding;
        this.list = getIntent().getParcelableArrayListExtra("list");
        this.acceId = getIntent().getStringExtra("acceId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_fitting_resend);
        deliveryWheel = new WheelPopupWindow(this);
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
        fittingResendByWorkerLayout.statusBar.setTextArr(R.array.fitting_receipting);
        fittingResendByWorkerLayout.statusBar.setProgress(3);
        fittingResendByWorkerLayout.workerDeleterScrollLayout.setDeleterHandlerCallback(this);
        fittingResendBean.setShippingPayType("1");
        fittingResendByWorkerLayout.setResend(fittingResendBean);
        networkModel.getFactoryAddress(getIntent().getStringExtra("orderId"), NetworkParams
                .DONUT);
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
            String contents = result.getContents();
            fittingResendBean.setShippingNum(contents);
            fittingResendByWorkerLayout.executePendingBindings();
            networkModel.getExpressCom(contents, NetworkParams.GINGERBREAD);//获取快递公司信息
        }
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_shipment:
                deliveriesData();
                break;
            case R.id.rylt_scan:
                InputDialog inputDialog = new InputDialog(this, false);
                inputDialog.setInputCallBack(this);
                inputDialog.setParams(NetworkParams.GINGERBREAD);
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
        String shippingPayType = fittingResendBean.getShippingPayType();
        progressUpload(shippingType, shippingNum, shippingCost, shippingPayType, remarks);
        return true;
    }

    private void progressUpload(String shippingType, String shippingNum, String shippingCost, String shippingPayType,
                                String remarks) {
        networkModel.returnAcce(acceId, alreadyAddPic, remarks, shippingType, shippingNum,
                shippingPayType, shippingCost, NetworkParams.CUPCAKE);
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        String comCode = expressComBean.getList().get(index).getComCode();
        fittingResendBean.setShippingType(selectData);
        fittingResendBean.setShippingTypeCom(comCode);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        switch (networkParams) {
            case CUPCAKE://means cost
                fittingResendBean.setShippingCost(word);
                break;
            case GINGERBREAD://means scan code
                fittingResendBean.setShippingNum(word);
                networkModel.getExpressCom(word, NetworkParams.GINGERBREAD);//获取快递公司信息
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
                Toast.makeText(FittingResendByWorkerActivity.this,
                        "由于到付的运费金额较高，厂家希望维修商先预付配件返厂运费，您预付的运费将和本工单维修费用一起结算，谢谢您的支持！", Toast
                                .LENGTH_LONG).show();
                fittingResendBean.setShippingPayType("2");
                break;
        }
    }


    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean, paramsCode);
        if (paramsCode == NetworkParams.CUPCAKE) {
            final Intent intent = new Intent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent
                    .FLAG_ACTIVITY_CLEAR_TOP);
            Class clz;
            int flag = getIntent().getIntExtra("flag", AppKeyMap.DONUT);
            switch (flag) {
                case AppKeyMap.CUPCAKE://代表回去工单详情
                    clz = OrderDetailActivity.class;
                    break;
                default://其他标识代表回去MainActivity
                    clz = MainActivity.class;
                    break;
            }
            intent.setClass(this, clz);
            startActivity(intent);
        } else if (paramsCode == NetworkParams.DONUT) {
            CustomerAddressBean customerAddressBean = (CustomerAddressBean) baseBean;
            fittingResendByWorkerLayout.setFactory(customerAddressBean.getData());
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
            Toast.makeText(FittingResendByWorkerActivity.this, "无法查询到快递公司,请仔细检查快递单号是否正确以及网络是否畅通!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list.size() == 0) {//如果只有一个 则直接录入
            fittingResendBean.setShippingType(list.get(0).getComName());
            fittingResendBean.setShippingTypeCom(list.get(0).getComCode());
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
    public void success(Set<Integer> keySet, List<String> photoPaths) {
        this.alreadyAddPic.clear();
        this.alreadyAddPic.addAll(photoPaths);
    }
}
