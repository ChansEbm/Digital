package com.szbb.pro.tools;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by ChanZeeBm on 2015/9/17.
 */
public class LocationTools {
    private LocationClient locationClient;
    private LocationClientOption option;

    public LocationTools(Context context, BDLocationListener bdLocationListener) {
        locationClient = new LocationClient(context);
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        locationClient.registerLocationListener(bdLocationListener);
        locationClient.setLocOption(option);
    }

    public void start() {
        if (locationClient.isStarted()) {
            request();
            return;
        }
        locationClient.start();
    }

    public void stop() {
        if (locationClient != null)
            locationClient.stop();
    }

    private void request() {
        locationClient.requestLocation();
    }


}
