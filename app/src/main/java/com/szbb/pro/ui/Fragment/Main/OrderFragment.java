package com.szbb.pro.ui.Fragment.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.adapters.TabFragmentAdapter;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.databinding.FgmOrderBinding;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.Fragment.Order.NewOrderFragment;
import com.szbb.pro.ui.Fragment.Order.PendingFragment;
import com.szbb.pro.ui.Fragment.Order.WaitAccountFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/16.
 */
public class OrderFragment extends BaseFgm implements UpdateUIListener {
    private FgmOrderBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabFragmentAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] tabs;
    private UpdateUIBroadcast broadcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast, AppKeyMap.APPOINTMENT_CLIENT_ACTION);

    }

    @Override
    protected void initViews() {
        binding = (FgmOrderBinding) viewDataBinding;
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;

        initTabs();
        initFragments();

        adapter = new TabFragmentAdapter(getChildFragmentManager(), fragmentList, tabs);
    }

    @Override
    protected void initEvents() {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_order;
    }

    private void initTabs() {
        tabs = getActivity().getResources().getStringArray(R.array.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]), true);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
    }

    private void initFragments() {
        fragmentList.add(new NewOrderFragment());
        fragmentList.add(new PendingFragment());
        fragmentList.add(new WaitAccountFragment());
    }

    @Override
    public void uiUpData(Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppKeyMap.APPOINTMENT_CLIENT_ACTION)) {
            viewPager.setCurrentItem(1);
        } else if (action.equals(AppKeyMap.WAITING_COST_ACTION)) {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppTools.unregisterBroadcast(broadcast);
    }
}
