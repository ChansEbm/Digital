package com.szbb.pro.impl;

import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Created by ChanZeeBm on 2015/9/10.
 */
public interface ImageLoadComplete {
    void onImageComplete(String id, ImageInfo imageInfo);

    void onImageFailed(String id);
}
