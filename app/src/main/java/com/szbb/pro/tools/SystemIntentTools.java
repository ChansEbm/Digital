package com.szbb.pro.tools;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * Created by ChanZeeBm on 2015/10/21.
 */
public class SystemIntentTools {

    //拨号
    public static Intent CALL(String phoneNum) {
        return new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phoneNum));
    }

    public static Intent CAMERA() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }


    //打开相册
    public static Intent PICTURE() {
        return new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
    }

    //打开系统设置
    public static Intent SETTING() {
        return new Intent(Settings.ACTION_APN_SETTINGS);
    }

    //打开wifi设置
    public static Intent WIFI() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }

}
