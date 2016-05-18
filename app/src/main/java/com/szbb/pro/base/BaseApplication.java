package com.szbb.pro.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.szbb.pro.BuildConfig;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.FileSaveTools;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.jpush.android.api.JPushInterface;


/**
 * Created by ChanZeeBm on 2015/9/7.
 * //                                           o8888888o
 * //                                           88" . "88
 * //                                           (| -_- |)
 * //                                           0\  =  /0
 * //                                         ___/`___'\___
 * //                                       .' \\|     |// '.
 * //                                      / \\|||  :  |||// \
 * //                                     / _||||| -:- |||||_ \
 * //                                    |   | \\\  _  /// |   |
 * //                                    | \_|  ''\___/''  |_/ |
 * //                                    \  .-\__  '_'  __/-.  /
 * //                                  ___'. .'  /--.--\  '. .'___
 * //                                ."" '<  .___\_<|>_/___. '>' "".
 * //                             | | :  `_ \`.;` \ _ / `;.`/ - ` : | |
 * //                             \ \  `_.   \_ ___\ /___ _/   ._`  / /
 * //                          ====`-.____` .__ \_______/ __. -` ___.`====
 * //                                           `=-----='
 * //
 * //                          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * //                                   佛祖保佑           永无BUG
 */

public class BaseApplication extends Application {

    private static AppCompatActivity appCompatActivity;

    public BaseApplication() {
        super();
    }

    public static AppCompatActivity getCurrentActivity() {
        return appCompatActivity;
    }

    public static void setCurrentActivity(AppCompatActivity appCompatActivity) {
        BaseApplication.appCompatActivity = appCompatActivity;
    }

    static {
        try {
            SoLoaderShim.loadLibrary("webp");
        } catch (UnsatisfiedLinkError nle) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CustomActivityOnCrash.setErrorActivityClass(ErrorActivity.class);
        AppTools.init(this);
        SDKInitializer.initialize(getApplicationContext());
        MultiDex.install(this);
        FileSaveTools.getInstance().init(this);
        Fresco.initialize(this);
        initGalleryFinal();
        Logger.init("digital").setMethodCount(3).hideThreadInfo().setLogLevel(LogLevel.FULL);
        CrashReport.initCrashReport(getApplicationContext(), "900020359", BuildConfig.isDebug);

        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

    }

    private void initGalleryFinal() {
        ImageLoader imageloader = new PicassoImageLoader();
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnablePreview(true).setMutiSelectMaxSize(8).setCropWidth(300).setCropHeight(300)
                .build();
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

    public class PicassoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

        private Bitmap.Config mConfig;

        public PicassoImageLoader() {
            this(Bitmap.Config.RGB_565);
        }

        public PicassoImageLoader(Bitmap.Config config) {
            this.mConfig = config;
        }

        @Override
        public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
            Picasso.with(activity)
                    .load(new File(path))
                    .placeholder(defaultDrawable)
                    .error(defaultDrawable)
                    .config(mConfig)
                    .resize(width, height)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
        }
    }

}
