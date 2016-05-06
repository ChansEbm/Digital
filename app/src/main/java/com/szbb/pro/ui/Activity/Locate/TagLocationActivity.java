package com.szbb.pro.ui.activity.locate;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.szbb.pro.R;
import com.szbb.pro.TagLocationLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.model.MapModel;
import com.szbb.pro.tools.AppTools;

public class TagLocationActivity extends BaseAty {

    private TagLocationLayout layout;
    private boolean isShowing = false;

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
        MapModel model = new MapModel(this);
        double lat = getIntent().getDoubleExtra("lat", 0.0d);
        double lng = getIntent().getDoubleExtra("lng", 0.0d);
        String address = getIntent().getStringExtra("address");
        if (lat == 0.0d || lng == 0.0d) {
            AppTools.removeSingleActivity(this);
        }
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_blue);
        final LatLng latLng = new LatLng(lat, lng);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(bd);
        baiduMap.addOverlay(options);
        final InfoWindow infoWindow = new InfoWindow(getInfoView(address), latLng, -getResources().getDrawable(R.mipmap.ic_location_blue).getMinimumHeight());
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!isShowing) {
                    baiduMap.showInfoWindow(infoWindow);
                } else {
                    baiduMap.hideInfoWindow();
                }
                isShowing = !isShowing;
                return false;
            }
        });
        model.moveToSpecifyLocation(baiduMap, latLng);
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

}
