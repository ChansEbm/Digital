package com.szbb.pro.tools;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.szbb.pro.R;

import java.util.ArrayList;

/**
 * Created by ChanZeeBm on 2015/10/13.
 */
public class SnakeTools {
    private static Snackbar snackbar;
    private ArrayList<Snackbar> snackbars = new ArrayList<>();
    private Snackbar.Callback callback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            snackbars.remove(snackbar);
            if (snackbars.size() > 0) {
                displaySnackBar(snackbars.get(0));
            }
        }
    };

    public static SnakeTools getInstance() {
        return SingleSnake.singleBar;
    }

    private void showSnackBar(Context context, View view, CharSequence charSequence) {
        Snackbar snackbar = Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG);
        initSnackBarBackground(context, snackbar);
        displaySnackBar(snackbar);
    }

    public void showSnackBar(Context context, View view, CharSequence charSequence,
                             CharSequence actionCharSequence, View.OnClickListener
                                     listener) {
        if (listener == null || TextUtils.isEmpty(actionCharSequence)) {
            showSnackBar(context, view, charSequence);
            return;
        }
         snackbar = Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG).setAction
                (actionCharSequence, listener).setActionTextColor(context.getResources().getColor
                (R.color.color_snack_bar_action_color));
        initSnackBarBackground(context, snackbar);
        displaySnackBar(snackbar);
    }

    private void showSnackBarAtLocation(Context context, View view, CharSequence charSequence, int
            gravity) {
         snackbar = Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG);
        View parentView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentView.getLayoutParams();
        params.gravity = gravity;
        parentView.setLayoutParams(params);
        initSnackBarBackground(context, snackbar);
        displaySnackBar(snackbar);
    }

    public void showSnackBarAtLocation(Context context, View view, CharSequence charSequence,
                                       CharSequence actionCharSequence, View.OnClickListener
                                               listener, int gravity) {
        if (listener == null) {
            showSnackBarAtLocation(context, view, charSequence, gravity);
            return;
        }
         snackbar = Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG).setAction
                (actionCharSequence, listener).setActionTextColor(context.getResources().getColor
                (R.color.color_snack_bar_action_color));
        View parentView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parentView.getLayoutParams();
        params.gravity = gravity;
        parentView.setLayoutParams(params);
        initSnackBarBackground(context, snackbar);
        displaySnackBar(snackbar);
    }

    public void showSettingSnackBar(Context context, View view, CharSequence charSequence) {
        showSnackBar(context, view, charSequence, context.getString(R.string.check_setting), new
                View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AppTools.SETTING();
                    }
                });
    }

    private void initSnackBarBackground(Context context, Snackbar snackbar) {
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color
                .color_snack_bar_bg));
    }

    private void addQueue(Snackbar snackbar) {
        snackbar.setCallback(callback);
        boolean isFirst = snackbars.size() == 0;
        LogTools.w("isfirst:? " + isFirst);
        snackbars.add(snackbar);
        if (isFirst)
            displaySnackBar(snackbar);
    }

    private void displaySnackBar(Snackbar snackbar) {
        snackbar.show();
    }

    private static class SingleSnake {
        private static final SnakeTools singleBar = new SnakeTools();
    }
}
