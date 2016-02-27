package com.szbb.pro.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by Administrator on 11/18/2015.
 */
public class FileSaveTools {
    private static Context context;
    private File file;

    public static FileSaveTools getInstance() {
        return FileSaveInstance.fileSaveTools;
    }

    public void init(Context context) {
        FileSaveTools.context = context;
    }

    /**
     * 保存图片到手机
     *
     * @param bitmap 要保存的位图
     */
    public FileSaveTools savePicture(@NonNull Bitmap bitmap) {
        String filePath = getPictureCacheDir() + stringToMD5(AppTools.formatTime(new Date())) + "" +
                ".png";
        file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);//新建输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);//压缩写入
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();//刷新流
                    fos.close();//关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * 通过Uri保存图片
     *
     * @param uri
     */
    public FileSaveTools savePicture(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            savePicture(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getFilePath() {
        if (file != null)
            return file.getPath();
        return "";
    }

    /**
     * String 转换为MD5 避免手机不适配的字符格式
     *
     * @param string 要转换的字符
     * @return
     */
    private String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 获取图片缓存目录
     *
     * @return 返回图片缓存目录
     */
    public String getPictureCacheDir() {
        String picturePath = SDCardTools.getSDCardPosition() + "Digital" +
                File.separator + "pictureCache";
        File file = new File(picturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator;
    }

    private static class FileSaveInstance {
        public final static FileSaveTools fileSaveTools = new FileSaveTools();
    }
}
