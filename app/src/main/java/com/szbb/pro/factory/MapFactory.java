package com.szbb.pro.factory;

/**
 * Created by KenChan on 16/7/9.
 */
public class MapFactory
        implements IMapFactory {

    @Override
    public BaiduMapper createBaiduMapper() {
        return new BaiduMapper();
    }

    @Override
    public TencentMapper createTencentMapper() {
        return new TencentMapper();
    }
}
