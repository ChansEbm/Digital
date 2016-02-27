package com.szbb.pro.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Spanned;

import com.szbb.pro.BR;
import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/10/21.
 */
public class TimeBean extends BaseObservable{
    private long time;
    private Spanned titleSpanned;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Bindable
    public Spanned getTitleSpanned() {
        return titleSpanned;
    }

    public void setTitleSpanned(String titleSpanned) {
        this.titleSpanned = AppTools.fromHtml(titleSpanned);
        notifyPropertyChanged(BR.titleSpanned);
    }


}
