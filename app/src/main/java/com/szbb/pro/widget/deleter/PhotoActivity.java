package com.szbb.pro.widget.deleter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.tools.ActivityManager;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.PermissionTools;

import java.io.File;

/**
 * Created by KenChan on 16/5/27.
 */
public class PhotoActivity
        extends AppCompatActivity {
    private final static int REQUEST_CODE = 9394;
    private File finaFile = null;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager()
                       .addActivity(this);
        if (PermissionTools.alreadyHasPermission(this, 1, android.Manifest.permission.CAMERA)) {
            startCamera();
        }
    }

    private void startCamera () {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        finaFile = new File(AppTools.getPictureCacheDir() + System.currentTimeMillis() +
                            ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(finaFile));
        startActivityForResult(intent,
                               REQUEST_CODE);
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,
                               resultCode,
                               data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String absolutePath = finaFile.getAbsolutePath();
            CameraUtils.done(absolutePath);
        }
        ActivityManager.getActivityManager()
                       .finishActivity(PhotoActivity.this);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        if (requestCode == 1 && permissions[0].equals(android.Manifest.permission.CAMERA) &&
            grantResults[0] ==
            PackageInfo.REQUESTED_PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityManager.getActivityManager()
                           .finishActivity();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager()
                       .finishActivity();
    }
}
