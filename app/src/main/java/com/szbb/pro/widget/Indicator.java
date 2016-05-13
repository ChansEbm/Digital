package com.szbb.pro.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.szbb.pro.R;


/**
 * Created by ChanZeeBm on 2016/4/15.
 */
public class Indicator extends View implements ViewPager.OnPageChangeListener {
    private Paint paint = new Paint();
    private float radius = 6;
    private int indicatorMargin = 4;
    private int colorNor = Color.parseColor("#626262");
    private int colorSelect = Color.parseColor("#bfbfbf");
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setDither(true);
        paint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.Indicator_indicator_radius:
                    radius = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Indicator_colorNor:
                    colorNor = a.getColor(index, Color.parseColor("#626262"));
                    break;
                case R.styleable.Indicator_colorSelect:
                    colorSelect = a.getColor(index, Color.parseColor("#bfbfbf"));
                    break;
                case R.styleable.Indicator_indicatorMargin:
                    indicatorMargin = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDraw(Canvas canvas) {
        if (viewPager != null && pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            int currentItem = viewPager.getCurrentItem();
            for (int i = 0; i < count; i++) {
                if (i == currentItem) {
                    paint.setColor(colorSelect);
                } else {
                    paint.setColor(colorNor);
                }
                float start = ((getWidth() - getPaddingStart() - getPaddingEnd()) / 2) - ((count *
                        (2 * radius)) + ((count - 1) * indicatorMargin)) / 2;
                float cx = start + (i * (indicatorMargin + 2 * radius));
                float cy = getHeight() / 2;
                canvas.drawCircle(cx, cy, radius, paint);
            }
        }
    }

    public void setWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        this.pagerAdapter = viewPager.getAdapter();
        postInvalidate();
    }

    public void setRadius(@DimenRes float radius) {
        this.radius = radius;
        postInvalidate();
    }

    public void setIndicatorMargin(@DimenRes int indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
        postInvalidate();
    }

    public void setColorNor(@ColorRes int colorNor) {
        this.colorNor = colorNor;
        postInvalidate();
    }

    public void setColorSelect(@ColorRes int colorSelect) {
        this.colorSelect = colorSelect;
        postInvalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
