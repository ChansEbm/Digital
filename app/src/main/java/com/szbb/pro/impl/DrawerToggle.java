package com.szbb.pro.impl;

import android.content.res.Configuration;
import android.view.MenuItem;

/**
 * Created by ChanZeeBm on 2016/2/17.
 */
public interface DrawerToggle {
    public void syncState();
    public void onConfigurationChanged(Configuration config);
    public boolean onOptionsItemSelected(MenuItem menuItem);
    void release();
}
