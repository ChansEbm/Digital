package com.szbb.pro.impl;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by KenChan on 16/5/24.
 */
public interface TakePhotoCallBack {
    void photoDone(String path,int requestCode);
    AppCompatActivity getAppCompatActivity();
}
