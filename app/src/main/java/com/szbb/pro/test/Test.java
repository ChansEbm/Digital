package com.szbb.pro.test;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Created by ChanZeeBm on 2015/10/12.
 */
public class Test extends BaseObservable {
    private String text;

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
