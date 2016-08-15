package com.szbb.pro.ui.activity.grab;

import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.GrabDetailLayout;
import com.szbb.pro.ItemGrabDetailLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.grab.GrabBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.factory.MapFactory;
import com.szbb.pro.factory.TencentLocationListener;
import com.szbb.pro.factory.TencentMapper;
import com.szbb.pro.model.MapModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.ScreenTools;
import com.tencent.map.geolocation.TencentLocation;

import org.solovyev.android.views.llm.LinearLayoutManager;

public class GrabDetailActivity
        extends BaseAty<BaseBean, GrabBean.ListBean.ProductListBean> {
    private GrabDetailLayout grabDetailLayout;
    private TencentMapper tencentMapper = new MapFactory().createTencentMapper();
    private TencentLocation tencentLocation;
    private TencentListener locationListener = new TencentListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grabDetailLayout = (GrabDetailLayout) viewDataBinding;
        grabDetailLayout.setGrab((GrabBean.ListBean) getIntent().getParcelableExtra("grab"));
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_competition_order_detail);
        initRecyclerView();
//        grabDetailLayout.llytTop.getViewTreeObserver()
//                                .addOnGlobalLayoutListener(new TopGlobal());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = grabDetailLayout.recyclerView;
        recyclerView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new TopGlobal());
        this.list.addAll(grabDetailLayout.getGrab()
                                         .getProduct_list());
        commonBinderAdapter = new CommonBinderAdapter<GrabBean.ListBean.ProductListBean>(this,
                                                                                         R.layout.item_grab_detail,
                                                                                         list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, GrabBean.ListBean.ProductListBean productListBean) {
                ((ItemGrabDetailLayout) viewDataBinding).setItem(productListBean);
            }
        };
        recyclerView.setAdapter(commonBinderAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvents() {
        initMapInfo();
    }

    private void initMapInfo() {
        GrabBean.ListBean grab = grabDetailLayout.getGrab();
        double lat = Double.parseDouble(grab.getLat());
        double lng = Double.parseDouble(grab.getLng());
        new MapModel(this).addOverlay(grabDetailLayout.mapView.getMap(),
                                      lat,
                                      lng,
                                      false,
                                      R.mipmap.ic_fixable_user);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                                     R.mipmap.ic_fixable_user);
        new MapModel(this).addInfoWindow(grabDetailLayout.mapView.getMap(),
                                         getInfoView(),
                                         lat,
                                         lng,
                                         -bitmap.getHeight());
        new MapModel(this).moveToSpecifyLocation(grabDetailLayout.mapView.getMap(),
                                                 new LatLng
                                                         (lat,
                                                          lng));

        tencentMapper.locate(this, locationListener);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_grab_detail;
    }

    @Override
    protected void onClick(int id, View view) {
        if (id == R.id.button) {
            grab();
        }
    }


    private void grab() {
        final MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle("抢单")
              .setMessage("抢单成功后工单会归类至我的工单-新工单")
              .setNegativeButton(getString(R.string.negative),
                                 new View.OnClickListener() {
                                     @Override
                                     public void
                                     onClick(View v) {
                                         dialog.dismiss();
                                     }
                                 })
              .setPositiveButton(getString(R.string.positive),
                                 new View.OnClickListener() {
                                     @Override
                                     public void
                                     onClick(View v) {
                                         String orderid =
                                                 grabDetailLayout.getGrab()
                                                                 .getOrderid();
                                         assert tencentLocation != null;
                                         networkModel
                                                 .snapupOrder(orderid,
                                                              String.valueOf(tencentLocation
                                                                                     .getLatitude
                                                                                             ()),
                                                              String.valueOf(tencentLocation
                                                                                     .getLongitude()),
                                                              NetworkParams.CUPCAKE);
                                         dialog.dismiss();
                                     }
                                 });
        dialog.show();

    }

    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        super.onJsonObjectSuccess(baseBean,
                                  paramsCode);
        if (paramsCode == NetworkParams.CUPCAKE) {//抢单
            grabSuccess();
        }
    }

    private void grabSuccess() {
        final MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle("抢单成功")
              .setMessage("请马上去服务")
              .setNegativeButton("继续抢单",
                                 new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Bundle bundle = new Bundle();
                                         bundle.putInt("flag",
                                                       AppKeyMap.CUPCAKE);
                                         AppTools.sendBroadcast(bundle,
                                                                AppKeyMap.GRAB_ACTION);
                                         dialog.dismiss();
                                         AppTools.removeSingleActivity(GrabDetailActivity.this);

                                     }
                                 })
              .setPositiveButton
                      ("去服务",
                       new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Bundle bundle = new Bundle();
                               bundle.putInt("flag",
                                             AppKeyMap.DONUT);
                               AppTools.sendBroadcast(bundle,
                                                      AppKeyMap.GRAB_ACTION);
                               dialog.dismiss();
                               AppTools.removeSingleActivity(GrabDetailActivity.this);
                           }
                       })
              .show();


    }

    private View getInfoView() {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.bg_info_popup);
        String address = grabDetailLayout.getGrab()
                                         .getAddress();
        String nickname = grabDetailLayout.getGrab()
                                          .getNickname();
        textView.setText(nickname + "\n" + address);
        return textView;
    }


    @Override
    protected void onStop() {
        super.onStop();
//        tencentMapper.stopLocate(this, locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class TopGlobal
            implements ViewTreeObserver
            .OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            int height = grabDetailLayout.llytTop.getHeight();
            int screenHeight = ScreenTools.getScreenHeight(GrabDetailActivity.this);
            int maxHeight = (int) (screenHeight * 0.6);
            ViewGroup.LayoutParams layoutParams = grabDetailLayout.llytTop.getLayoutParams();
            layoutParams.height = Math.min(maxHeight,
                                           height);
            grabDetailLayout.llytTop.setLayoutParams(layoutParams);
        }
    }

    class TencentListener
            extends TencentLocationListener {

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            GrabDetailActivity.this.tencentLocation = tencentLocation;
            LogTools.i(tencentLocation.getLatitude());
            LogTools.i(tencentLocation.getLongitude());

            new MapFactory().createBaiduMapper()
                            .addMyLocation(grabDetailLayout.mapView.getMap(),
                                           tencentLocation.getLatitude(),
                                           tencentLocation.getLongitude(),
                                           0,
                                           R.mipmap.ic_owner);
            tencentMapper.stopLocate(GrabDetailActivity.this, this);
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }
}
