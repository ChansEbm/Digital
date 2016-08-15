package com.szbb.pro.widget.deleter;

/**
 * Created by KenChan on 16/5/27.
 */
public interface DeleterLayoutCallback {
    void photoAdd(String photoPath, DeleterImageView deleterImageView);

    void placerAdd(String photoPath, DeleterImageView deleterImageView);

    void photoDelete(String photoPath, DeleterImageView deleterImageView);
}
