package com.szbb.pro.dialog;

import android.content.Context;
import android.view.View;

import com.szbb.pro.R;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ChanZeeBm on 2016/1/5.
 */
public class MessageDialog {
    private MaterialDialog materialDialog;

    public MessageDialog(Context context) {
        materialDialog = new MaterialDialog(context).setCanceledOnTouchOutside(true);
        materialDialog.setTitle(R.string.notice);
    }

    public MessageDialog setTitle(String title) {
        materialDialog.setTitle(title);
        return this;
    }

    public MessageDialog setMessage(String message) {
        materialDialog.setMessage(message);
        return this;
    }

    public MessageDialog setPositiveButton(String positiveButton, View.OnClickListener
            onClickListener) {
        materialDialog.setPositiveButton(positiveButton, onClickListener);
        return this;
    }

    public MessageDialog setNegativeButton(String negativeButton, View.OnClickListener
            onClickListener) {
        materialDialog.setNegativeButton(negativeButton, onClickListener);
        return this;
    }

    public MessageDialog setTouchOutside(boolean touchOutside) {
        materialDialog.setCanceledOnTouchOutside(touchOutside);
        return this;
    }

    public void show() {
        materialDialog.show();
        materialDialog.getPositiveButton().setId(R.id.positive);
        materialDialog.getNegativeButton().setId(R.id.negative);
    }

    public void dismiss() {
        materialDialog.dismiss();
    }


}
