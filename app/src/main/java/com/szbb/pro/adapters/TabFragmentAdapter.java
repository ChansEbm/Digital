package com.szbb.pro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public class TabFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private String[] tabs;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] tabs) {
        super(fm);
        this.fragmentList = fragmentList;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
