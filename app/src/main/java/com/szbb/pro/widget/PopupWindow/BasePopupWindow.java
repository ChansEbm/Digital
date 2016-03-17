package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.szbb.pro.R;
import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/11/18.
 */
public abstract class BasePopupWindow extends PopupWindow implements PopupWindow
        .OnDismissListener, View.OnClickListener {
    protected AppCompatActivity appCompatActivity;//附上的窗体
    protected ViewDataBinding viewDataBinding;
    protected Context context;
    protected View rootView;

    public BasePopupWindow(Context context) {
        this.context = context;
        this.appCompatActivity = (AppCompatActivity) context;
        this.viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                getPopupLayout(), null, false);
        rootView = viewDataBinding.getRoot();
        setOnDismissListener(this);//设置窗体消失监听器
        setFocusable(true);//设置点击窗体外允许消失
        setBackgroundDrawable(new BitmapDrawable());//设置背景颜色为无,目的是...
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置宽度
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置高度
        setContentView(viewDataBinding.getRoot());//设置显示的布局
        setAnimationStyle(R.style.PopupAnim);//设置进出动画
    }


    /**
     * 当窗体消失的时候,把背景颜色恢复
     */
    @Override
    public void onDismiss() {
        AppTools.setWindowBackground(appCompatActivity, 1f);
    }

    /**
     * 当窗体出现的时候,把背景设为60%透明度
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        AppTools.setWindowBackground(appCompatActivity, 0.6f);
        super.showAtLocation(parent, gravity, x, y);
    }

    public void showAtDefaultLocation() {
        AppTools.setWindowBackground(appCompatActivity, 0.6f);
        super.showAtLocation(appCompatActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获取子类布局Id
     *
     * @return 子类布局Id
     */
    public abstract int getPopupLayout();

    @Override
    public void onClick(View v) {
        AppTools.hideSoftInputMethod(v);
    }
}
