package com.szbb.pro.ui.Activity.Vip.Wallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.TransactionDetailLayout;
import com.szbb.pro.adapters.TabFragmentAdapter;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.ui.Fragment.Wallet.IncomeFragment;
import com.szbb.pro.ui.Fragment.Wallet.OtherCostFragment;
import com.szbb.pro.ui.Fragment.Wallet.WithdrawFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易明细
 */
public class TransactionDetailActivity extends BaseAty {
    private TransactionDetailLayout detailLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabFragmentAdapter adapter;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailLayout = (TransactionDetailLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_transaction_detail);
        tabLayout = detailLayout.tabLayout;
        viewPager = detailLayout.viewPager;

        initTabs();
        initFragments();
        adapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments, tabs);
    }


    @Override
    protected void initEvents() {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initFragments() {
        fragments.add(new IncomeFragment());
        fragments.add(new WithdrawFragment());
        fragments.add(new OtherCostFragment());
    }

    private void initTabs() {
        tabs = getResources().getStringArray(R.array.tabs_transaction_name);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]), true);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void onClick(int id, View view) {

    }
}
