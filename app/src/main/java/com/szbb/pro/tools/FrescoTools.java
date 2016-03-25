package com.szbb.pro.tools;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.szbb.pro.impl.ImageLoadComplete;

/**
 * Created by ChanZeeBm on 2015/9/10.
 */
public class FrescoTools implements ControllerListener<ImageInfo> {
    private ImageLoadComplete imageLoadComplete;

    public static FrescoTools getInstance() {
        return SingleFresco.frescoTools;
    }

    public void displayImage(String url, @NonNull SimpleDraweeView simpleDraweeView,
                             ImageLoadComplete
            imageLoadComplete) {
        this.imageLoadComplete = imageLoadComplete;
        if (checkURL(url)) {
            throw new NullPointerException("plz check url");
        }
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new
                        ResizeOptions(100, 100)).setProgressiveRenderingEnabled(true).build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(imageRequest).setControllerListener(this).setTapToRetryEnabled
                        (true).setOldController(simpleDraweeView.getController()).build();
        simpleDraweeView.setController(controller);
    }

    private boolean checkURL(String url) {
        return url == null || url.equals("");
    }

    @Override
    public void onSubmit(String s, Object o) {

    }

    @Override
    public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
        if (imageLoadComplete != null) {
            imageLoadComplete.onImageComplete(s, imageInfo);
        }
    }

    @Override
    public void onIntermediateImageSet(String s, ImageInfo imageInfo) {
        if (imageLoadComplete != null) {
            imageLoadComplete.onImageFailed(s);
        }
    }

    @Override
    public void onIntermediateImageFailed(String s, Throwable throwable) {

    }

    @Override
    public void onFailure(String s, Throwable throwable) {

    }

    @Override
    public void onRelease(String s) {

    }

    private static class SingleFresco {
        private static final FrescoTools frescoTools = new FrescoTools();
    }
}

