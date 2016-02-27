package com.szbb.pro.ui.Activity.Locate;

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
import com.szbb.pro.entity.EventBus.AreaEvent;
import com.szbb.pro.entity.Login.AreaListBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Activity.Login.CompleteInfoActivity;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class CityActivity extends BaseAty implements BinderOnItemClickListener {
    private RecyclerView recyclerView;
    private CityDistrictLayout cityDistrictLayout;
    private String provinceId = "";
    private String province = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityDistrictLayout = (CityDistrictLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.city);
        EventBus.getDefault().register(this);
        recyclerView = cityDistrictLayout.recyclerView;
        if (getIntent() != null) {
            this.provinceId = getIntent().getStringExtra("provinceId");
            this.province = getIntent().getStringExtra("province");
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
        networkModel.areaList(provinceId, null);
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
        AreaListBean.ListEntity listEntity = (AreaListBean.ListEntity) list.get(pos);
        Intent intent = new Intent();
        intent.putExtra("province", province);
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("cityId", listEntity.getId());
        intent.putExtra("city", listEntity.getName());

        if (listEntity.isHasChild()) {
            intent.setClass(this, DistrictActivity.class);
        } else {
            EventBus.getDefault().post(new AreaEvent(intent));
        }
        startActivity(intent);

    }

    @Override
    public void onBinderItemLongClick(View view, int pos) {

    }

    @Override
    public void onJsonObjectSuccess(Object o, NetworkParams paramsCode) {
        AreaListBean areaListBean = (AreaListBean) o;
        this.list.addAll(areaListBean.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
