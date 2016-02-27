package com.szbb.pro.tools;

import android.view.View;

import com.szbb.pro.eum.Perform;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by ChanZeeBm on 2015/11/10.
 */
public class ObjectAnimatorTools implements Animator.AnimatorListener {

    private ObjectAnimator objectAnimator;
    private boolean isAnimationRunning = false;

    /**
     * @param view
     * @param perform
     * @param from
     * @param to
     */
    public void performAnimate(View view, Perform perform, int duration, int from, int to) {
        ViewWrapper wrapper = new ViewWrapper(view);

        switch (perform) {
            case HEIGHT:
                objectAnimator = ObjectAnimator.ofInt(wrapper, "Height", from, to);
                break;
            case WIDTH:
                objectAnimator = ObjectAnimator.ofInt(wrapper, "Width", from, to);
                break;
        }
        objectAnimator.addListener(this);
        objectAnimator.setDuration(duration).start();
    }


    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

    public boolean isAnimationRunning() {
        return isAnimationRunning;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isAnimationRunning = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isAnimationRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isAnimationRunning = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        isAnimationRunning = true;
    }

}
