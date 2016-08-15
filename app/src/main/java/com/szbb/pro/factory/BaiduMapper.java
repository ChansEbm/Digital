package com.szbb.pro.factory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by KenChan on 16/7/9.
 */
public class BaiduMapper
        extends Mapper {

    private LocationClient locationClient;

    @Override
    public Mapper moveToSpecialPoint(@NonNull View view, double lat, double lng) {
        if (!(view instanceof MapView)) {
            throw new RuntimeException("error class,should be com.baidu.mapapi.map.MapView");
        }
        MapView mapView = (MapView) view;
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng));
        mapView.getMap()
               .animateMapStatus(mapStatusUpdate);
        return this;
    }

    @Override
    public Mapper locate(Context context, LocationListener locationListener) {
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.requestLocation();
            return this;
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);//每10000毫秒扫描一次位置信息
        locationClient = new LocationClient(context, option);
        locationClient.registerLocationListener(locationListener);
        return this;
    }

    public Mapper stopLocate() {
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
        }
        return this;
    }

    public void addMyLocation(BaiduMap baiduMap, double lat, double lng, float direction, int
            ownerRes) {
        baiduMap.setMyLocationEnabled(true);
        MyLocationData locationData = new MyLocationData.Builder().latitude(lat)
                                                                  .longitude(lng)
                                                                  .direction(direction)
                                                                  .build();
        baiduMap.setMyLocationData(locationData);
        if (ownerRes != 0) {
            addCustomLocationTag(baiduMap,
                                 ownerRes);
        }
    }

    private void addCustomLocationTag(BaiduMap baiduMap, int ownerRes) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(ownerRes);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration
                                                                             .LocationMode
                                                                             .NORMAL,
                                                                     true,
                                                                     bitmapDescriptor);
        baiduMap.setMyLocationConfigeration(config);
    }

    public Marker addOverlay(@NonNull BaiduMap baiduMap, double lat, double lng, boolean
            draggable, int overLayRes) {
        LatLng latLng = new LatLng(lat,
                                   lng);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(overLayRes);
        OverlayOptions overlayOptions = new MarkerOptions().position(latLng)
                                                           .icon(bitmapDescriptor)
                                                           .draggable(draggable);
        return (Marker) baiduMap.addOverlay(overlayOptions);
    }

    public void addInfoWindow(BaiduMap baiduMap, View showContent, double lat, double lng, int
            contentOffset) {
        InfoWindow infoWindow = new InfoWindow(showContent,
                                               new LatLng(lat,
                                                          lng),
                                               contentOffset);
        baiduMap.showInfoWindow(infoWindow);
    }

}
