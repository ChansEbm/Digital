package com.szbb.pro.biz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * map model (depend on Baidu map)
 * Created by ChanZeeBm on 2015/12/30.
 */
public class MapBiz {
    private Context context;

    public MapBiz (Context context) {
        this.context = context;
    }

    /**
     * add overlay in baidu map
     *
     * @param baiduMap  which map need be added overlay
     * @param lat       lat
     * @param lng       lng
     * @param draggable could this map be dragged or not
     * @return a marker object
     */
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

    public void moveToSpecifyLocation(@NonNull BaiduMap baiduMap, @NonNull LatLng latLng) {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
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

    public void addInfoWindow(BaiduMap baiduMap, View showContent, double lat, double lng, int
            contentOffset) {
        InfoWindow infoWindow = new InfoWindow(showContent,
                                               new LatLng(lat,
                                                          lng),
                                               contentOffset);
        baiduMap.showInfoWindow(infoWindow);
    }


}
