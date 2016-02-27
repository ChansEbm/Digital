package com.szbb.pro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by ChanZeeBm on 2016/1/4.
 */
public class NotEventEditText extends EditText {

    public NotEventEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnabled(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
