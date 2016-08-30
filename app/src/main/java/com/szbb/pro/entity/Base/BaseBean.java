package com.szbb.pro.entity.base;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import java.io.Serializable;

/**
 * Created by ChanZeeBm on 2015/12/25.
 */
public class BaseBean extends BaseObservable implements Serializable {

    private int errorcode = 0;
    private String msg;
    private int isNext;
    private int page;
    private String total;
    private View.OnClickListener onClickListener;
    private String tag;

    public int getErrorcode () {
        return errorcode;
    }

    public void setErrorcode (int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public int getIsNext () {
        return isNext;
    }

    public void setIsNext (int isNext) {
        this.isNext = isNext;
    }

    public int getPage () {
        return page;
    }

    public void setPage (int page) {
        this.page = page;
    }

    public String getTotal () {
        return total;
    }

    public void setTotal (String total) {
        this.total = total;
    }

    @Bindable
    public View.OnClickListener getOnClickListener () {
        return onClickListener;
    }

    public void setOnClickListener (View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Bindable
    public String getTag () {
        return tag;
    }

    public void setTag (String tag) {
        this.tag = tag;
    }
}
