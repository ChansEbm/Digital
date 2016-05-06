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
import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class DistrictActivity extends BaseAty<BaseBean, AreaListBean.ListEntity> implements
        BinderOnItemClickListener {
    private RecyclerView recyclerView;
    private CityDistrictLayout cityDistrictLayout;

    private String provinceId = "";
    private String province = "";
    private String cityId = "";
    private String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityDistrictLayout = (CityDistrictLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.district);
        EventBus.getDefault().register(this);
        recyclerView = cityDistrictLayout.recyclerView;
        if (getIntent() != null) {
            provinceId = getIntent().getStringExtra("provinceId");
            province = getIntent().getStringExtra("province");
            city = getIntent().getStringExtra("city");
            cityId = getIntent().getStringExtra("cityId");
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
        networkModel.areaList(cityId, null);
    }

    @Override
    protected void noNetworkStatus() {

    }

    public void onEvent(AreaEvent areaEvent) {
        AppTools.removeSingleActivity(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.aty_city_district;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        AreaListBean.ListEntity listEntity = (AreaListBean.ListEntity) list.get(pos);
        Intent intent = new Intent();
        intent.putExtra("province", province);
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("cityId", cityId);
        intent.putExtra("city", city);
        intent.putExtra("districtId", listEntity.getId());
        intent.putExtra("district", listEntity.getName());
        if (listEntity.isHasChild()) {
            intent.setClass(this, StreetActivity.class);
        } else {
            EventBus.getDefault().post(new AreaEvent(intent));
        }
        startActivity(intent);
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
