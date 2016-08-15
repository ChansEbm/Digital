package com.szbb.pro.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 11/19/2015.
 */
public class BitmapCompressTool {


    public static String getRadioBitmapPath(@NonNull String path, int targetWidth, int
            targetHeight) {
        Bitmap bitmap = scaleBitmap(path,
                                    targetWidth,
                                    targetHeight);
        String osPath = getOsPath(path);
        compressBitmap(bitmap,
                       osPath);
        return osPath;
    }

    public static Bitmap getRadioBitmap(@NonNull String path, int targetWidth, int targetHeight) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Bitmap bitmap = scaleBitmap(path,
                                    targetWidth,
                                    targetHeight);
        String osPath = getOsPath(path);
        return compressBitmap(bitmap,
                              osPath);
    }

    private static Bitmap compressBitmap(Bitmap bitmap, String osPath) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(osPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG,
                            90,
                            fileOutputStream);
            return rotateBitmap(bitmap,
                                osPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    private static Bitmap scaleBitmap(@NonNull String path, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,
                                 options);
        options.inSampleSize = calculateSize(options,
                                             targetWidth,
                                             targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,
                                        options);
    }

    private static int calculateSize(BitmapFactory.Options options, int width, int height) {
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int simpleSize = 1;
        if (bitmapWidth > width || bitmapHeight > height) {
            int widthRadio = Math.round(bitmapWidth / width);
            int heightRadio = Math.round(bitmapHeight / height);
            simpleSize = widthRadio > heightRadio ? widthRadio : heightRadio;
        }
        return simpleSize;
    }

    private static String getOsPath(String wholePath) {
        FileTools fileTools = new FileTools();
        String osPath;
        osPath = fileTools.isPictureCacheDir(wholePath) ? wholePath : fileTools.getPictureCacheDir()
                + System.currentTimeMillis() + ".jpg";
        return osPath;
    }

    private static Bitmap rotateBitmap(@NonNull Bitmap bitmap, @NonNull String path) {
        int degree = getBitmapDegree(path);
        Log.i("digital",
              "rotateBitmap: " + degree);
        if (degree != 0) {
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            matrix.setRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap,
                                         0,
                                         0,
                                         width,
                                         height,
                                         matrix,
                                         true);

        }
        return bitmap;
    }


    private static int getBitmapDegree(@NonNull String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        try {
            ExifInterface ei = new ExifInterface(path);
            int attributeInt = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                  ExifInterface.ORIENTATION_NORMAL);
            switch (attributeInt) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
