package com.szbb.pro.impl;

/**
 * Created by ChanZeeBm on 2015/9/30.
 */
public interface OnCountListener<T> {
    void onCount(int position, int count);//点击增减的时候回调

    void onDelete(int position, T t);//点击到0的时候回调
}
