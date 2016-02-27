package com.szbb.pro.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 11/19/2015.
 */
public class BitmapCompressTool {

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

    public static Bitmap getRadioBitmap(String path, int targetWidth, int targetHeight) {
        if (TextUtils.isEmpty(path))
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
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
        return bitmap;
    }


}
