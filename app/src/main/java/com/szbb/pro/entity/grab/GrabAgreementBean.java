package com.szbb.pro.entity.grab;

import android.databinding.Bindable;

import com.szbb.pro.entity.base.BaseBean;

/**
 * Created by KenChan on 16/7/11.
 */
public class GrabAgreementBean
        extends BaseBean {
    private String protocol;

    @Bindable
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
