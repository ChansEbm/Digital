package com.szbb.pro.entity.EventBus;

import android.content.Intent;

/**
 * Created by ChanZeeBm on 2016/2/1.
 */
public class AreaEvent {
    private Intent intent;

    public AreaEvent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
