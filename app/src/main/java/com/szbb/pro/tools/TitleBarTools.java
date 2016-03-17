package com.szbb.pro.tools;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.szbb.pro.R;

import java.lang.reflect.Field;

/**
 * Created by ChanZeeBm on 2015/9/8.
 */
public class TitleBarTools {
    private TextView title;
    private AppCompatActivity appCompatActivity;
    private Toolbar toolbar;

    //初始化
    public TitleBarTools(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        toolbar = (Toolbar) appCompatActivity.findViewById(R.id.toolBar);

        if (toolbar == null) {
            LogTools.e("toolbar not set");
        }
        toolbar.setTitle("");
        appCompatActivity.setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);

    }

    public TitleBarTools(Fragment fragment, Toolbar toolbar) {
        appCompatActivity = (AppCompatActivity) fragment.getActivity();
        this.toolbar = toolbar;
        if (toolbar == null) {
            LogTools.e("toolbar not set");
        }
        appCompatActivity.setSupportActionBar(toolbar);
        this.toolbar.setTitle("");
        title = (TextView) toolbar.findViewById(R.id.title);
    }

    //设置居中标题
    public TitleBarTools setTitle(String text) {
        title.setText(text);
        return this;
    }

    //设置标题背景
    public TitleBarTools setTitleBackgroundColor(int color) {
        toolbar.setBackgroundColor(title.getResources().getColor(color));
        return this;
    }

    //设置居中标题
    public TitleBarTools setTitle(int resId) {
        title.setText(resId);
        return this;
    }

    //设置ToolBar自带主标题
    public TitleBarTools setToolBarTitle(String toolBarTitle) {
        if (!TextUtils.isEmpty(toolBarTitle))
            toolbar.setTitle(toolBarTitle);
        else
            toolbar.setTitle("");
        toolbar.setTitleTextColor(appCompatActivity.getResources().getColor(R.color.color_white));
        return this;
    }

    //设置ToolBar自带主标题
    public TitleBarTools setToolBarTitle(int resId) {
        if (resId != 0)
            toolbar.setTitle(resId);
        else
            setToolBarTitle("");
        toolbar.setTitleTextColor(appCompatActivity.getResources().getColor(R.color.color_white));
        return this;
    }

    //设置ToolBar自带主标题字体大小
    public TitleBarTools setToolBarTitleSize(int size) {
        TextView textView = getTitleTextView();
        textView.setTextSize(size);
        return this;
    }

    //设置ToolBar自带副标题
    public TitleBarTools setToolBarSubTile(String subTile) {
        if (!TextUtils.isEmpty(subTile))
            toolbar.setSubtitle(subTile);
        else
            toolbar.setSubtitle("");
        toolbar.setSubtitleTextColor(appCompatActivity.getResources().getColor(R.color
                .color_white));
        return this;
    }

    //设置ToolBar自带副标题
    public TitleBarTools setToolBarSubTile(int resId) {
        if (resId != 0)
            toolbar.setSubtitle(resId);
        else
            setToolBarSubTile("");
        toolbar.setSubtitleTextColor(appCompatActivity.getResources().getColor(R.color
                .color_white));
        return this;
    }

    //设置ToolBar自带副标题字体大小
    public TitleBarTools setToolBarSubTitleSize(int size) {
        TextView textView = getTitleSubTextView();
        textView.setTextSize(size);
        return this;
    }

    //隐藏居中标题
    public TitleBarTools hideTitle() {
        toolbar.setVisibility(View.GONE);
        return this;
    }

    public TitleBarTools hideTitleBar() {
        if (toolbar != null)
            toolbar.setVisibility(View.GONE);
        return this;
    }

    public TitleBarTools showTitleBar() {
        if (toolbar != null)
            toolbar.setVisibility(View.VISIBLE);
        return this;
    }


    //设置Nav图标
    public TitleBarTools setNavigationIcon(int resId) {
        if (resId != 0) {
            toolbar.setNavigationIcon(resId);
        }
        return this;
    }

    //设置Nav图标
    public TitleBarTools setNavigationIcon(Drawable drawable) {
        toolbar.setNavigationIcon(drawable);
        return this;
    }

    //设置Nav监听器
    public TitleBarTools setNavigationListener(View.OnClickListener listener) {
        setSupportActionBar();
        if (toolbar.getNavigationIcon() == null) {
            LogTools.e("plz set NavigationIcon first");
        } else {
            toolbar.setNavigationOnClickListener(listener);
        }
        return this;
    }

    //设置ToolBar为ActionBar
    public TitleBarTools setSupportActionBar() {
        if (appCompatActivity != null)
            if (appCompatActivity.getSupportActionBar() == null) {
                appCompatActivity.setSupportActionBar(toolbar);
            }
        return this;
    }

    //标题默认样式
    public TitleBarTools defaultToolBar(View.OnClickListener onClickListener) {
        setNavigationIcon(R.mipmap.ic_back).setNavButtonId().setNavigationListener
                (onClickListener);
        return this;
    }

    public int getToolBarHeight() {
        return toolbar.getHeight();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    //通过反射获得Title主标题
    private TextView getTitleTextView() {
        TextView textView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            textView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return textView;
    }

    //通过反射获得Title副标题
    private TextView getTitleSubTextView() {
        TextView textView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mSubtitleTextView");
            f.setAccessible(true);
            textView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return textView;
    }

    private TitleBarTools setNavButtonId() {
        ImageButton imageButton;
        try {
            Field f = toolbar.getClass().getDeclaredField("mNavButtonView");
            f.setAccessible(true);
            imageButton = (ImageButton) f.get(toolbar);
            imageButton.setId(R.id.nav);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

}
