package com.szbb.pro.entity.EventBus;

/**
 * Created by ChanZeeBm on 2016/2/17.
 */
public class FittingNavEvent {
    private int currentPos = -1;

    public FittingNavEvent(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getCurrentPos() {
        return currentPos;
    }


}
