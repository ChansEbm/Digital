package com.szbb.pro.tools;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by Administrator on 11/19/2015.
 */
//照相辅助类
public class CameraTools {
    private File file;

    public CameraTools takePicture(AppCompatActivity appCompatActivity, int requestCode) {
        Intent intent = SystemIntentTools.CAMERA();//获得相机intent
        String filePosition = AppTools.getPictureCacheDir() + AppTools.stringToMD5(System
                .currentTimeMillis() + "") + ".png";//要保存的路径
        file = new File(filePosition);//要保存的文件
        Uri uri = Uri.fromFile(file);//新建Uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//设置拍照后直接保存到Uri指定路径
        appCompatActivity.startActivityForResult(intent, requestCode);//打开相机
        return this;
    }

    public String getPicturePath() {
        if (file == null)
            throw new NullPointerException("takePicture method no invoke");
        return file.getPath();
    }
}
