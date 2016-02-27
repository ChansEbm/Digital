package com.szbb.pro.tools;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ChanZeeBm on 2015/10/26.
 */
public class WindowManagerTools {

    public static void setWindowBackground(AppCompatActivity appCompatActivity, float alpha) {
        Window window = appCompatActivity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }
}
