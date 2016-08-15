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
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.szbb.pro.BuildConfig;
import com.szbb.pro.entity.eventbus.ChatListEvent;
import com.szbb.pro.entity.eventbus.UpdateListBadgeEvent;
import com.szbb.pro.service.ChatService;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.FileTools;
import com.szbb.pro.tools.LogTools;
import com.tencent.TIMElem;
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
            public boolean onNewMessages(List<TIMMessage> list) {
                EventBus.getDefault().post(new ChatListEvent(list));//接收聊天消息后发送给相应页面更新数据(CustomerActivity)
                EventBus.getDefault().post(new UpdateListBadgeEvent());
                return false;
            }
        });


        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        Intent intent = new Intent(this, ChatService.class);
        startService(intent);

    }

    private void initGalleryFinal() {
        ImageLoader imageloader = new PicassoImageLoader();
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
                .setTakePhotoFolder(new File(AppTools
                        .getPictureCacheDir()))
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class PicassoImageLoader
            implements cn.finalteam.galleryfinal.ImageLoader {

        private Bitmap.Config mConfig;

        public PicassoImageLoader() {
            this(Bitmap.Config.RGB_565);
        }

        public PicassoImageLoader(Bitmap.Config config) {
            this.mConfig = config;
        }

        @Override
        public void displayImage(Activity activity,
                                 String path,
                                 GFImageView imageView,
                                 Drawable defaultDrawable,
                                 int width,
                                 int height) {
            Picasso
                    .with(activity)
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
