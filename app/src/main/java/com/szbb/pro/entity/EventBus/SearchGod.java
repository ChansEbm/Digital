package com.szbb.pro.entity.eventbus;

import com.szbb.pro.ui.fragment.order.NewOrderFragment;
import com.szbb.pro.ui.fragment.order.OrderSearchBaseFragment;
import com.szbb.pro.ui.fragment.order.ServicedFragment;
import com.szbb.pro.ui.fragment.order.WaitAccountFragment;

/**
 * Created by Administrator on 2016/7/28.
 */
public class SearchGod {
    private int searchIndex;
    private String[] searchFields = {};

    public SearchGod(int searchIndex, String[] searchFields) {
        this.searchIndex = searchIndex;
        this.searchFields = searchFields;
    }

    public boolean isSelf(OrderSearchBaseFragment orderBase) {
        switch (searchIndex) {
            case 0:
                return (orderBase instanceof NewOrderFragment);
            case 1:
                return (orderBase instanceof ServicedFragment);
            case 2:
                return (orderBase instanceof WaitAccountFragment);
            default:
                return false;
        }
    }

    public String[] getSearchFields() {
        return searchFields;
    }
}
