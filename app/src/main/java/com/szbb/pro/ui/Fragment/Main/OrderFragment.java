package com.szbb.pro.ui.fragment.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.adapters.TabFragmentAdapter;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.broadcast.UpdateUIBroadcast;
import com.szbb.pro.databinding.FgmOrderBinding;
import com.szbb.pro.entity.eventbus.SearchGod;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.ui.fragment.order.NewOrderFragment;
import com.szbb.pro.ui.fragment.order.ServicedFragment;
import com.szbb.pro.ui.fragment.order.WaitAccountFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ChanZeeBm on 2015/9/16.
 */
public class OrderFragment
        extends BaseFgm
        implements UpdateUIListener, MaterialSearchView.OnQueryTextListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabFragmentAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] tabs;
    private UpdateUIBroadcast broadcast;
    private Toolbar toolbar;
    private FgmOrderBinding binding;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcast = new UpdateUIBroadcast();
        broadcast.setListener(this);
        AppTools.registerBroadcast(broadcast,
                                   AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE,
                                   AppKeyMap.APPOINTMENT_CAN_NOT_CONTENT_CLIENT,
                                   AppKeyMap.GRAB_ACTION);
    }

    @Override
    protected void initViews () {
        binding = (FgmOrderBinding) viewDataBinding;
        toolbar = binding.toolBar;
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;

        initTabs();
        initFragments();

        adapter = new TabFragmentAdapter(getChildFragmentManager(),
                                         fragmentList,
                                         tabs);
    }

    @Override
    protected void initEvents () {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                MaterialSearchView searchView = binding.materialSearchView;
                searchView.setOnQueryTextListener(OrderFragment.this);
                searchView.setMenuItem(item);
                if (!searchView.isSearchOpen()) {
                    searchView.showSearch();
                }
                return true;
            }
        });
    }

    @Override
    protected void noNetworkStatus () {

    }

    @Override
    protected void onClick (int id, View view) {

    }

    @Override
    protected int getContentView () {
        return R.layout.fgm_order;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initTabs () {
        tabs = getActivity().getResources()
                            .getStringArray(R.array.tabs_order_name);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab()
                                  .setText(tabs[0]),
                         true);
        tabLayout.addTab(tabLayout.newTab()
                                  .setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab()
                                  .setText(tabs[2]));
    }

    private void initFragments () {
        fragmentList.add(new NewOrderFragment());
        fragmentList.add(new ServicedFragment());
        fragmentList.add(new WaitAccountFragment());
    }

    @Override
    public void uiUpData (Intent intent) {
        super.uiUpData(intent);
        final String action = intent.getAction();
        switch (action) {
            case AppKeyMap.NO_NETWORK_ACTION:
                return;
            case AppKeyMap.REFRESH_AND_JUMPTO_SERVICED_PAGE:
                viewPager.setCurrentItem(1);
                break;
            case AppKeyMap.WAITING_COST_ACTION:
                viewPager.setCurrentItem(2);
                break;
            default:
                viewPager.setCurrentItem(0);
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onDetach () {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        AppTools.unregisterBroadcast(broadcast);
    }

    @Override
    public boolean onQueryTextSubmit (String query) {
        Toast.makeText(getContext(), "搜索中..", Toast.LENGTH_SHORT)
             .show();
        String[] searchFields;
        String trim = query.trim();
        if (!trim.contains(" ")) {
            searchFields = new String[1];
            searchFields[0] = trim;
        } else {
            searchFields = trim.split(" ");
        }
        EventBus.getDefault()
                .post(new SearchGod(viewPager.getCurrentItem(), searchFields));

        return false;
    }

    @Override
    public boolean onQueryTextChange (String newText) {
        return false;
    }
}
