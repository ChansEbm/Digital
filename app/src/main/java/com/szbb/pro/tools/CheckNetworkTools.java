package com.szbb.pro.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by ChanZeeBm on 2015/12/30.
 */
public class CheckNetworkTools {
    /**
     * check network is connected or not
     *
     * @param context context
     * @return true if one of network is Available false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cnnMgr = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cnnMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * check wifi state
     *
     * @param context context
     * @return true if wifi is connected false otherwise
     */
    public static boolean isWifiState(Context context) {
        ConnectivityManager cnnMgr = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cnnMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * help user open wifi without move to setting
     *
     * @param context context
     */
    public static void openWifi(Context context) {
        if (!isWifiState(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }

}
