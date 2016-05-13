package com.szbb.pro.ui.fragment.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.adapters.TabFragmentAdapter;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.databinding.FgmFittingOrderBinding;
import com.szbb.pro.entity.eventbus.FittingNavEvent;
import com.szbb.pro.ui.fragment.fittings.ExpensesApplyFragment;
import com.szbb.pro.ui.fragment.fittings.FittingApplyFragment;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/9/16.
 * 配件订单页面
 */
public class FittingsFragment extends BaseFgm {
    private TabLayout tabLayout;//上方导航栏
    private ViewPager viewPager;//滑动视图
    private FgmFittingOrderBinding fgmFittingOrderBinding;
    private List<Fragment> fragments = new ArrayList<>();//碎片数据
    private TabFragmentAdapter adapter;//适配器
    private String[] tabs;//标题

    @Override
    protected void initViews() {
        fgmFittingOrderBinding = (FgmFittingOrderBinding) viewDataBinding;
        tabLayout = fgmFittingOrderBinding.tabLayout;
        viewPager = fgmFittingOrderBinding.viewPager;
        initTabs();
        initFragments();
        adapter = new TabFragmentAdapter(getChildFragmentManager(), fragments, tabs);
    }

    @Override
    protected void initEvents() {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置导航栏模式
        tabLayout.setTabsFromPagerAdapter(adapter);//从适配器设置Tabs
        tabLayout.setupWithViewPager(viewPager);//导航栏跟滑动视图联动
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        ;//设置监听器

        fgmFittingOrderBinding.imageButton.setOnClickListener(this);
    }

    @Override
    protected void noNetworkStatus() {

    }

    private void initTabs() {
        tabs = getActivity().getResources().getStringArray(R.array.fitting_orders);
        //设置各个Tabs
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]), true);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
    }

    private void initFragments() {
        fragments.add(new FittingApplyFragment());
        fragments.add(new ExpensesApplyFragment());
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.imageButton:
                operateNav();
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_fitting_order;
    }

    private void operateNav() {
        int currentPos = viewPager.getCurrentItem();
        EventBus.getDefault().post(new FittingNavEvent(currentPos));
    }
}
