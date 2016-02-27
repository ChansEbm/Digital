package com.szbb.pro.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.impl.DialListener;
import com.szbb.pro.tools.AppTools;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 打电话dialog
 * Created by ChanZeeBm on 2015/10/30.
 */
public class DialDialog {
    private MaterialDialog dialog;
    private Context context;
    private DialListener listener;

    public DialDialog(Context context, DialListener listener) {
        this.context = context;
        this.listener = listener;
        dialog = new MaterialDialog(context);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void show(@NonNull final String phoneNum) {
        dialog.setTitle(R.string.title_dial_dialog).setMessage(context.getResources().getString(R
                .string.dialog_content) + phoneNum +
                "吗?").setPositiveButton(R.string.positive, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppTools.CALL(phoneNum);
                if (listener != null) {
                    listener.dial();
                }
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.negative, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
