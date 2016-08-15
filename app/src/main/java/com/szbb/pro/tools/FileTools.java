package com.szbb.pro.tools;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 11/18/2015.
 */
public class FileTools {
    private static Context context;
    private File file;

    public static FileTools getInstance() {
        return FileSaveInstance.FILE_TOOLS;
    }

    public void init(Context context) {
        FileTools.context = context;
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
            hash = MessageDigest.getInstance("MD5")
                                .digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
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
        return getSpecDir("pictureCache" + File.separator);
    }

    public boolean isPictureCacheDir(String wholePath) {
        if (isFileNameValid(wholePath)) {
            File file = new File(wholePath);
            String fileParent = file.getParent();
            String substring = getPictureCacheDir().substring(0,
                                                              getPictureCacheDir().length() - 1);
            return fileParent.equals(substring);
        }
        return false;
    }

    private boolean isFileNameValid(String wholePath) {
        File f = new File(wholePath);
        try {
            File canonicalFile = f.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getDownloadCacheDir() {
        return getSpecDir("Download" + File.separator);
    }

    private String getSpecDir(String dirName) {
        File file = new File(getAPPCacheDir() + dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath() + File.separator;
    }

    public boolean isFileOverLimitSize(String filePath, long limitSize) {
        File file = new File(filePath);
        if (!file.exists()) {
            LogTools.w("无效文件路径!!!");
            return false;
        }
        long fileLength = file.length() / 1024;
        if (fileLength > limitSize) {
            return true;
        }
        return false;
    }

    public String getAPPCacheDir() {
        File file = new File(SDCardTools.getSDCardPosition() + "Digital");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath() + File.separator;
    }


    private static class FileSaveInstance {
        public final static FileTools FILE_TOOLS = new FileTools();
    }
}
