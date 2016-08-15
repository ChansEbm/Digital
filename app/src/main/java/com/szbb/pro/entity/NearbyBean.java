package com.szbb.pro.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by ChanZeeBm on 2015/9/10.
 */
public class NearbyBean {
    private int color;
    private Drawable drawable;
    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
