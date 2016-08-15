package com.szbb.pro.factory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.szbb.pro.tools.LogTools;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;

/**
 * Created by KenChan on 16/7/9.
 */
public class TencentMapper
        extends Mapper {

    @Override
    public Mapper moveToSpecialPoint(@NonNull View view, double lat, double lng) {
        if (!(view instanceof MapView)) {
            throw new RuntimeException(
                    "error class,should be com.tencent.tencentmap.mapsdk.map.MapView");
        }
        MapView mapView = (MapView) view;
        mapView.getMap()
               .setCenter(new LatLng(lat, lng));
        return this;
    }

    @Override
    public Mapper locate(Context context, LocationListener locationListener) {
        if (!(locationListener instanceof TencentLocationListener)) {
            throw new RuntimeException("error listener,should be TencentLocationListener");
        }
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setAllowCache(true);
        TencentLocationManager manager = TencentLocationManager.getInstance(context);
        LogTools.w( manager.requestLocationUpdates(request, locationListener));

        return this;
    }

    public Mapper stopLocate(Context context, LocationListener locationListener) {
        TencentLocationManager manager = TencentLocationManager.getInstance(context);
        manager.removeUpdates(locationListener);
        return this;
    }


}
