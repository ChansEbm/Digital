package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.ShopCarLayout;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.databinding.PopupShopCarLayout;
import com.szbb.pro.entity.Fittings.FittingWareHouseBean;
import com.szbb.pro.impl.CountChangeCallBack;
import com.szbb.pro.impl.OnCountListener;
import com.szbb.pro.tools.AppTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/25.
 */
public class ShopCarPopupWindow extends BasePopupWindow implements
        OnCountListener<FittingWareHouseBean.AcceListEntity> {
    private PopupShopCarLayout shopCarLayout;
    private SparseArray<FittingWareHouseBean.AcceListEntity> acceListEntitySparseArray = new
            SparseArray<>();
    private List<FittingWareHouseBean.AcceListEntity> shopCarBeanList = new ArrayList<>();
    private CommonBinderAdapter<FittingWareHouseBean.AcceListEntity> shopCarBeanCommonAdapter;

    private RecyclerView recyclerView;
    private CountChangeCallBack<FittingWareHouseBean.AcceListEntity> countChangeCallBack;

    public ShopCarPopupWindow(Context context) {
        super(context);
        shopCarLayout = (PopupShopCarLayout) viewDataBinding;
        initViews();
        initEvents();
    }

    private void initViews() {
        recyclerView = shopCarLayout.recyclerView;
        shopCarBeanCommonAdapter = new CommonBinderAdapter<FittingWareHouseBean.AcceListEntity>
                (context, R.layout.item_shop_car, shopCarBeanList) {

            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, FittingWareHouseBean.AcceListEntity acceListEntity) {
                ShopCarLayout itemShopCarLayout = (ShopCarLayout) viewDataBinding;
                itemShopCarLayout.countView.setCount(acceListEntity.getCount());
                itemShopCarLayout.countView.setPosition(position);
                itemShopCarLayout.countView.setT(acceListEntity);
                itemShopCarLayout.countView.setOnCountListener(ShopCarPopupWindow.this);
                itemShopCarLayout.simpleDraweeView.setImageURI(Uri.parse
                        (acceListEntity.getAcce_thumb()));
                itemShopCarLayout.setShopCar(acceListEntity);
            }
        };
    }

    private void initEvents() {
        shopCarLayout.tvClear.setOnClickListener(this);

        recyclerView.setAdapter(shopCarBeanCommonAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(AppTools.defaultHorizontalDecoration());
        recyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager
                (context));
        int maxHeight = AppTools.getScreenHeight() - AppTools.getScreenHeight() / 3;
        setHeight(maxHeight);
    }

    public void addItem(int position, FittingWareHouseBean.AcceListEntity acceListEntity) {
        acceListEntitySparseArray.put(position, acceListEntity);
        notifyAdapter();
    }

    public void removeItem(int position) {
        acceListEntitySparseArray.remove(position);
        notifyAdapter();
    }

    private void notifyAdapter() {
        shopCarBeanList.clear();
        for (int i = 0; i < acceListEntitySparseArray.size(); i++) {
            FittingWareHouseBean.AcceListEntity acceListEntity = acceListEntitySparseArray
                    .valueAt(i);
            if (!shopCarBeanList.contains(acceListEntity))
                shopCarBeanList.add(acceListEntity);
        }
        shopCarBeanCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (shopCarBeanList.isEmpty())
            return;
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_shop_car;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                shopCarBeanList.clear();
                acceListEntitySparseArray.clear();
                shopCarBeanCommonAdapter.notifyDataSetChanged();
                if (countChangeCallBack != null)
                    countChangeCallBack.onCountClear();
                break;
        }
    }

    @Override
    public void onCount(int position, int count) {
        FittingWareHouseBean.AcceListEntity acceListEntity = shopCarBeanList.get(position);
        acceListEntity.setCount(count);
        int key = acceListEntitySparseArray.keyAt(position);
        if (countChangeCallBack != null)
            countChangeCallBack.onCountChange(key, acceListEntity, acceListEntitySparseArray,
                    false);
        shopCarBeanCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position, FittingWareHouseBean.AcceListEntity acceListEntity) {
        shopCarBeanList.remove(acceListEntity);
        shopCarBeanCommonAdapter.notifyDataSetChanged();
        int key = acceListEntitySparseArray.keyAt(position);
        acceListEntitySparseArray.removeAt(position);
        countChangeCallBack.onCountChange(key, acceListEntity, acceListEntitySparseArray, true);

    }

    public void setCountChangeCallBack(CountChangeCallBack countChangeCallBack) {
        this.countChangeCallBack = countChangeCallBack;
    }
}