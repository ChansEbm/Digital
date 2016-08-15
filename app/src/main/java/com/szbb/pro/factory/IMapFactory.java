package com.szbb.pro.factory;

/**
 * Created by KenChan on 16/7/9.
 */
public interface IMapFactory {
    BaiduMapper createBaiduMapper();

    TencentMapper createTencentMapper();
}
