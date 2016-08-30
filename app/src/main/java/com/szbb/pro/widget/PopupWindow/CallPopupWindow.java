package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.PopupCallLayout;
import com.szbb.pro.R;
import com.szbb.pro.impl.CallActionCallback;

/**
 * Created by ChanZeeBm on 2015/11/18.
 */
public class CallPopupWindow
        extends BasePopupWindow {
    private PopupCallLayout callLayout;
    private CallActionCallback callActionCallback;

    public CallPopupWindow (@NonNull Context context) {
        super(context);
        callLayout = (PopupCallLayout) viewDataBinding;
        callLayout.btnCancel.setOnClickListener(this);
        callLayout.btnCall1.setOnClickListener(this);
        callLayout.btnCall2.setOnClickListener(this);
    }

    public CallPopupWindow setCallNumbers (@Size(2) String dials[]) {
        callLayout.btnCall1.setText(dials[0]);
        callLayout.btnCall2.setText(dials[1]);
        return this;
    }

    @Override
    public int getPopupLayout () {
        return R.layout.popup_take_photo;
    }

    @Override
    public void onClick (View v) {
        int id = v.getId();
        String numbers = null;
        switch (id) {
            case R.id.btn_call1:
                numbers = callLayout.btnCall1.getText().toString();
                break;
            case R.id.btn_call2:
                numbers = callLayout.btnCall2.getText().toString();
                break;
        }
        if (callActionCallback != null && !TextUtils.isEmpty(numbers)) {
            callActionCallback.onCall(numbers);
        }
        dismiss();
    }

    @Override public void showAtLocation (View parent, int gravity, int x, int y) {
        if (isNumberNotSet()) {
            return;
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override public void showAsDropDown (View anchor) {
        if (isNumberNotSet()) {
            return;
        }
        super.showAsDropDown(anchor);
    }

    @Override public void showAsDropDown (View anchor, int xoff, int yoff) {
        if (isNumberNotSet()) {
            return;
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override public void showAsDropDown (View anchor, int xoff, int yoff, int gravity) {
        if (isNumberNotSet()) {
            return;
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override public void showAtDefaultLocation () {
        if (isNumberNotSet()) {
            return;
        }
        super.showAtDefaultLocation();
    }

    private boolean isNumberNotSet () {
        return TextUtils.isEmpty(callLayout.btnCall1.getText().toString()) ||
               TextUtils.isEmpty(callLayout.btnCall2.getText().toString());
    }

    public void setCallActionCallback (CallActionCallback callActionCallback) {
        this.callActionCallback = callActionCallback;
    }
}
