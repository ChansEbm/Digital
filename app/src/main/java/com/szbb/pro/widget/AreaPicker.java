package com.szbb.pro.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCrossPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.szbb.pro.R;
import com.szbb.pro.impl.AreaCallBack;

import java.util.ArrayList;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public class AreaPicker extends LinearLayout {
    private WheelCurvedPicker provincePicker;
    private WheelCurvedPicker cityPicker;
    private int labelTextSize;

    private String provinceName = "";
    private String cityName = "";
    private int provinceIndex = 0;
    private int cityIndex = 0;

    private AreaCallBack areaCallBack;

    public AreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AreaPicker(Context context) {
        super(context);
        init();
    }

    private void addLabel(WheelCrossPicker picker, final String label) {
        picker.setWheelDecor(true, new AbstractWheelDecor() {
            @Override
            public void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint) {

                paint.setColor(getResources().getColor(R.color.theme_primary));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(labelTextSize * 1.5F);
                canvas.drawText(label, rectNext.centerX(),
                        rectNext.centerY() - (paint.ascent() + paint.descent()) / 2.0F, paint);
            }
        });
    }

    public void init() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 2;
        labelTextSize = padding;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        provincePicker = new WheelCurvedPicker(getContext());
        cityPicker = new WheelCurvedPicker(getContext());

        provincePicker.setPadding(0, padding, padding2x, padding);
        cityPicker.setPadding(0, padding, padding2x, padding);

        addLabel(provincePicker, "省");
        addLabel(cityPicker, "市");

        addView(provincePicker, params);
        addView(cityPicker, params);

        initListener(provincePicker, 0);
        initListener(cityPicker, 1);


    }

    private void initListener(WheelCrossPicker picker, final int type) {
        picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (type == 0) {
                    provinceIndex = index;
                    provinceName = data;
                    checkState(index);
                }
                if (type == 1) {
                    cityIndex = index;
                    cityName = data;
                }
                if (areaCallBack != null)
                    areaCallBack.onSelect(provinceName, cityName, provinceIndex, cityIndex);
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
    }

    private void checkState(int index) {
        if (areaCallBack != null && areaCallBack.getCity() != null && !areaCallBack.getCity()
                .isEmpty()) {
            cityPicker.setData(areaCallBack.getCity().get(index));
            cityPicker.setItemIndex(0);
            cityPicker.invalidate();
        }
        provincePicker.invalidate();
    }

    public void setAreaCallBack(AreaCallBack areaCallBack) {
        this.areaCallBack = areaCallBack;
    }

    public void notifyData() {
        if (areaCallBack != null) {
            provincePicker.setData(areaCallBack.getProvince());
            cityPicker.setData(areaCallBack.getCity().get(0));
        }
    }
}
