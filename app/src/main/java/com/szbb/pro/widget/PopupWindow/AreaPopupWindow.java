package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.databinding.PopAreaLayout;
import com.szbb.pro.impl.AreaCallBack;
import com.szbb.pro.widget.AreaPicker;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public class AreaPopupWindow extends BasePopupWindow {
    private AreaPicker areaPicker;

    public AreaPopupWindow(Context context) {
        super(context);
        PopAreaLayout layout = (PopAreaLayout) viewDataBinding;
        areaPicker = layout.areaPicker;

    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_area;
    }

    @Override
    public void onClick(View v) {

    }

    public void setAreaCallBack(AreaCallBack areaCallBack) {
        areaPicker.setAreaCallBack(areaCallBack);
    }

    public void notifyData() {
        areaPicker.notifyData();
    }
}
