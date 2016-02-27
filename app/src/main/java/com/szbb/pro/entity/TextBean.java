package com.szbb.pro.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import com.szbb.pro.BR;
import com.szbb.pro.R;

import java.io.Serializable;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class TextBean extends BaseObservable implements Serializable {
    private String text = "";
    private int textColor = R.color.color_text_dark;
    private int rootGravity = Gravity.CENTER_VERTICAL;
    private int textGravity = Gravity.CENTER_VERTICAL;
    private Drawable rootBackground;
    private View.OnClickListener onClickListener;

    @Bindable
    public Drawable getRootBackground() {
        return rootBackground;
    }

    public void setRootBackground(Drawable rootBackground) {
        this.rootBackground = rootBackground;
        notifyPropertyChanged(BR.rootBackground);
    }

    @Bindable
    public int getTextGravity() {
        return textGravity;
    }


    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
        notifyPropertyChanged(BR.textGravity);
    }

    @Bindable
    public int getRootGravity() {
        return rootGravity;
    }

    public void setRootGravity(int gravity) {
        this.rootGravity = gravity;
        notifyPropertyChanged(BR.rootGravity);
    }

    @Bindable
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyPropertyChanged(BR.onClickListener);
    }


}
