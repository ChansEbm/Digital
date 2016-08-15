package com.szbb.pro.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.TestLayout;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.AreaCallBack;
import com.szbb.pro.impl.UpdateUIListener;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.widget.deleter.DeleterConfigs;
import com.szbb.pro.widget.deleter.DeleterConfigsBuilder;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;
import com.szbb.pro.widget.deleter.DeleterImageView;
import com.szbb.pro.widget.deleter.DeleterScrollLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by ChanZeeBm on 2015/9/19.
 */
public class TestAty
        extends BaseAty<MyOrderBean, MyOrderBean.ListEntity>
        implements
        UpdateUIListener, AreaCallBack {
    TestLayout testLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLayout = (TestLayout) viewDataBinding;
        DeleterScrollLayout deleterScrollLayout = testLayout.deleterScrollLayout;
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        urls.add("http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110638_1.jpg");
        deleterScrollLayout.setConfigs(deleterScrollLayout.getConfigsBuilder()
                                                          .setNetworkUrls(urls)
                                                          .setMode(DeleterConfigs.MODE_VIEW)
                                                          .createDeleterConfigs());
        deleterScrollLayout.setPlacerImages(R.mipmap.ic_citizen_id,
                                            R.mipmap.ic_citizen_back,
                                            R.mipmap.ic_citizen_font);
        deleterScrollLayout.setDeleterHandlerCallback(new DeleterHandlerCallback() {
            @Override
            public void success(Set<Integer> keySet,
                                List<String> photoPaths) {
                for (String photoPath : photoPaths) {
                    LogTools.w(photoPath);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        managerCompat1.notify(1, builder.build());
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.test;
    }

    @Override
    protected void onClick(int id,
                           View view) {
//        if (id == R.id.button) {
//            startActivity(new Intent().setClass(this, SecondActivity.class).setFlags(Intent
//                    .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
    }

    @Override
    public void uiUpData(Intent intent) {

    }

    @Override
    public void onSelect(String provinceName,
                         String cityName,
                         int provinceIndex,
                         int cityIndex) {

    }

    @Override
    public List<String> getProvince() {
        return null;
    }

    @Override
    public List<List<String>> getCity() {
        return null;
    }

    @Override
    public void onJsonObjectSuccess(MyOrderBean myOrderBean,
                                    NetworkParams paramsCode) {

    }
}
