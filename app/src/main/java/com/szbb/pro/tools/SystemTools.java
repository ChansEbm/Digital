package com.szbb.pro.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.szbb.pro.impl.TakePhotoCallBack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by ChanZeeBm on 2015/10/21.
 */
public class SystemTools {

    //拨号
    public static Intent CALL(String phoneNum) {
        Context context;

        return new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phoneNum));
    }




    //打开系统设置
    public static Intent SETTING() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    //打开wifi设置
    public static Intent WIFI() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }


    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }




}
