package com.szbb.pro.impl;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/1/22.
 */
public interface OnErrorProductCallback {
    void onSubmit(@NonNull String detailId, @NonNull String info, @NonNull List<String> filePaths);
}
