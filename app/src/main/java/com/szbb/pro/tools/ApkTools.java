package com.szbb.pro.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.dialog.MessageDialog;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by KenChan on 16/6/12.
 */
public class ApkTools {
    public static String getUpdateApkFile (@NonNull Context context) {
        FileTools fileTools = new FileTools();
        String downloadCacheDir = fileTools.getDownloadCacheDir();
        File file = new File(downloadCacheDir);

        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept (File pathname) {
                String name = pathname.getName();
                return name.endsWith(".apk");
            }
        });
        if (files == null || files.length == 0) { return ""; }

        for (File f : files) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageArchiveInfo(f.getAbsolutePath(),
                                                                    PackageManager.GET_ACTIVITIES);
            int versionCode = info.versionCode;
            if (versionCode > MiscUtils.getAppVersionCode(context)) {
                return f.getAbsolutePath();
            }
        }
        return "";
    }


    public static boolean installAPK (final Context context) {
        if (!TextUtils.isEmpty(ApkTools.getUpdateApkFile(context))) {
            final MessageDialog messageDialog = new MessageDialog(context);
            messageDialog.setTitle("发现新版本")
                         .setPositiveButton("安装",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick (View v) {
                                                    Intent intent = new Intent(
                                                            Intent.ACTION_VIEW).setFlags(
                                                            Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                               .setDataAndTypeAndNormalize(
                                                                                       Uri.fromFile(
                                                                                               new File(
                                                                                                       ApkTools.getUpdateApkFile(
                                                                                                               context))),
                                                                                       "application/vnd.android.package-archive");
                                                    context.startActivity(intent);
                                                    messageDialog.dismiss();
                                                }
                                            })
                         .setNegativeButton("取消",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick (View v) {
                                                    messageDialog.dismiss();
                                                }
                                            })
                         .setMessage("新版本已下载完毕,需要安装吗")
                         .show();
        }
        return !TextUtils.isEmpty(ApkTools.getUpdateApkFile(context));
    }

}
