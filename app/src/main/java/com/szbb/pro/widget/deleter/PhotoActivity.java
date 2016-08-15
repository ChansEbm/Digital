package com.szbb.pro.widget.deleter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.szbb.pro.tools.ActivityManager;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.DataFormatter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KenChan on 16/5/27.
 */
public class PhotoActivity
        extends AppCompatActivity {
    private final static int REQUEST_CODE = 9394;
    private File finaFile = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager()
                       .addActivity(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory
//                (Environment.DIRECTORY_PICTURES);
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss",
//                                                     Locale.CHINA);
        finaFile = new File(AppTools.getPictureCacheDir() + System.currentTimeMillis() +
                                    ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(finaFile));
        startActivityForResult(intent,
                               REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}
