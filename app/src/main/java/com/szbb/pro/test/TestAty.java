package com.szbb.pro.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.TestLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.Tx;
import com.szbb.pro.impl.AreaCallBack;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.library.dialog.WheelViewDialog;
import com.szbb.pro.widget.AreaPicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public class TestAty extends BaseAty implements
        UpdateUIListener, AreaCallBack {
    Toolbar toolbar;
    TestLayout testLayout;
    DrawerLayout drawerLayout;
    List<String> p = new ArrayList<>();
    List<List<String>> c = new ArrayList<>();
    AreaPicker areaPicker;
    WheelViewDialog wheelViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLayout = (TestLayout) viewDataBinding;
        wheelViewDialog = new WheelViewDialog(this, WheelViewDialog.TWO_LINKAGE);
        Tx tx = new Tx();
        tx.setColor("2");
        testLayout.setTest(tx);
        testLayout.executePendingBindings();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void initViews() {

    }


    @Override
    protected void initEvents() {

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

        wheelViewDialog.show();
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
}
