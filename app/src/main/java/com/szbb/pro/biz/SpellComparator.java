package com.szbb.pro.biz;

import com.szbb.pro.entity.SpellBean;

import java.util.Comparator;

/**
 * Created by ChanZeeBm on 2015/11/6.
 */
public class SpellComparator implements Comparator<SpellBean> {

    @Override
    public int compare(SpellBean lhs, SpellBean rhs) {
        if (lhs.getFirstLetter().equals("#")) {
            return -1;
        } else if (rhs.getFirstLetter().equals("#")) {
            return 1;
        }
        return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
    }
}
