package com.szbb.pro.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.BR;

/**
 * Created by ChanZeeBm on 2015/10/23.
 */
public class NearbyContentBean extends BaseObservable {
    private String title;
    private String subTitle;
    private String detailMsg;
    private String totalPrice;
    private String bounds;

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    @Bindable
    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
        notifyPropertyChanged(BR.detailMsg);
    }

    @Bindable
    public String getTotalPrice() {
        return "¥" + totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        notifyPropertyChanged(BR.totalPrice);
    }

    @Bindable
    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
        notifyPropertyChanged(BR.bounds);
    }
}
