package com.szbb.pro.entity;

import java.io.Serializable;

/**
 * Created by ChanZeeBm on 2015/10/19.
 */
public class SkillBean extends TextBean implements Serializable {
    private boolean isChosen;

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChoosen(boolean isChosen) {
        this.isChosen = isChosen;
    }


}
