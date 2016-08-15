package com.szbb.pro.entity.order;

import android.databinding.Bindable;

import com.szbb.pro.BR;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.DataFormatter;
import com.tencent.TIMElem;
import com.tencent.TIMMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ChanZeeBm on 2016/3/17.
 */
public class OrderMsgListBean
        extends BaseBean {
    private boolean isSelf;
    private TIMElem timElem;
    private TIMMessage timMessage;
    private String conversionTime = "";

    public boolean isSelf () {
        return isSelf;
    }

    public void setSelf (boolean self) {
        isSelf = self;
    }

    public TIMElem getTimElem () {
        return timElem;
    }

    public void setTimElem (TIMElem timElem) {
        this.timElem = timElem;
    }

    public TIMMessage getTimMessage () {
        return timMessage;
    }

    public void setTimMessage (TIMMessage timMessage) {
        this.timMessage = timMessage;
    }

    public void setConversionTime (String conversionTime) {
        this.conversionTime = conversionTime;
        notifyPropertyChanged(BR.conversionTime);
    }

    @Bindable
    public String getConversionTime () {
        if (conversionTime.isEmpty()) {
            long timestamp = timMessage.timestamp();
            conversionTime = DataFormatter.formatTimeDefaultRegex(new Date(timestamp * 1000),
                                                                  "");
        }
        return conversionTime;
    }
}
