package com.szbb.pro.ui.Activity.Locate;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.szbb.pro.LocationLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.model.MapModel;
import com.szbb.pro.tools.AppTools;

public class LocationActivity extends BaseAty implements BDLocationListener {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationLayout locationLayout;
    private MapModel mapModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationLayout = (LocationLayout) viewDataBinding;
    }

    @Override
    protected void initViews() {
        mapModel = new MapModel(this);
        defaultTitleBar(this).setTitle(R.string.now_position);
        checkNetWorkStateAndLocate();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (baiduMap.getProjection() == null)
            return false;
        if (item.getItemId() == R.id.menu_done) {
            Point point = new Point(mapView.getWidth() / 2, mapView.getHeight() / 2);
            double lat = baiduMap.getProjection().fromScreenLocation(point).latitude;
            double lng = baiduMap.getProjection().fromScreenLocation(point).longitude;
            setResult(RESULT_OK, new Intent().putExtra("lat", lat).putExtra("lng", lng));
            AppTools.removeSingleActivity(this);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkNetWorkStateAndLocate();
    }

    private boolean checkNetWorkStateAndLocate() {
        if (!AppTools.isNetworkConnected()) {
            AppTools.showSettingSnackBar(parentView, getResources().getString(R.string
                    .no_network_is_detected));
            return false;
        }
        mapView = locationLayout.mapView;
        baiduMap = mapView.getMap();
        AppTools.locate(this);
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_location;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double lat = bdLocation.getLatitude();
        double lng = bdLocation.getLongitude();
        mapModel.moveToSpecifyLocation(mapView.getMap(), new LatLng(lat, lng));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppTools.stopLocate();
    }
}
