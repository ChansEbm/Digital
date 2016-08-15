package com.szbb.pro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.OkHttpResponseListener;
import com.szbb.pro.model.NetworkModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.ChatManager;
import com.szbb.pro.tools.LogTools;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ResetUnreadService extends Service implements OkHttpResponseListener<BaseBean> {
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        NetworkModel<BaseBean> networkModel = new NetworkModel<>(getApplicationContext());
        networkModel.setResultCallBack(this);
        networkModel.resetUnread(intent.getStringExtra("orderId"),
                                 NetworkParams.CUPCAKE);
        LogTools.w("resetService");
        return START_NOT_STICKY;
    }

    @Override
    public void onJsonObjectResponse (BaseBean baseBean, NetworkParams paramsCode) {
        if (baseBean.getErrorcode() == 0) {
            LogTools.w("resetServiceSuccess");
            ChatManager.setChattingFlag("");
            AppTools.sendBroadcast(new Bundle(),
                                   AppKeyMap.REFRESH_ORDER_ACTION);
            stopSelf();
        }
    }

    @Override
    public void onJsonArrayResponse (List<BaseBean> t, NetworkParams paramsCode) {

    }

    @Override
    public void onError (String error, NetworkParams paramsCode) {

    }
}
