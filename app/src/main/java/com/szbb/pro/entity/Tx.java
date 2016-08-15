package com.szbb.pro.entity;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/18.
 */
public class Tx extends BaseObservable {
    private int s;
    private String i;

    public Tx(int s, String i) {
        this.s = s;
        this.i = i;
    }

}
