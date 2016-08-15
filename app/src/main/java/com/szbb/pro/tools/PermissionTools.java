package com.szbb.pro.tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by KenChan on 16/6/14.
 */
public class PermissionTools {

    public static boolean alreadyHasPermission(Activity activity,
                                               int requestCode,
                                               String... permissions) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }
        for (String permission : permissions) {
            int checkState = ContextCompat.checkSelfPermission(activity,
                                                               permission);
            if (checkState != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                                                  permissions,
                                                  requestCode);
                return false;
            }
        }

        return true;
    }
}
