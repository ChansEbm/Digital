package com.szbb.pro.widget.deleter;

import android.content.Context;
import android.content.Intent;

/**
 * Created by KenChan on 16/5/27.
 */
public class CameraUtils {
    private static CameraCallback callback;
    private static Context context;
    private static int requestCode = 0;


    public static CameraCallback getCallback() {
        return callback;
    }

    public static void openCamera(Context context, CameraCallback cameraCallback, int requestCode) {
        if (context == null || cameraCallback == null)
            throw new RuntimeException("context or cameraCallback cannot be null");
        CameraUtils.context = context;
        CameraUtils.callback = cameraCallback;
        CameraUtils.requestCode = requestCode;
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void done(String imagePath) {
        getCallback().onPhotoSuccess(imagePath, requestCode);
    }
}
