package com.szbb.pro.impl;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public interface AreaCallBack {
    void onSelect(String provinceName, String cityName, int provinceIndex, int cityIndex);
    List<String> getProvince();
    List<List<String>> getCity();
}
