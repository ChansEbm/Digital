package com.szbb.pro.ui.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.MainLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.entity.vip.CheckUpdateBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OnPopUpSelectListener;
import com.szbb.pro.manager.AppUpdaterManager;
import com.szbb.pro.tools.ApkTools;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.tools.MiscUtils;
import com.szbb.pro.ui.fragment.main.FittingsFragment;
import com.szbb.pro.ui.fragment.main.GrabFragment;
import com.szbb.pro.ui.fragment.main.OrderFragment;
import com.szbb.pro.ui.fragment.main.SystemMsgFragment;
import com.szbb.pro.ui.fragment.main.VIPFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity
        extends BaseAty<BaseBean, BaseBean>
        implements OnPopUpSelectListener, RadioGroup.OnCheckedChangeListener {

    private int[] titles = {R.string.main_bottom_order, R.string
            .main_bottom_fittings, R.string.title_competition_order, R.string
            .main_bottom_system_msg, R.string.main_bottom_vip};

    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    private MainLayout layout;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (MainLayout) viewDataBinding;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        AppTools.registerBroadcast(new GrabBroadcast(),
                                   AppKeyMap.GRAB_ACTION);

    }

    @Override
    protected void initViews () {
        defaultTitleBar(this).setNavigationIcon(null);

        if (fragments.isEmpty()) {
            fragments.add(new OrderFragment());
            fragments.add(new FittingsFragment());
            fragments.add(new GrabFragment());
            fragments.add(new SystemMsgFragment());
            fragments.add(new VIPFragment());
            fragment = fragments.get(0);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.flyt_fragment,
                                    fragment)
                               .commit();
            changeFragment(0);
            titleBarTools.hideTitleBar();
        }

    }

    @Override
    protected void initEvents () {
        layout.radioGroup.setOnCheckedChangeListener(this);
        networkModel.workerInfo(NetworkParams.DONUT);
        networkModel.versions(MiscUtils.getAppVersion(this),
                              NetworkParams.GINGERBREAD);

        ApkTools.installAPK(this);
    }

    @Override
    protected void noNetworkStatus () {

    }

    @Override
    protected int getContentView () {
        return R.layout.activity_main;
    }

    @Override
    protected void onClick (int id, View view) {

    }

    private void changeFragment (int index) {
        Fragment newFragment = fragments.get(index);
        changeToolbar(index);
//        resetBottomAll();
//        setSingleBottom(index);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (newFragment != fragment) {
            if (!newFragment.isAdded()) {
                fragmentTransaction.hide(fragment)
                                   .add(R.id.flyt_fragment,
                                        newFragment)
                                   .commit();
            } else {
                fragmentTransaction.hide(fragment)
                                   .show(newFragment)
                                   .commit();
            }
            fragment = newFragment;
        }
    }

    @Override
    protected void onResumeFragments () {
        super.onResumeFragments();
    }

    private void changeToolbar (int index) {
        switch (index) {
            case 0:
            case 1:
                titleBarTools.hideTitleBar();
                break;
            default:
                titleBarTools.showTitleBar();
                titleBarTools.setTitle(titles[index]);
                break;
        }
    }


    @Override
    public void onPopUpItemClick (int parentIndex, int childIndex) {

    }


    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode,
                               event);
    }


    @Override
    public void onJsonObjectSuccess (BaseBean baseBean, NetworkParams paramsCode) {
        if (paramsCode == NetworkParams.DONUT) {
            VipInfoBean vipInfoBean = (VipInfoBean) baseBean;
            Prefser prefser = new Prefser(AppTools.getSharePreferences());
            prefser.put("VipInfo",
                        vipInfoBean);
        } else if (paramsCode == NetworkParams.GINGERBREAD) {//检查版本
            CheckUpdateBean checkUpdateBean = (CheckUpdateBean) baseBean;
            if (AppUpdaterManager.isNeedUpdate(this, checkUpdateBean)) {
                AppUpdaterManager.startDownloadApkFile(this, checkUpdateBean.getUrl());
            }
        }
    }

    @Override
    public void onError (BaseBean baseBean) {
        super.onError(baseBean);
        LogTools.i("error");
    }


    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_main_order:
                changeFragment(0);
                break;
            case R.id.rb_main_fitting:
                changeFragment(1);
                break;
            case R.id.rb_main_grab:
//                Toast.makeText(MainActivity.this,
//                               "敬请期待!",
//                               Toast.LENGTH_SHORT)
//                     .show();
//                return;
                changeFragment(2);
                break;
            case R.id.rb_main_sys_msg:
                changeFragment(3);
                break;
            case R.id.rb_main_vip:
                changeFragment(4);
                break;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {

    }

//    private void newApkCheck(final CheckUpdateBean checkUpdateBean) {
//        if (ApkTools.getUpdateApkFile(this).isEmpty()) {
//            Intent intent = new Intent(MainActivity.this, DownloadService.class);
//            intent.putExtra("uri", checkUpdateBean.getUrl());
//            startService(intent);
//        }
//    }

    class GrabBroadcast
            extends BroadcastReceiver {

        @Override
        public void onReceive (Context context, Intent intent) {
            if (intent.getAction()
                      .equals(AppKeyMap.GRAB_ACTION)) {
                if (intent.getIntExtra("flag",
                                       0) == AppKeyMap.DONUT) {
                    layout.rbMainOrder.setChecked(true);
                } else if (intent.getIntExtra("flag",
                                              0) == AppKeyMap.MARSHMALLOW) {//推送
                    layout.rbMainGrab.setChecked(true);
                }
            }
        }
    }
}
