package com.szbb.pro.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class BroadcastTools {
    public static void sendBroadcast(Context context, Bundle bundle, String action) {
        Intent intent = new Intent(action);
        if (bundle != null)
            intent.putExtras(bundle);
        else
            intent.putExtras(new Bundle());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver, String...
            actions) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions)
            intentFilter.addAction(action);
        manager.registerReceiver(receiver, intentFilter);
    }

    public static void unRegisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}
