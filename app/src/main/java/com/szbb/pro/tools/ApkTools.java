package com.szbb.pro.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by KenChan on 16/6/12.
 */
public class ApkTools {
    public static String getUpdateApkFile(@NonNull Context context) {
        FileTools fileTools = new FileTools();
        String downloadCacheDir = fileTools.getDownloadCacheDir();
        File file = new File(downloadCacheDir);

        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".apk");
            }
        });
        if (files == null || files.length == 0)
            return "";

        for (File f : files) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageArchiveInfo(f.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
            int versionCode = info.versionCode;
            if (versionCode > MiscUtils.getAppVersionCode(context)) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }


}
