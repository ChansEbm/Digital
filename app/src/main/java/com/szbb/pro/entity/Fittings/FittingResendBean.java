package com.szbb.pro.entity.Fittings;

import android.databinding.Bindable;

import com.szbb.pro.BR;
import com.szbb.pro.entity.Base.BaseBean;

/**
 * Created by Administrator on 2/11/2016.
 */
public class FittingResendBean extends BaseBean {

    private String remarks = "";
    private String shippingType = "";
    private String shippingNum = "";
    private String shippingTypeCom = "";
    private String shippingPayType = "";
    private String shippingCost = "";
    private String totalCount = "";


    @Bindable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        notifyPropertyChanged(BR.remarks);
    }

    @Bindable
    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
        notifyPropertyChanged(BR.shippingType);
    }

    @Bindable
    public String getShippingNum() {
        return shippingNum;
    }

    public void setShippingNum(String shippingNum) {
        this.shippingNum = shippingNum;
        notifyPropertyChanged(BR.shippingNum);
    }

    @Bindable
    public String getShippingPayType() {
        return shippingPayType;
    }

    public void setShippingPayType(String shippingPayType) {
        this.shippingPayType = shippingPayType;
        notifyPropertyChanged(BR.shippingPayType);
    }

    @Bindable
    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
        notifyPropertyChanged(BR.shippingCost);
    }

    @Bindable
    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getShippingTypeCom() {
        return shippingTypeCom;
    }

    public void setShippingTypeCom(String shippingTypeCom) {
        this.shippingTypeCom = shippingTypeCom;
    }
}
