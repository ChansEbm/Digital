package com.szbb.pro.ui.Activity.Vip.PersonalInfo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.szbb.pro.PreviewLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.widget.SimplePhotoView;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends BaseAty {
    private PreviewLayout previewLayout;
    private ViewPager viewPager;
    private List<String> source = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private int specialPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        previewLayout = (PreviewLayout) viewDataBinding;
        final ArrayList<String> source = getIntent().getStringArrayListExtra("source");
        this.source.addAll(source);
        specialPosition = getIntent().getIntExtra("specialPosition", 0);
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitleBackgroundColor(R.color.color_black);
        viewPager = previewLayout.viewPager;

        for (String s : source) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_preview, null, false);
            SimplePhotoView simplePhotoView = (SimplePhotoView) view.findViewById(R.id
                    .simpleDraweeView);
            simplePhotoView.setImageURI(Uri.parse(s));
            views.add(view);
        }
    }

    @Override
    protected void initEvents() {
        viewPager.setAdapter(new PagerAdapter());
        viewPager.setCurrentItem(specialPosition);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_preview;
    }

    @Override
    protected void onClick(int id, View view) {

    }

    class PagerAdapter extends android.support.v4.view.PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }
}
