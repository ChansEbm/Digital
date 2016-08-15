package com.szbb.pro.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.impl.TakePhotoCallBack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.finalteam.galleryfinal.GalleryFinal;

/**
 * Created by Administrator on 11/19/2015.
 */
//照相辅助类
public class CameraTools {
    private File imageFile;


    public void takePhoto(AppCompatActivity appCompatActivity, int requestCode) {

        PackageManager pm = appCompatActivity.getPackageManager();
        boolean cameraFeature = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!cameraFeature) {
            AppTools.showNormalSnackBar(appCompatActivity.getWindow().getDecorView(), "你还没有打开相机权限");
            return;
        }
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (createPhotoTempFile() != null) {
            imageFile = createPhotoTempFile();
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            appCompatActivity.startActivityForResult(takeIntent, requestCode);
        }
    }

    private static File createPhotoTempFile() {
        @SuppressLint("SimpleDateFormat")
        String tempName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileName = "JPEG_" + tempName + "_JPG";
        File strongDir = new File(AppTools.getPictureCacheDir());
        try {
            return File.createTempFile(fileName, ".jpg", strongDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPhotoPath(int resultCode) {
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }
        if (imageFile != null) {
            BitmapCompressTool.getRadioBitmap(imageFile.getAbsolutePath(), 500, 500);
            return imageFile.getAbsolutePath();
        }
        return "";
    }


}
