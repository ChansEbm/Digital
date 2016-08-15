package com.szbb.pro.widget.deleter;

import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.R;

import java.util.ArrayList;

public class DeleterConfigsBuilder {
    private int deleteBitmapRes = R.mipmap.ic_cancel;
    private int deleterWidth = 100;
    private int deleterHeight = 100;
    private AppCompatActivity appCompatActivity;
    private int adderRes = R.mipmap.ic_add_default;
    private boolean hasAdderIcon = true;
    private int maxPhotoLimit = 8;
    private int surplusSelectLimit = maxPhotoLimit;
    private int margin = 15;
    private int mode = DeleterConfigs.MODE_HAND;
    private ArrayList<String> networkUrls = new ArrayList<>();

    public DeleterConfigsBuilder setDeleteBitmapRes(int deleteBitmapRes) {
        this.deleteBitmapRes = deleteBitmapRes;
        return this;
    }

    public DeleterConfigsBuilder setDeleterWidth(int deleterWidth) {
        this.deleterWidth = deleterWidth;
        return this;
    }

    public DeleterConfigsBuilder setDeleterHeight(int deleterHeight) {
        this.deleterHeight = deleterHeight;
        return this;
    }

    public DeleterConfigsBuilder setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        return this;
    }

    public DeleterConfigsBuilder setAdderRes(int adderRes) {
        this.adderRes = adderRes;
        return this;
    }

    public DeleterConfigsBuilder setHasAdderIcon(boolean hasAdderIcon) {
        this.hasAdderIcon = hasAdderIcon;
        return this;
    }

    public DeleterConfigsBuilder setMaxPhotoLimit(int maxPhotoLimit) {
        this.maxPhotoLimit = maxPhotoLimit;
        return this;
    }

    public DeleterConfigsBuilder setSurplusSelectLimit(int surplusSelectLimit) {
        this.surplusSelectLimit = surplusSelectLimit;
        return this;
    }

    public DeleterConfigsBuilder setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public DeleterConfigsBuilder setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public DeleterConfigsBuilder setNetworkUrls(ArrayList<String> networkUrls) {
        this.networkUrls = networkUrls;
        return this;
    }

    public DeleterConfigs createDeleterConfigs() {
        return new DeleterConfigs(deleteBitmapRes,
                                  deleterWidth,
                                  deleterHeight,
                                  appCompatActivity,
                                  adderRes,
                                  hasAdderIcon,
                                  maxPhotoLimit,
                                  surplusSelectLimit,
                                  margin,
                                  mode,
                                  networkUrls);
    }
}