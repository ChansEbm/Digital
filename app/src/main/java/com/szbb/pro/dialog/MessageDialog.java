package com.szbb.pro.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.szbb.pro.R;

import java.lang.reflect.Field;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ChanZeeBm on 2016/1/5.
 */
public class MessageDialog {
    private MaterialDialog materialDialog;
    private View.OnClickListener onClickListener;
    private String positive = "";
    private String negative = "";

    public MessageDialog(Context context) {
        materialDialog = new MaterialDialog(context).setCanceledOnTouchOutside(true)
                .setPositiveButton(positive, onClickListener).setNegativeButton(negative,
                        onClickListener);
        setPositiveId();
        setNegativeId();
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
        setPositiveId();
        return this;
    }

    public MessageDialog setNegativeButton(String negativeButton, View.OnClickListener
            onClickListener) {
        materialDialog.setNegativeButton(negativeButton, onClickListener);
        setNegativeId();
        return this;
    }

    public void show() {
        materialDialog.show();
    }

    public void dismiss() {
        materialDialog.dismiss();
    }

    private void setPositiveId() {
        Button button;
        try {
            Field f = materialDialog.getClass().getDeclaredField("mPositiveButton");
            f.setAccessible(true);
            button = (Button) f.get(materialDialog);
            button.setId(R.id.positive);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setNegativeId() {
        Button button;
        try {
            Field f = materialDialog.getClass().getDeclaredField("mNegativeButton");
            f.setAccessible(true);
            button = (Button) f.get(materialDialog);
            button.setId(R.id.negative);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
