package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.ShareLayout;
import com.szbb.pro.entity.Base.Events;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by ChanZeeBm on 2016/3/1.
 */
public class SharePopupWindow extends BasePopupWindow implements PlatformActionListener {
    private ShareLayout shareLayout;
    private Platform.ShareParams shareParams;
    private Platform platform;

    public SharePopupWindow(Context context) {
        super(context);
        shareLayout = (ShareLayout) viewDataBinding;
        Events events = new Events();
        events.setOnClickListener(this);
        shareLayout.setShare(events);
    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_share;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.wechat:
                shareParams = new Wechat.ShareParams();
                shareParams.setTitle("ShareTitle");
                shareParams.setText("This is text");
                shareParams.setImageUrl("http://www.aimis.com.cn/upload/2015031133975205.png");
                shareParams.setUrl("http://www.baidu.com");
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                platform = ShareSDK.getPlatform(Wechat.NAME);
                platform.setPlatformActionListener(this);
                platform.share(shareParams);
                break;
            case R.id.wechat_moment:
                shareParams = new WechatMoments.ShareParams();
                shareParams.setTitle("ShareTitle");
                shareParams.setText("This is text");
                shareParams.setImageUrl("http://www.aimis.com.cn/upload/2015031133975205.png");
                shareParams.setUrl("http://www.baidu.com");
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                platform = ShareSDK.getPlatform(WechatMoments.NAME);
                platform.setPlatformActionListener(this);
                platform.share(shareParams);
                break;
            case R.id.qq:
                shareParams = new QQ.ShareParams();
                shareParams.setTitle("QQ Share Title");
                shareParams.setText("is the contant?");
                shareParams.setTitleUrl("www.baidu.com");
                shareParams.setImageUrl("http://www.aimis.com.cn/upload/2015031133975205.png");
                platform = ShareSDK.getPlatform(QQ.NAME);
                platform.setPlatformActionListener(this);
                platform.share(shareParams);
                break;
            case R.id.qzone:
                shareParams = new QZone.ShareParams();
                shareParams.setTitle("QQ Share Title");
                shareParams.setText("is the contant?");
                shareParams.setTitleUrl("www.baidu.com");
                shareParams.setImageUrl("http://www.aimis.com.cn/upload/2015031133975205.png");
                shareParams.setSite("szbb");
                shareParams.setSiteUrl("http://www.szlb.net/");
                platform = ShareSDK.getPlatform(QZone.NAME);
                platform.setPlatformActionListener(this);
                platform.share(shareParams);
                break;
        }
        dismiss();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
