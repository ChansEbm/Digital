package com.szbb.pro.manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.szbb.pro.entity.vip.CheckUpdateBean;
import com.szbb.pro.service.DownloadService;
import com.szbb.pro.tools.ApkTools;

/**
 * Created by Administrator on 2016/8/15.
 */
public class AppUpdaterManager {
    public static boolean isNeedUpdate (@NonNull Context context,
                                        @NonNull CheckUpdateBean checkUpdateBean) {
        String versionCode = checkUpdateBean.getVersion_code();
        String url = checkUpdateBean.getUrl();
        String updateApkFile = ApkTools.getUpdateApkFile(context);

        boolean needUpdate = TextUtils.equals(versionCode,
                                              "1");
        boolean nonApkFile = updateApkFile.isEmpty();
        boolean urlNonEmpty = !TextUtils.isEmpty(url);
        return needUpdate && nonApkFile && urlNonEmpty;
    }

    public static void startDownloadApkFile (Context context, String downloadUrl) {
        Intent intent = new Intent(context,
                                   DownloadService.class).putExtra("uri",
                                                                   downloadUrl);
        context.startService(intent);
    }
}
