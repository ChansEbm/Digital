package com.szbb.pro.ui.Activity.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.databinding.AtyMainBinding;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OnPopUpSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.Fragment.Main.FittingsFragment;
import com.szbb.pro.ui.Fragment.Main.NearbyFragment;
import com.szbb.pro.ui.Fragment.Main.OrderFragment;
import com.szbb.pro.ui.Fragment.Main.VIPFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

public class MainActivity extends BaseAty implements OnPopUpSelectListener {

    private int[] titles = {R.string.main_bottom_order, R.string.main_bottom_nearby, R.string
            .main_bottom_fittings, R.string.main_bottom_vip};
    private int[] bottoms = {R.id.llyt_main_bottom_order, R.id.llyt_main_bottom_nearby, R.id
            .llyt_main_bottom_fittings, R.id.llyt_main_bottom_vip};
    private int[] bottomImageNormal = {R.mipmap.ic_order_nor, R.mipmap.ic_nearby_nor, R.mipmap
            .ic_fittings_nor, R.mipmap.ic_vip_nor};
    private int[] bottomImagePress = {R.mipmap.ic_order_press, R.mipmap.ic_nearby_press, R.mipmap
            .ic_fittings_press, R.mipmap.ic_vip_press};
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    private AtyMainBinding mainBinding;
    private BGABadgeImageView ivMainBottomOrder;
    private BGABadgeImageView ivMainBottomNearby;
    private BGABadgeImageView ivMainBottomVip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = (AtyMainBinding) viewDataBinding;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }


    @Override
    protected void initViews() {
        titleBarTools(this).setTitle(titles[0]);

        ivMainBottomOrder = mainBinding.ivMainBottomOrder;//带数字Badge
        ivMainBottomNearby = mainBinding.ivMainBottomNearby;//带点Badge
        ivMainBottomVip = mainBinding.ivMainBottomVip;//带点Badge

        if (fragments.isEmpty()) {
            fragments.add(new OrderFragment());
            fragments.add(new NearbyFragment());
            fragments.add(new FittingsFragment());
            fragments.add(new VIPFragment());
            fragment = fragments.get(0);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.flyt_fragment, fragment).commit();
            changeFragment(0);
            titleBarTools.hideTitleBar();
        }
    }

    @Override
    protected void initEvents() {
        String extra_registration_id;
        Prefser prefser = new Prefser(AppTools.getSharePreferences());
        extra_registration_id = prefser.get("registrationId", String.class, "");
        if (!extra_registration_id.isEmpty()) {
            LogTools.v(extra_registration_id);
            networkModel.setDevice(extra_registration_id, NetworkParams.CUPCAKE);
        }
    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_main;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.llyt_main_bottom_order:
                changeFragment(0);
                titleBarTools.hideTitleBar();
                titleBarTools.setTitle(titles[0]);
                break;
            case R.id.llyt_main_bottom_nearby:
                changeFragment(1);
                titleBarTools.showTitleBar();
                titleBarTools.setTitle(titles[1]);
                break;
            case R.id.llyt_main_bottom_fittings:
                changeFragment(2);
                titleBarTools.hideTitleBar();
                titleBarTools.setTitle(titles[2]);
                break;
            case R.id.llyt_main_bottom_vip:
                changeFragment(3);
                titleBarTools.showTitleBar();
                titleBarTools.setTitle(titles[3]);
                break;

        }

    }

    private void changeFragment(int index) {
        Fragment newFragment = fragments.get(index);
        resetBottomAll();
        setSingleBottom(index);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (newFragment != fragment) {
            if (!newFragment.isAdded()) {
                fragmentTransaction.hide(fragment).add(R.id.flyt_fragment, newFragment).commit();
            } else {
                fragmentTransaction.hide(fragment).show(newFragment).commit();
            }
            fragment = newFragment;
        }
    }

    private void setSingleBottom(int index) {
        LinearLayout linearLayout = (LinearLayout) findViewById(bottoms[index]);
        TextView textView = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(getResources().getColor(R.color.theme_primary));
        BGABadgeImageView bgaBadgeImageView = (BGABadgeImageView) linearLayout.getChildAt(0);
        bgaBadgeImageView.setImageResource(bottomImagePress[index]);
    }

    private void resetBottomAll() {

        for (int i = 0; i < bottoms.length; i++) {
            int id = bottoms[i];
            LinearLayout linearLayout = (LinearLayout) findViewById(id);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            textView.setTextColor(getResources().getColor(R.color.color_text_dark));
            BGABadgeImageView bgaBadgeImageView = (BGABadgeImageView) linearLayout.getChildAt(0);
            bgaBadgeImageView.setImageResource(bottomImageNormal[i]);
        }
    }

    @Override
    public void onPopUpItemClick(int parentIndex, int childIndex) {

    }
}
