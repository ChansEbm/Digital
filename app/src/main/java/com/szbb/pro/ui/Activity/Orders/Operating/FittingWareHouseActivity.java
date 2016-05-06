package com.szbb.pro.ui.activity.orders.operating;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.szbb.pro.R;
import com.szbb.pro.ShopCarLayout;
import com.szbb.pro.WareHouseLayout;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.Fittings.FittingWareHouseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.CountChangeCallBack;
import com.szbb.pro.impl.OnCountListener;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.activity.orders.operating.a_mode.FittingApplyActivity;
import com.szbb.pro.ui.activity.orders.operating.b_mode.FittingResendBModeActivity;
import com.szbb.pro.widget.PopupWindow.ShopCarPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 配件库
 */
public class FittingWareHouseActivity extends BaseAty<BaseBean, FittingWareHouseBean
        .AcceListEntity> implements MaterialSearchView
        .OnQueryTextListener, OnWheelOptsSelectCallback, OnCountListener<FittingWareHouseBean
        .AcceListEntity>,
        CountChangeCallBack<FittingWareHouseBean.AcceListEntity> {

    private String orderId = "";
    private String serviceId = "";
    private String accId = "";
    private String detailId = "";

    private WheelPopupWindow wheelPopupWindow;
    private WareHouseLayout fittingWareHouseLayout;
    private MaterialSearchView materialSearchView;

    private RecyclerView recyclerView;
    private ShopCarPopupWindow shopCarPopupWindow;

    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray
            = new
            SparseArray<>();//保存选择好的配件

    private String classifyId = "";
    private boolean isFirst = true;//记录是否第一次进该页面
    private List<FittingWareHouseBean.PartsListEntity> partsListEntities = new ArrayList<>();
    //记录分类数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            AppTools.removeSingleActivity(this);
        }
        orderId = getIntent().getStringExtra("orderId");
        serviceId = getIntent().getStringExtra("serviceId");
        accId = getIntent().getStringExtra("accId");
        detailId = getIntent().getStringExtra("detailId");

        fittingWareHouseLayout = (WareHouseLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.fitting_ware_horse);
        recyclerView = fittingWareHouseLayout.recyclerView;
        materialSearchView = fittingWareHouseLayout.materialSearchView;
        shopCarPopupWindow = new ShopCarPopupWindow(this);

        wheelPopupWindow = new WheelPopupWindow(this);
        commonBinderAdapter = new CommonBinderAdapter<FittingWareHouseBean.AcceListEntity>(this,
                R.layout.item_shop_car, list) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingWareHouseBean.AcceListEntity acceListEntity) {
                ShopCarLayout itemShopCarLayout = (ShopCarLayout) viewDataBinding;
                itemShopCarLayout.countView.setCount(acceListEntity.getCount());
                itemShopCarLayout.countView.setPosition(position);
                itemShopCarLayout.countView.setT(acceListEntity);
                itemShopCarLayout.countView.setOnCountListener(FittingWareHouseActivity.this);
                itemShopCarLayout.simpleDraweeView.setImageURI(Uri.parse
                        (acceListEntity.getAcce_thumb()));
                itemShopCarLayout.setShopCar(acceListEntity);
            }
        };
    }

    @Override
    protected void initEvents() {
        wheelPopupWindow.setOnWheelOptsSelectCallback(this);
        wheelPopupWindow.setOptions(WheelOptions.STANDER);
        wheelPopupWindow.setPopupTitle(getString(R.string.classify));

        shopCarPopupWindow.setCountChangeCallBack(this);

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fittingWareHouseLayout.llytShopCar.setOnClickListener(this);

        materialSearchView.setOnQueryTextListener(this);
        networkModel.productAcceList(orderId, serviceId, "", "", NetworkParams.CUPCAKE);
        notifyShopCarCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ware_house, menu);
        materialSearchView.setMenuItem(menu.findItem(R.id.search));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.add) {
//            //添加其他配件
//            startActivityForResult(new Intent().setClass(this, FittingAdditionalActivity.class)
//                    .putExtra("detailId", detailId), AppKeyMap.CUPCAKE);
//        }
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fitting_ware_house;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_classify:
                wheelPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.llyt_shop_car:
                shopCarPopupWindow.showAtLocation(parentView, Gravity
                        .BOTTOM, 0, 0);
                break;
            case R.id.btn_done:
                if (checkNecessary()) {
                    startActivityByAccType();//根据状态不同打开不同页面
                }
                break;
        }
    }


    private void startActivityByAccType() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("detailId", detailId);
        bundle.putString("orderId", orderId);
        bundle.putSparseParcelableArray("acceListEntitySparseArray", acceListEntitySparseArray);
        intent.putExtras(bundle);
        switch (accId) {
            case "1":
                intent.setClass(this, FittingApplyActivity.class);
                break;
            case "2":
                intent.setClass(this, FittingResendBModeActivity.class);
                break;
            default:
                return;

        }
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        fittingWareHouseLayout.tvClassify.setText(selectData);
        classifyId = partsListEntities.get(index).getParts_id();//获取选择的分类id
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.CUPCAKE) {//首次获取数据
            FittingWareHouseBean fittingWareHouseBean = (FittingWareHouseBean) baseBean;
            fillClassify(fittingWareHouseBean);
            fillMainBody(fittingWareHouseBean);
        }
    }

    /**
     * fill classify wheel
     *
     * @param fittingWareHouseBean data
     */
    private void fillClassify(FittingWareHouseBean fittingWareHouseBean) {
        partsListEntities = fittingWareHouseBean.getParts_list();
        int partSize = partsListEntities.size();
        String[] wheelData = new String[partSize];
        for (int i = 0; i < partSize; i++) {
            //填充wheel数据源
            wheelData[i] = fittingWareHouseBean.getParts_list().get(i).getParts_name();
        }
        wheelPopupWindow.setCurvedData(wheelData);
    }

    /**
     * fill main body
     *
     * @param fittingWareHouseBean data
     */
    private void fillMainBody(FittingWareHouseBean fittingWareHouseBean) {
        list.clear();
        final List<FittingWareHouseBean.AcceListEntity> acceList = fittingWareHouseBean
                .getAcce_list();
        list.addAll(acceList);
        if (isFirst) {
            String[] suggestions = new String[acceList.size()];
            for (int i = 0; i < acceList.size(); i++) {
                suggestions[i] = acceList.get(i).getAcce_name();
            }
            materialSearchView.setSuggestions(suggestions);
            isFirst = false;
        }
        commonBinderAdapter.notifyDataSetChanged();
    }

    private boolean checkNecessary() {
//        if (TextUtils.isEmpty(classifyId)) {
//            AppTools.showNormalSnackBar(parentView, getString(R.string
//                    .fitting_please_enter_classify));
//            return false;
//        }
        if (acceListEntitySparseArray.size() == 0) {
            AppTools.showNormalSnackBar(parentView, getString(R.string
                    .fitting_please_enter_product));
            return false;
        }
        return true;
    }

    @Override
    public void onCount(int position, int count) {
        FittingWareHouseBean.AcceListEntity acceListEntity = list.get(position);
        acceListEntity.setCount(count);
        commonBinderAdapter.notifyItemChanged(position);
        shopCarPopupWindow.addItem(position, acceListEntity);

        acceListEntitySparseArray.put(position, acceListEntity);

        notifyShopCarCount();
    }

    @Override
    public void onDelete(int position, FittingWareHouseBean.AcceListEntity entity) {
        shopCarPopupWindow.removeItem(position);
        acceListEntitySparseArray.remove(position);

        FittingWareHouseBean.AcceListEntity acceListEntity = list.get(position);
        acceListEntity.setCount(0);
        commonBinderAdapter.notifyDataSetChanged();
        notifyShopCarCount();
    }

    @Override
    public void onCountChange(int position, FittingWareHouseBean.AcceListEntity acceListEntity,
                              SparseArray<FittingWareHouseBean.AcceListEntity>
                                      sparseArray,
                              boolean isDelete) {
        if (!acceListEntity.isExtra()) {
            if (isDelete) {
                acceListEntity.setCount(0);
            }
            list.set(position, acceListEntity);
            commonBinderAdapter.notifyItemChanged(position);
        }
        this.acceListEntitySparseArray = sparseArray.clone();

        notifyShopCarCount();
    }

    @Override
    public void onCountClear() {
        //重置所有item的数量为0
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            int key = acceListEntitySparseArray.keyAt(i);
            final FittingWareHouseBean.AcceListEntity entity = list.get(key);
            entity.setCount(0);
        }
        commonBinderAdapter.notifyDataSetChanged();
        this.acceListEntitySparseArray.clear();
        notifyShopCarCount();
    }

    private void notifyShopCarCount() {
        int allCount = 0;
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            allCount += acceListEntitySparseArray.valueAt(i).getCount();
        }
        fittingWareHouseLayout.tvCount.setText(String.valueOf(allCount));
    }
}
