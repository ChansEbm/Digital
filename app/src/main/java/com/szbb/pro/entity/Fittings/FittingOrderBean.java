package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/23.
 */
public class FittingOrderBean extends BaseObservable {
    private String no;
    private String time;
    private String state;
    private String price;
    private List<Child> childList = new ArrayList<>();

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    @Bindable
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
        notifyPropertyChanged(BR.no);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price + "元";
        notifyPropertyChanged(BR.price);

    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);

    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    public class Child extends BaseObservable {
        private String url;
        private String fittingName;
        private String spec;
        private String num;

        @Bindable
        public String getFittingName() {
            return fittingName;
        }

        public void setFittingName(String fittingName) {
            this.fittingName = fittingName;
            notifyPropertyChanged(BR.fittingName);
        }

        @Bindable
        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
            notifyPropertyChanged(BR.spec);

        }

        @Bindable
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
            notifyPropertyChanged(BR.url);

        }

        @Bindable
        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
            notifyPropertyChanged(BR.num);
        }
    }

}
