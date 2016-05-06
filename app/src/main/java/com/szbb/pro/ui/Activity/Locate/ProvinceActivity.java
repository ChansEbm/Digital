package com.szbb.pro.ui.activity.locate;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szbb.pro.ItemAreaLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.AtyDistrictBinding;
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
public class ProvinceActivity extends BaseAty<BaseBean, AreaListBean.ListEntity> implements
        BinderOnItemClickListener {
    private AtyDistrictBinding binding;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = (AtyDistrictBinding) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.province);
        EventBus.getDefault().register(this);
        recyclerView = binding.recyclerView;
        list = new ArrayList<>();
        commonBinderAdapter = new CommonBinderAdapter<AreaListBean.ListEntity>(this, R.layout
                .item_area,
                list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, AreaListBean.ListEntity areaListBean) {
                ((ItemAreaLayout) viewDataBinding).setArea(areaListBean);
            }
        };
    }

    public void onEvent(AreaEvent areaEvent) {
        AppTools.removeSingleActivity(this);
    }

    @Override
    protected void initEvents() {
        commonBinderAdapter.setBinderOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());

        networkModel.areaList("", null);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_district;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onBinderItemClick(View view, int pos) {
        AreaListBean.ListEntity listEntity = list.get(pos);
        if (listEntity.isHasChild()) {
            Intent intent = new Intent().putExtra("provinceId", listEntity.getId()).putExtra
                    ("province", listEntity.getName()).setClass(this, CityActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBinderItemLongClick(View view, int pos) {

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
