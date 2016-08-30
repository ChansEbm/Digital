package com.szbb.pro.ui.fragment.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.GrabLayout;
import com.szbb.pro.ItemGrabLayout;
import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.entity.grab.GrabBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.factory.MapFactory;
import com.szbb.pro.factory.TencentLocationListener;
import com.szbb.pro.factory.TencentMapper;
import com.szbb.pro.impl.CallActionCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.CheckNetworkTools;
import com.szbb.pro.tools.PermissionTools;
import com.szbb.pro.ui.activity.grab.GrabAgreementActivity;
import com.szbb.pro.ui.activity.grab.GrabDetailActivity;
import com.szbb.pro.widget.PopupWindow.CallPopupWindow;
import com.tencent.map.geolocation.TencentLocation;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.finalteam.galleryfinal.permission.EasyPermissions;


public class GrabFragment
        extends BaseFgm<GrabBean, GrabBean.ListBean>
        implements RadioGroup.OnCheckedChangeListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private GrabLayout grabLayout;
    private int page = 1;
    private RecyclerView recyclerView;
    private GrabBean grabBean;
    private TencentLocation tencentLocation;
    private TencentMapper tencentMapper = new MapFactory().createTencentMapper();

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTools.registerBroadcast(new GrabBroadcast(),
                                   AppKeyMap.GRAB_ACTION);
    }

    @Override
    protected void initViews () {
        grabLayout = (GrabLayout) viewDataBinding;
        recyclerView = grabLayout.include.recyclerView;

        commonBinderAdapter = new CommonBinderAdapter<GrabBean.ListBean>(getContext(),
                                                                         R.layout.item_grab,
                                                                         list) {
            @Override
            public void onBind (ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, GrabBean.ListBean listBean) {
                ItemGrabLayout layout = (ItemGrabLayout) viewDataBinding;
                layout.setGrab(listBean);
            }
        };

        checkProtocol();
    }

    private void checkProtocol () {
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        boolean isNeedShowGrabAgreement = prefser.get("isNeedShowGrabAgreement", Boolean.class,
                                                      true);
        if (isNeedShowGrabAgreement) {
            start(GrabAgreementActivity.class);
        }
    }

    private void locate () {
        tencentMapper.locate(getContext(), new TencentListener());
    }

    private void cancelRefreshBlink () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
            }
        } else {
            recyclerView.getItemAnimator()
                        .setAddDuration(0);
        }
    }

    @Override
    protected void initEvents () {
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                                               .sizeResId(R.dimen.small_margin_5dp)
                                               .colorResId(R.color.color_transparent)
                                               .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commonBinderAdapter);

        commonBinderAdapter.setBinderOnItemClickListener(this);
        grabLayout.radioGroup.setOnCheckedChangeListener(this);
        AppTools.defaultRefresh(grabLayout.include.refreshLayout,
                                this);

        if (PermissionTools.alreadyHasPermission(getActivity(),
                                                 AppKeyMap.CUPCAKE,
                                                 Manifest.permission.ACCESS_COARSE_LOCATION)) {
            locate();
        }
        grabLayout.btnSigned.setOnClickListener(this);
        cancelRefreshBlink();

    }

    private void requestNetworkData (String radius) {
        if (tencentLocation == null) {
            return;
        }
        networkModel.index(String.valueOf(tencentLocation.getLatitude()),
                           String.valueOf(tencentLocation.getLongitude()),
                           radius,
                           page + "",
                           NetworkParams.CUPCAKE);
    }

    @Override
    protected void noNetworkStatus () {
        endRefreshLoading();
    }

    private void endRefreshLoading () {
        grabLayout.include.refreshLayout.endLoadingMore();
        grabLayout.include.refreshLayout.endRefreshing();
    }

    @Override
    protected void onClick (int id, View view) {
        switch (id) {
            case R.id.btn_signed:
                CallPopupWindow window = new CallPopupWindow(getContext());
                window.setCallNumbers(new String[]{"020-81316728", "020-81316730"})
                      .setCallActionCallback(
                              new CallActionCallback() {
                                  @Override public void onCall (String callNumber) {
                                      Intent intent = new Intent(Intent.ACTION_CALL,
                                                                 Uri.parse("tel:" + callNumber));
                                      if (EasyPermissions.hasPermissions(getContext(),
                                                                         Manifest.permission
                                                                                 .CALL_PHONE)) {
                                          startActivity(intent);
                                      } else {
                                          permissions.put("phone", callNumber);
                                          requestPermissions(
                                                  new String[]{Manifest.permission.CALL_PHONE},
                                                  AppKeyMap.CUPCAKE);
                                      }
                                  }
                              });
                window.showAtDefaultLocation();
                break;
        }
    }

    @Override
    protected int getContentView () {
        return R.layout.fragment_grab;
    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        page = 1;
    }

    private String getRadius () {
        int checkedRadioButtonId = grabLayout.radioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_within_5km:
                return "1";
            case R.id.rb_within_10km:
                return "2";
            case R.id.rb_within_30km:
                return "3";
            case R.id.rb_above_30km:
                return "4";
            default:
                return "";
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing (BGARefreshLayout bgaRefreshLayout) {
        page = 1;
//        requestNetworkData(getRadius());
        if (!CheckNetworkTools.isNetworkAvailable(getContext())) {
            noNetworkStatus();
            return;
        }
        locate();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore (BGARefreshLayout bgaRefreshLayout) {
        if (hasNext()) {
            page++;
            networkModel.index(String.valueOf(tencentLocation.getLatitude()),
                               String.valueOf(tencentLocation.getLongitude()),
                               getRadius(),
                               page + "",
                               NetworkParams.DONUT);
        }
        return hasNext();
    }

    private boolean hasNext () {
        if (grabBean == null) {
            return false;
        }
        int isNext = grabBean.getIsNext();
        if (isNext == 0) {
            Toast.makeText(getContext(),
                           "没有更多了!",
                           Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        return true;
    }

    @Override
    public void onJsonObjectSuccess (GrabBean t, NetworkParams paramsCode) {
        endRefreshLoading();
        grabLayout.setGrab(t);
        this.grabBean = t;
        if (paramsCode == NetworkParams.CUPCAKE) {//获取数据|刷新
            notifyAdapter(t);
            showEmptyView(t);
        } else if (paramsCode == NetworkParams.DONUT) {
            this.list.addAll(t.getList());
            commonBinderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError () {
        super.onError();
        endRefreshLoading();
    }

    @Override
    public void onBinderItemClick (View view, int pos) {
        Log.i("digital",
              "onBinderItemClick: " + pos);
        startGrabDetailActivity(pos);
    }

    private void startGrabDetailActivity (final int pos) {
        GrabBean.ListBean listBean = list.get(pos);
        startActivity(new Intent(getContext(),
                                 GrabDetailActivity.class).putExtra("grab",
                                                                    listBean));
    }


    private void showEmptyView (GrabBean t) {
        List<GrabBean.ListBean> list = t.getList();
        if (list.isEmpty() && this.list.isEmpty()) {
            grabLayout.llytEmptyData.setVisibility(View.VISIBLE);
        } else {
            grabLayout.llytEmptyData.setVisibility(View.GONE);
        }
    }

    private void notifyAdapter (GrabBean t) {
        this.list.clear();
        this.list.addAll(t.getList());
        commonBinderAdapter.notifyDataSetChanged();
    }

    class GrabBroadcast
            extends BroadcastReceiver {

        @Override
        public void onReceive (Context context, Intent intent) {
            if (intent.getAction()
                      .equals(AppKeyMap.GRAB_ACTION)) {
                requestNetworkData(getRadius());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equals(Manifest.permission.CALL_PHONE) &&
            grantResults[0] == PackageInfo.REQUESTED_PERMISSION_GRANTED) {
            String phone = (String) this.permissions.get("phone");
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    class TencentListener
            extends TencentLocationListener {

        @Override
        public void onLocationChanged (TencentLocation tencentLocation, int i, String s) {
            GrabFragment.this.tencentLocation = tencentLocation;
            requestNetworkData(getRadius());
            tencentMapper.stopLocate(getContext(), this);
        }

        @Override
        public void onStatusUpdate (String s, int i, String s1) {

        }
    }
}
