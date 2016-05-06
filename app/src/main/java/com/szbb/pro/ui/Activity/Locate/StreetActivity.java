package com.szbb.pro.ui.activity.locate;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.CityDistrictLayout;
import com.szbb.pro.ItemAreaLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Base.BaseBean;
import com.szbb.pro.entity.EventBus.AreaEvent;
import com.szbb.pro.entity.Login.AreaListBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class StreetActivity extends BaseAty<BaseBean, AreaListBean.ListEntity> {

    private RecyclerView recyclerView;
    private CityDistrictLayout cityDistrictLayout;

    private String provinceId = "";
    private String province = "";
    private String cityId = "";
    private String city = "";
    private String districtId = "";
    private String district = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityDistrictLayout = (CityDistrictLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.street);
        EventBus.getDefault().register(this);
        recyclerView = cityDistrictLayout.recyclerView;
        if (getIntent() != null) {
            provinceId = getIntent().getStringExtra("provinceId");
            province = getIntent().getStringExtra("province");
            city = getIntent().getStringExtra("city");
            cityId = getIntent().getStringExtra("cityId");
            districtId = getIntent().getStringExtra("districtId");
            district = getIntent().getStringExtra("district");
        }
        list = new ArrayList<>();

        commonBinderAdapter = new CommonBinderAdapter<AreaListBean.ListEntity>(this, R.layout
                .item_area, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, AreaListBean.ListEntity listEntity) {
                ((ItemAreaLayout) viewDataBinding).setArea(listEntity);
            }
        };

    }

    @Override
    protected void initEvents() {
        commonBinderAdapter.setBinderOnItemClickListener(this);
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        networkModel.areaList(districtId, null);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_city_district;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    public void onEvent(AreaEvent areaEvent) {
        AppTools.removeSingleActivity(this);
    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        super.onBinderItemClick(view, pos);
        AreaListBean.ListEntity listEntity = list.get(pos);
        Intent intent = new Intent();
        intent.putExtra("province", province);
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("cityId", cityId);
        intent.putExtra("city", city);
        intent.putExtra("districtId", districtId);
        intent.putExtra("district", district);
        intent.putExtra("streetId", listEntity.getId());
        intent.putExtra("street", listEntity.getName());
        EventBus.getDefault().post(new AreaEvent(intent));
    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        AreaListBean areaListBean = (AreaListBean) baseBean;
        this.list.addAll(areaListBean.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
