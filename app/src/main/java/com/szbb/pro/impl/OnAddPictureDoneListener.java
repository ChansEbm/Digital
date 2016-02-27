package com.szbb.pro.impl;

import android.view.View;

/**
 * Created by ChanZeeBm on 2015/11/19.
 */
public interface OnAddPictureDoneListener {
    void onAddPictureDone(View picParentView, int childViewCount);

    void onDeletePictureDone(View deleteView, int childViewCount, int childPosition, int tagPos);
}
