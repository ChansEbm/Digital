package com.szbb.pro.adapters;


import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/12/4.
 */
public abstract class JsonObjectController<T> implements OkHttpResponseListener<T> {

    @Override
    public void onJsonArrayResponse(List<T> t, NetworkParams paramsCode) {

    }

}
