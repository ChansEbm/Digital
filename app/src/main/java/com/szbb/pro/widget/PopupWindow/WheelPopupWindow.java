package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.aigestudio.wheelpicker.widget.curved.WheelDatePicker;
import com.aigestudio.wheelpicker.widget.curved.WheelTimePicker;
import com.szbb.pro.R;
import com.szbb.pro.WheelPickerLayout;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.OnWheelMultiOptsCallback;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.LogTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/7.
 */
public class WheelPopupWindow extends BasePopupWindow {
    private WheelDatePicker wheelDatePicker;
    private WheelTimePicker wheelTimePicker;
    private WheelCurvedPicker wheelCurvedPicker;
    private WheelPickerLayout wheelPickerLayout;

    private TextView positive;

    private String popupTitle = null;
    private int params = -1;

    private WheelOptions options;

    private OnWheelOptsSelectCallback onWheelOptsSelectCallback;
    private OnWheelMultiOptsCallback onWheelMultiOptsCallback;
    private String curved;
    private String date;
    private String time;
    private int index = -1;

    private List<String> list = new ArrayList<>();



    public WheelPopupWindow(Context context) {
        super(context);
        wheelPickerLayout = (WheelPickerLayout) viewDataBinding;
        wheelDatePicker = wheelPickerLayout.datePicker;
        wheelTimePicker = wheelPickerLayout.timePicker;
        wheelCurvedPicker = wheelPickerLayout.curvedPicker;
        positive = wheelPickerLayout.positive;
        initEvents();
    }

    public void setOptions(@NonNull WheelOptions options) {
        this.options = options;
        changeLayout(options);
    }

    private void initEvents() {
        positive.setOnClickListener(this);
        wheelPickerLayout.negative.setOnClickListener(this);
        wheelDatePicker.setWheelDecor(true, new ColorDecor());
        wheelCurvedPicker.setWheelDecor(true, new ColorDecor());
        wheelTimePicker.setWheelDecor(true, new ColorDecor());
        wheelCurvedPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {

            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                WheelPopupWindow.this.index = index;
                curved = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    positive.setEnabled(false);
                } else {
                    positive.setEnabled(true);
                }
            }
        });

        wheelDatePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                date = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    positive.setEnabled(false);
                } else {
                    positive.setEnabled(true);
                }
            }
        });

        wheelTimePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                time = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state != AbstractWheelPicker.SCROLL_STATE_IDLE) {
                    positive.setEnabled(false);
                } else {
                    positive.setEnabled(true);
                }
            }
        });
    }

    private void changeLayout(WheelOptions wheelOptions) {
        switch (wheelOptions) {
            case STANDER:
                wheelDatePicker.setVisibility(View.GONE);
                wheelTimePicker.setVisibility(View.GONE);
                wheelCurvedPicker.setVisibility(View.VISIBLE);
                break;
            case DATE:
                wheelTimePicker.setVisibility(View.GONE);
                wheelCurvedPicker.setVisibility(View.GONE);
                wheelDatePicker.setVisibility(View.VISIBLE);
                break;
            case DATE_WITH_TIME:
                wheelCurvedPicker.setVisibility(View.GONE);
                wheelDatePicker.setVisibility(View.VISIBLE);
                wheelTimePicker.setVisibility(View.VISIBLE);
                break;
            case TIME:
                wheelDatePicker.setVisibility(View.GONE);
                wheelCurvedPicker.setVisibility(View.GONE);
                wheelTimePicker.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setParams(int params) {
        this.params = params;
    }

    public void setOnWheelOptsSelectCallback(OnWheelOptsSelectCallback onWheelOptsSelectCallback) {
        this.onWheelOptsSelectCallback = onWheelOptsSelectCallback;
    }

    public void setCurvedData(String[] data) {
        this.list.clear();
        for (String str : data) {
            list.add(str);
        }
        wheelCurvedPicker.setData(list);
    }

    public void setPopupTitle(String popupTitle) {
        wheelPickerLayout.title.setText(popupTitle);
        this.popupTitle = popupTitle;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (options == null) {
            LogTools.e("options null");
            return;
        }
        if (options == WheelOptions.STANDER) {
            if (list.isEmpty()) {
                LogTools.e("no data in set");
                return;
            }
            if (popupTitle.isEmpty()) {
                LogTools.e("no title was set");
                return;
            }
        }
        if (onWheelMultiOptsCallback != null) {
            if (params == -1) {
                LogTools.e("params not set");
                return;
            }
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_wheel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive:
                if (onWheelOptsSelectCallback != null)
                    singleOpts();
                if (onWheelMultiOptsCallback != null) {
                    multiOpts();
                }
                break;
        }
        dismiss();
    }

    private void singleOpts() {
        switch (options) {
            case STANDER:
                onWheelOptsSelectCallback.onWheelSelect(curved, options, index);
                break;
            case TIME:
                onWheelOptsSelectCallback.onWheelSelect(time, options, index);
                break;
            case DATE:
                onWheelOptsSelectCallback.onWheelSelect(date, options, index);
                break;
            case DATE_WITH_TIME:
                onWheelOptsSelectCallback.onWheelSelect(date + " " + time, options,
                        index);
                break;
        }
    }

    private void multiOpts() {
        switch (options) {
            case STANDER:
                onWheelMultiOptsCallback.onWheelSelect(curved, options, params, index);
                break;
            case TIME:
                onWheelMultiOptsCallback.onWheelSelect(time, options, params, index);
                break;
            case DATE:
                onWheelMultiOptsCallback.onWheelSelect(date, options, params, index);
                break;
            case DATE_WITH_TIME:
                onWheelMultiOptsCallback.onWheelSelect(date + " " + time, options, params,
                        index);
                break;
        }
    }

    public void setOnWheelMultiOptsCallback(OnWheelMultiOptsCallback onWheelMultiOptsCallback) {
        this.onWheelMultiOptsCallback = onWheelMultiOptsCallback;
    }


    class ColorDecor extends AbstractWheelDecor {

        @Override
        public void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint) {
            canvas.drawColor(context.getResources().getColor(R.color.color_20_primary));
        }
    }
}
