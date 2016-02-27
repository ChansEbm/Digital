package com.szbb.pro.impl;

import android.view.View;

/**
 * Created by ChanZeeBm on 2015/10/13.
 */
//BinderAdapter适配点击事件
public interface BinderOnItemClickListener {
    void onBinderItemClick(View view, int pos);

    void onBinderItemLongClick(View view, int pos);
}
