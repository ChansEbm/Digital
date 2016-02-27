package com.szbb.pro.impl;

import android.util.SparseArray;

/**
 * for the shop car popup window
 * Created by ChanZeeBm on 2016/1/28.
 */
public interface CountChangeCallBack<T> {
    void onCountChange(int position, T t, SparseArray<T> sparseArray, boolean isDelete);

    void onCountClear();
}
