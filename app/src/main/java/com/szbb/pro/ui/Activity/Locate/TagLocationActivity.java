package com.szbb.pro.ui.activity.locate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.szbb.pro.R;
import com.szbb.pro.TagLocationLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.biz.MapBiz;
import com.szbb.pro.tools.AppTools;

public class TagLocationActivity
        extends BaseAty
        implements BDLocationListener {

    private TagLocationLayout layout;
//    private boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (TagLocationLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle("定位");
        MapView mapView = layout.mapView;
        final BaiduMap baiduMap = mapView.getMap();
        MapBiz model = new MapBiz(this);
        double lat = getIntent().getDoubleExtra("lat",
                                                0.0d);
        double lng = getIntent().getDoubleExtra("lng",
                                                0.0d);
        String address = getIntent().getStringExtra("address");
        if (lat == 0.0d || lng == 0.0d) {
            AppTools.removeSingleActivity(this);
        }
        final LatLng latLng = new LatLng(lat,
                                         lng);
        model.addOverlay(baiduMap,
                         lat,
                         lng,
                         false,
                         R.mipmap.ic_location_blue);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                                     R.mipmap.ic_location_blue);
        int offsetHeight = bitmap.getHeight();
        model.addInfoWindow(baiduMap,
                            getInfoView(address),
                            lat,
                            lng,
                            -offsetHeight);
//        final InfoWindow infoWindow = new InfoWindow(getInfoView(address),
//                                                     latLng,
//                                                     -offsetHeight);
//        baiduMap.showInfoWindow(infoWindow);
//        isShowing = true;
//        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (!isShowing) {
//                    baiduMap.showInfoWindow(infoWindow);
//                } else {
//                    baiduMap.hideInfoWindow();
//                }
//                isShowing = !isShowing;
//                return false;
//            }
//        });
        model.moveToSpecifyLocation(baiduMap,
                                    latLng);
        AppTools.locate(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tag_location;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    private View getInfoView(String info) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.bg_info_popup);
        textView.setText(info);
        return textView;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        new MapBiz(this).addMyLocation(layout.mapView.getMap(),
                                       bdLocation.getLatitude(),
                                       bdLocation.getLongitude(),
                                       0,
                                       0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppTools.stopLocate();
    }
}
