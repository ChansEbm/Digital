package com.szbb.pro.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.szbb.pro.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * map model (depend on Baidu map)
 * Created by ChanZeeBm on 2015/12/30.
 */
public class MapModel {
    private Context context;

    public MapModel(Context context) {
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
            draggable) {
        LatLng latLng = new LatLng(lat, lng);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap
                .ic_location);
        OverlayOptions overlayOptions = new MarkerOptions().position(latLng).icon
                (bitmapDescriptor).draggable(draggable);
        return (Marker) baiduMap.addOverlay(overlayOptions);
    }

    public void moveToSpecifyLocation(@NonNull BaiduMap baiduMap, @NonNull LatLng latLng) {
        MyLocationData locationData = new MyLocationData.Builder().latitude(latLng.latitude)
                .longitude(latLng.longitude)
                .build();
        baiduMap.setMyLocationData(locationData);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }
}
