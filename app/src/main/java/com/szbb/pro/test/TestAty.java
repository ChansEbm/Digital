package com.szbb.pro.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.TestLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.AreaCallBack;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.ViewUtils;

import java.util.List;


/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public class TestAty extends BaseAty<MyOrderBean, MyOrderBean.ListEntity> implements
        UpdateUIListener, AreaCallBack {
    TestLayout testLayout;

    NotificationManagerCompat managerCompat1;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLayout = (TestLayout) viewDataBinding;
//        builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.mipmap.status_icon);
//        builder.setContentTitle("content title");
//        Intent intent = new Intent(this, WalletActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(WalletActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
//                .FLAG_UPDATE_CURRENT);
//        builder.setAutoCancel(true);
//        builder.setContentIntent(pendingIntent);
//        builder.setContentText("content text");
//        builder.setTicker("this is ticker");
//        builder.setColor(getResources().getColor(R.color.theme_primary));
//        managerCompat1 = NotificationManagerCompat.from(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        managerCompat1.notify(1, builder.build());
    }

    @Override
    protected void initViews() {
//        recyclerView = testLayout.recyclerView;
//        fancyIndicator = testLayout.fancyIndicator;
//        fancyIndicator.attachRecyclerView(recyclerView);
//        commonBinderAdapter = new CommonBinderAdapter<MyOrderBean.ListEntity>(this, R
//                .layout.item_new_order, list) {
//            @Override
//            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
//                    position, MyOrderBean.ListEntity listEntity) {
//                ItemNewOrderLayout layout = (ItemNewOrderLayout) viewDataBinding;
//                layout.setOrder(listEntity);
//            }
//        };
    }

    @Override
    protected void initEvents() {
//        recyclerView.setAdapter(commonBinderAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
//                .sizeResId(R.dimen.large_margin_15dp).colorResId(R.color
//                        .color_bg_gravy).build());
//
//        networkModel.myOrderList("1", "", "", NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.test;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void uiUpData(Intent intent) {

    }

    @Override
    public void onSelect(String provinceName, String cityName, int provinceIndex, int cityIndex) {

    }

    @Override
    public List<String> getProvince() {
        return null;
    }

    @Override
    public List<List<String>> getCity() {
        return null;
    }

    @Override
    public void onJsonObjectSuccess(MyOrderBean myOrderBean, NetworkParams paramsCode) {
//        super.onJsonObjectSuccess(myOrderBean, paramsCode);
//        final List<MyOrderBean.ListEntity> list = myOrderBean.getList();
//        this.list.addAll(list);
//        commonBinderAdapter.notifyDataSetChanged();
    }
}
