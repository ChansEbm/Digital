package com.szbb.pro.entity.eventbus;

/**
 * Created by Administrator on 2016/7/28.
 */
public class NewOrderSearchEvent {
    private String[] searchFields;

    public NewOrderSearchEvent(String[] searchFields) {
        this.searchFields = searchFields;
    }

    public String[] getSearchFields() {
        return searchFields;
    }
}
