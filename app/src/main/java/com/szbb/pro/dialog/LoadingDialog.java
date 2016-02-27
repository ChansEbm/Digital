package com.szbb.pro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.szbb.pro.LoadingLayout;
import com.szbb.pro.R;

/**
 * Created by ChanZeeBm on 2015/10/16.
 */
public class LoadingDialog extends Dialog {
    ValueAnimator valueAnimator;

    public LoadingDialog(Context context) {
        super(context, R.style.dialogDefaultStyle);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        LoadingLayout loadingLayout = DataBindingUtil.inflate(getLayoutInflater(), R.layout
                .dig_loading, null, false);
        setContentView(loadingLayout.getRoot());

        ImageView frame = loadingLayout.frame;
        valueAnimator = ObjectAnimator.ofFloat(frame,
                "rotation", 360);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(-1);
        valueAnimator.start();
    }

    @Override
    public void show() {
        if (valueAnimator != null && !valueAnimator.isStarted())
            valueAnimator.start();
        super.show();
    }

    @Override
    public void dismiss() {
        if (valueAnimator != null && valueAnimator.isStarted())
            valueAnimator.end();
        super.dismiss();
    }
}
