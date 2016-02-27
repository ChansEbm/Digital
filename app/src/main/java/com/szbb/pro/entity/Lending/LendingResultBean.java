package com.szbb.pro.entity.Lending;

import android.databinding.Bindable;

import com.szbb.pro.BR;
import com.szbb.pro.entity.TextBean;

/**
 * Created by ChanZeeBm on 2015/11/10.
 */
public class LendingResultBean extends TextBean {
    private String price;
    private String description;
    private boolean expand;

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
        notifyPropertyChanged(BR.expand);
    }

}
