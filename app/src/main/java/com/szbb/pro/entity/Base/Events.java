package com.szbb.pro.entity.Base;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by ChanZeeBm on 2016/2/24.
 */
public class Events extends BaseObservable {
    private View.OnClickListener onClickListener;

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
