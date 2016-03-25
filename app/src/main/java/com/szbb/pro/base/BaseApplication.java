package com.szbb.pro.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.FileSaveTools;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.jpush.android.api.JPushInterface;


/**
 * Created by ChanZeeBm on 2015/9/7.
 //                                           o8888888o
 //                                           88" . "88
 //                                           (| -_- |)
 //                                           0\  =  /0
 //                                         ___/`___'\___
 //                                       .' \\|     |// '.
 //                                      / \\|||  :  |||// \
 //                                     / _||||| -:- |||||_ \
 //                                    |   | \\\  _  /// |   |
 //                                    | \_|  ''\___/''  |_/ |
 //                                    \  .-\__  '_'  __/-.  /
 //                                  ___'. .'  /--.--\  '. .'___
 //                                ."" '<  .___\_<|>_/___. '>' "".
 //                             | | :  `_ \`.;` \ _ / `;.`/ - ` : | |
 //                             \ \  `_.   \_ ___\ /___ _/   ._`  / /
 //                          ====`-.____` .__ \_______/ __. -` ___.`====
 //                                           `=-----='
 //
 //                          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 //                                   佛祖保佑           永无BUG
 */

public class BaseApplication extends Application {

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppTools.init(this);
        SDKInitializer.initialize(getApplicationContext());
        MultiDex.install(this);
        FileSaveTools.getInstance().init(this);
        initGalleryFinal();
        Logger.init("digital").setMethodCount(3).hideThreadInfo().setLogLevel(LogLevel.FULL);
        CrashReport.initCrashReport(getApplicationContext(), "900020359", true);

        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

    }

    private void initGalleryFinal() {
        ImageLoader imageloader = new FrescoImageLoader(this);
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnablePreview(true).setMutiSelectMaxSize(8).build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, ThemeConfig.CYAN)
                .setDebug(BuildConfig.DEBUG).setTakePhotoFolder(new File(AppTools
                        .getPictureCacheDir()))
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class FrescoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

        private Context context;

        public FrescoImageLoader(Context context) {
            this(context, Bitmap.Config.RGB_565);
        }

        public FrescoImageLoader(Context context, Bitmap.Config config) {
            this.context = context;
            ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                    .setBitmapsConfig(config)
                    .build();
            Fresco.initialize(context, imagePipelineConfig);
        }

        @Override
        public void displayImage(Activity activity, String path, final GFImageView imageView,
                                 final Drawable defaultDrawable, int width, int height) {
            Resources resources = context.getResources();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                    .setFadeDuration(300)
                    .setPlaceholderImage(defaultDrawable)
                    .setFailureImage(defaultDrawable)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();

            final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create
                    (hierarchy, context);
            imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
                @Override
                public void onDetach() {
                    draweeHolder.onDetach();
                }

                @Override
                public void onAttach() {
                    draweeHolder.onAttach();
                }

                @Override
                public boolean verifyDrawable(Drawable dr) {
                    if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDraw(Canvas canvas) {
                    Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                    if (drawable == null) {
                        imageView.setImageDrawable(defaultDrawable);
                    } else {
                        imageView.setImageDrawable(drawable);
                    }
                }
            });
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(path)
                    .build();
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))//图片目标大小
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeHolder.getController())
                    .setImageRequest(imageRequest)
                    .build();
            draweeHolder.setController(controller);
        }

        @Override
        public void clearMemoryCache() {

        }
    }
}
