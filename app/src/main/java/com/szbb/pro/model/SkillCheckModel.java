package com.szbb.pro.model;

import com.szbb.pro.entity.SkillBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/10/19.
 */
public class SkillCheckModel {
    //检查并且限定5个技能
    public static boolean checkCount(List<SkillBean> list, int pos) {
        int totalCount = 0;
        for (SkillBean bean : list) {
            if (bean.isChosen()) {
                totalCount += 1;
            }
            if (totalCount == 4 && list.get(pos).isChosen()) {
                break;
            }
            if (totalCount >= 5) {
                return false;
            }
        }
        return true;
    }
}
