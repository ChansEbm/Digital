package com.szbb.pro.entity.vip;

import com.szbb.pro.entity.base.BaseBean;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CheckUpdateBean extends BaseBean {
    private String version_code;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }
}
