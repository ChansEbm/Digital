package com.szbb.pro.ui.activity.orders.operating.a_mode;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.FittingApplyLayout;
import com.szbb.pro.ItemFittingApplyLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.fittings.CustomerAddressBean;
import com.szbb.pro.entity.fittings.FittingWareHouseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.orders.operating.FittingReceiverActivity;
import com.szbb.pro.ui.activity.orders.operating.OrderDetailActivity;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;
import com.szbb.pro.widget.deleter.DeleterScrollLayout;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * A模式下的先申请配件
 */
public class FittingApplyActivity
        extends BaseAty<BaseBean, FittingWareHouseBean
        .AcceListEntity>
        implements
        InputCallBack, DeleterHandlerCallback {
    private FittingApplyLayout fittingApplyLayout;
    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray = null;

    private String detailId = "";//用于最后提交
    private String orderId = "";//用于获取客户默认地址
    private ArrayList<String> alreadyAdd = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fittingApplyLayout = (FittingApplyLayout) viewDataBinding;
        if (getIntent() == null) {
            AppTools.removeSingleActivity(this);
        }
        acceListEntitySparseArray = getIntent().getExtras()
                .getSparseParcelableArray
                        ("acceListEntitySparseArray");
        //如果是直接从其他配件进来,则肯定为空
        FittingWareHouseBean.AcceListEntity acceListEntity = getIntent().getExtras()
                .getParcelable(
                        "acceListEntity");
        if (acceListEntitySparseArray == null && acceListEntity != null) {//如果是直接从其他配件进来 则执行该步骤
            acceListEntitySparseArray = new SparseArray<>();
            acceListEntitySparseArray.put(0, acceListEntity);
        }
        detailId = getIntent().getExtras()
                .getString("detailId");
        orderId = getIntent().getExtras()
                .getString("orderId");
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_fitting_apply);
        fillSource();//填充数据源
        commonBinderAdapter = new CommonBinderAdapter<FittingWareHouseBean.AcceListEntity>(this,
                R.layout.item_fitting_list_ware_house,
                list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingWareHouseBean.AcceListEntity acceListEntity) {
                ItemFittingApplyLayout itemShopCarLayout = (ItemFittingApplyLayout) viewDataBinding;
                itemShopCarLayout.simpleDraweeView.setImageURI(Uri.parse
                        (acceListEntity.getAcce_thumb()));
                itemShopCarLayout.setFitting(acceListEntity);
            }
        };
        recyclerView = fittingApplyLayout.recyclerView;

    }

    @Override
    protected void initEvents() {
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        networkModel.getCustomerAddress(orderId, NetworkParams.CUPCAKE);//获取用户默认地址
        networkModel.getMechanicAddress(NetworkParams.CUPCAKE);//获取技工默认地址
        DeleterScrollLayout deleterScrollLayout = fittingApplyLayout.applyDeleterScrollLayout;
        deleterScrollLayout.setDeleterHandlerCallback(this);

    }


    private void fillSource() {
        int allCount = 0;
        list.clear();
        for (int i = 0;
             i < acceListEntitySparseArray.size();
             i++) {
            list.add(acceListEntitySparseArray.valueAt(i));
            allCount += acceListEntitySparseArray.valueAt(i)
                    .getCount();
        }
        fittingApplyLayout.tvNum.setText(String.valueOf(allCount));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_apply;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.btn_submit:
                progressUpload();
                break;
            case R.id.btn_edit:
                startActivityForResult(new Intent().setClass(this, FittingReceiverActivity.class)
                        .putExtra("orderId", orderId), AppKeyMap.CUPCAKE);
                break;
            case R.id.llyt_report:
                InputDialog inputDialog = new InputDialog(this, false);
                inputDialog.setTitle(getString(R.string.fitting_leave_supplier_message));
                inputDialog.setInputCallBack(this);
                inputDialog.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppKeyMap.CUPCAKE && resultCode == RESULT_OK) {
            fittingApplyLayout.setCustomer(data.<CustomerAddressBean
                    .DataEntity>getParcelableExtra("dataEntity"));
        }
    }

    private void progressUpload() {
        List<String> acces = new ArrayList<>();
        List<String> otherAcces = new ArrayList<>();
        List<String> fileThumbs = new ArrayList<>();

        for (String str : alreadyAdd) {
            if (!fileThumbs.contains(str)) {
                fileThumbs.add(str);
            }
        }
        for (int i = 0;
             i < acceListEntitySparseArray.size();
             i++) {
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
        final CustomerAddressBean.DataEntity customer = fittingApplyLayout.getCustomer();
        String applicant = customer.getNickname();
        String applicantTell = customer.getTelephone();
        String areaIds = customer.getArea_ids();
        String address = customer.getAddress();
        String remarks = fittingApplyLayout.tvMessage.getText()
                .toString();
        networkModel.requireAcce(detailId, acces, otherAcces, fileThumbs, remarks, applicant,
                applicantTell, areaIds, address, NetworkParams.DONUT);
    }

    @Override
    public void onJsonObjectSuccess(BaseBean t, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {//means get default client info
            CustomerAddressBean customerAddressBean = (CustomerAddressBean) t;
            fittingApplyLayout.setCustomer(customerAddressBean.getData());
        } else if (paramsCode == NetworkParams.DONUT) {
            Toast.makeText(FittingApplyActivity.this,
                    "配件申请已提交成功，神州联保会尽快安排寄发新配件，请在收到配件后再次预约客户继续服务。【为了准确计算您的上门费用，请把再次预约时间通过工单详情里的“再次预约”按钮上传提交】",
                    Toast.LENGTH_LONG)
                    .show();
            postApplySuccess();
        }
    }

    private void postApplySuccess() {
        start(OrderDetailActivity.class,
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppTools.sendBroadcast(new Bundle(), AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE);
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        fittingApplyLayout.tvMessage.setText(word);
    }

    @Override
    public void success(Set<Integer> keySet, List<String> photoPaths) {
        this.alreadyAdd.clear();
        this.alreadyAdd.addAll(photoPaths);
    }
}
