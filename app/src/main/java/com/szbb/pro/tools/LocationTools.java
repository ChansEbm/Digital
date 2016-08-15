package com.szbb.pro.tools;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.szbb.pro.AppKeyMap;

/**
 * Created by ChanZeeBm on 2015/9/17.
 */
public class LocationTools {
    private LocationClient locationClient;
    private Context context;

    public LocationTools(Context context, BDLocationListener bdLocationListener) {
        this.context = context;
        if (!checkNetworkState()) {
            return;
        }
        locationClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);//每10000毫秒扫描一次位置信息
        locationClient.registerLocationListener(bdLocationListener);
        locationClient.setLocOption(option);
    }

    public void start() {
        if (locationClient == null) {
            return;
        }
        if (checkNetworkState()) {
            if (locationClient.isStarted()) {
                request();
                return;
            }
            locationClient.start();

        }
    }

    public void stop() {
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    private void request() {
        if (checkNetworkState()) {
            if (locationClient.isStarted()) {
                locationClient.requestLocation();
            }
        }
    }

    private boolean checkNetworkState() {
        if (!CheckNetworkTools.isNetworkAvailable(context)) {
            Toast.makeText(context,
                           "请先激活网络",
                           Toast.LENGTH_SHORT)
                 .show();
            AppTools.sendBroadcast(new Bundle(),
                                   AppKeyMap.NO_NETWORK_ACTION);
            return false;
        }
        return true;
    }
}
