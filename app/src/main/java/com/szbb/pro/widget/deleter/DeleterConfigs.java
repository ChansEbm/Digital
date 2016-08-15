package com.szbb.pro.widget.deleter;

import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.R;

import java.util.ArrayList;

/**
 * Created by KenChan on 16/5/26.
 */
public class DeleterConfigs {
    protected int deleteBitmapRes = R.mipmap.delete;
    protected int deleterWidth = 100;
    protected int deleterHeight = 100;

    protected AppCompatActivity appCompatActivity = null;

    protected ArrayList<String> alreadyAddPhoto = new ArrayList<>();

    protected ArrayList<String> networkUrls = new ArrayList<>();
    protected int adderRes = R.mipmap.ic_launcher;
    protected boolean hasAdderIcon = false;
    protected int maxPhotoLimit = 5;

    protected int surplusSelectLimit = maxPhotoLimit;
    protected int margin = 10;

    protected int mode = MODE_HAND;

    public static final int MODE_VIEW = 0x001;
    public static final int MODE_HAND = 0x002;

    public DeleterConfigs() {
    }

    public DeleterConfigs(int deleteBitmapRes,
                          int deleterWidth,
                          int deleterHeight,
                          AppCompatActivity appCompatActivity,
                          int adderRes,
                          boolean hasAdderIcon,
                          int maxPhotoLimit,
                          int surplusSelectLimit,
                          int margin,
                          int mode,
                          ArrayList<String> networkUrls
                         ) {
        this.deleteBitmapRes = deleteBitmapRes;
        this.deleterWidth = deleterWidth;
        this.deleterHeight = deleterHeight;
        this.appCompatActivity = appCompatActivity;
        this.adderRes = adderRes;
        this.hasAdderIcon = hasAdderIcon;
        this.maxPhotoLimit = maxPhotoLimit;
        this.surplusSelectLimit = surplusSelectLimit;
        this.margin = margin;
        this.mode = mode;
        this.networkUrls = networkUrls;
    }

    public int getDeleteBitmapRes() {
        return deleteBitmapRes;
    }

    public int getDeleterWidth() {
        return deleterWidth;
    }

    public int getDeleterHeight() {
        return deleterHeight;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public ArrayList<String> getAlreadyAddPhoto() {
        return alreadyAddPhoto;
    }

    public int getAdderRes() {
        return adderRes;
    }

    public boolean isHasAdderIcon() {
        return hasAdderIcon;
    }

    public int getMaxPhotoLimit() {
        return maxPhotoLimit;
    }

    public int getSurplusSelectLimit() {
        return surplusSelectLimit;
    }

    public int getMargin() {
        return margin;
    }

    public int getMode() {
        return mode;
    }

    public ArrayList<String> getNetworkUrls() {
        return networkUrls;
    }


}
