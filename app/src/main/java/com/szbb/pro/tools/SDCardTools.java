package com.szbb.pro.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 11/18/2015.
 */
public class SDCardTools {
    private static Context context;

    public static void init(Context context) {
        SDCardTools.context = context;
    }

    /**
     * 获取存储路径,如果没sd卡则直接存在手机根目录上
     *
     * @return
     */
    public static String getSDCardPosition() {
        //如果有挂载外置sdcard,则直接返回sdcard根目录
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + File.separator;
        }
        //否则直接用手机自带储存目录
        return Environment.getRootDirectory().getPath() + File.separator;
    }
}
