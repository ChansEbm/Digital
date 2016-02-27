package com.szbb.pro.adapters;


import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;

/**
 * Created by ChanZeeBm on 2015/12/4.
 */
public abstract class JsonArrayController<T> implements OkHttpResponseListener<T> {

    @Override
    public void onJsonObjectResponse(T t, NetworkParams paramsCode) {

    }
}
