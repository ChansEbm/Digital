package com.szbb.pro.library.dialog;

/**
 * Created by Win8 on 2015/11/12.
 */
public interface OnTextSetListener extends OnSetListener {

    /**
     * 监听文本设置
     *
     * @param wheelViewDialog
     * @param text
     */
    void onTextSet(WheelViewDialog wheelViewDialog, String text, int[] pos);
}
