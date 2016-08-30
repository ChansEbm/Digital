package com.szbb.pro.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.szbb.pro.BuildConfig;
import com.szbb.pro.R;
import com.szbb.pro.entity.eventbus.ChatListEvent;
import com.szbb.pro.entity.eventbus.UpdateListBadgeEvent;
import com.szbb.pro.service.ChatService;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.FileTools;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;


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

public class BaseApplication
        extends Application {

    private static AppCompatActivity appCompatActivity;

    public BaseApplication () {
        super();
    }

    public static AppCompatActivity getCurrentActivity () {
        return appCompatActivity;
    }

    public static void setCurrentActivity (AppCompatActivity appCompatActivity) {
        BaseApplication.appCompatActivity = appCompatActivity;
    }

    static {
        try {
            SoLoaderShim.loadLibrary("webp");
        } catch (UnsatisfiedLinkError nle) {
        }
    }

    @Override
    public void onCreate () {
        super.onCreate();
        AppTools.init(this);
        SDKInitializer.initialize(getApplicationContext());
        MultiDex.install(this);
        FileTools
                .getInstance()
                .init(this);
        Fresco.initialize(this);
        initGalleryFinal();
        Logger
                .init("szbb")
                .setMethodCount(3)
                .hideThreadInfo()
                .setLogLevel(LogLevel.FULL);
        CrashReport.initCrashReport(getApplicationContext(), "900020359", BuildConfig.isDebug);
        TIMManager
                .getInstance()
                .init(this);
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages (List<TIMMessage> list) {
                EventBus.getDefault()
                        .post(new ChatListEvent(list));//接收聊天消息后发送给相应页面更新数据(CustomerActivity)
                EventBus.getDefault().post(new UpdateListBadgeEvent());
                return false;
            }
        });


        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        Intent intent = new Intent(this, ChatService.class);
        startService(intent);

    }

    private void initGalleryFinal () {
        ImageLoader imageloader = new GlideImageLoader();
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnablePreview(true)
                .setMutiSelectMaxSize(8)
                .setCropWidth(300)
                .setCropHeight(300)
                .build();
        ThemeConfig CYAN = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0x01, 0x83, 0x93))
                .setFabNornalColor(Color.rgb(0x00, 0xac, 0xc1))
                .setFabPressedColor(Color.rgb(0x01, 0x83, 0x93))
                .setCheckSelectedColor(Color.rgb(0x00, 0xac, 0xc1))
                .setCropControlColor(Color.rgb(0x00, 0xac, 0xc1))
                .setIconCamera(android.R.drawable.ic_menu_camera)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, CYAN)
                .setTakePhotoFolder(new File(AppTools.getPictureCacheDir()))
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class GlideImageLoader
            implements cn.finalteam.galleryfinal.ImageLoader {

        private Bitmap.Config mConfig;

        public GlideImageLoader () {
            this(Bitmap.Config.RGB_565);
        }

        public GlideImageLoader (Bitmap.Config config) {
            this.mConfig = config;
        }

        @Override
        public void displayImage (Activity activity,
                                  String path,
                                  final GFImageView imageView,
                                  Drawable defaultDrawable,
                                  int width,
                                  int height) {
            Glide.with(activity)
                 .load("file://" + path)
                 .placeholder(defaultDrawable)
                 .error(defaultDrawable)
                 .override(width, height)
                 .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                 .skipMemoryCache(true)
                 //.centerCrop()
                 .into(new ImageViewTarget<GlideDrawable>(imageView) {
                     @Override
                     protected void setResource (GlideDrawable resource) {
                         imageView.setImageDrawable(resource);
                     }

                     @Override
                     public void setRequest (Request request) {
                         imageView.setTag(R.id.tag_cupcake, request);
                     }

                     @Override
                     public Request getRequest () {
                         return (Request) imageView.getTag(R.id.tag_cupcake);
                     }
                 });
        }

        @Override
        public void clearMemoryCache () {
        }
    }

}
