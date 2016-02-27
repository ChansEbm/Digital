package com.szbb.pro.entity.Fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by ChanZeeBm on 2015/11/24.
 */
public class FittingItemListBean extends BaseObservable {
    private String url;//图片URL
    private String fittingName;//产品标题
    private String spec;//产品规格
    private String totalCount;//数量

    @Bindable
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Bindable
    public String getFittingName() {
        return fittingName;
    }

    public void setFittingName(String fittingName) {
        this.fittingName = fittingName;
    }

    @Bindable
    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
