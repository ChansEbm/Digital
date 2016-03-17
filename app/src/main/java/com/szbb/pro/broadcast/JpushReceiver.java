package com.szbb.pro.broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;
import com.szbb.pro.R;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ChanZeeBm on 3/2/2016.
 */
public class JpushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals("cn.jpush.android.intent.REGISTRATION")) {
            Prefser prefser = new Prefser(AppTools.getSharePreferences());
            final String extraRegistrationId = bundle.getString(JPushInterface
                    .EXTRA_REGISTRATION_ID, "");
            LogTools.i("receiver:" + extraRegistrationId);
            if (!TextUtils.isEmpty(extraRegistrationId)) {
                prefser.put("registrationId", extraRegistrationId);
            }
        } else if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")) {
            LogTools.w("message");
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.status_icon);
            builder.setColor(context.getResources().getColor(R.color.theme_primary)).setWhen
                    (System.currentTimeMillis()).setDefaults(Notification.DEFAULT_ALL);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            stackBuilder.addParentStack(WalletActivity.class);
//            stackBuilder.addNextIntent(intent);
//            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
//                    .FLAG_UPDATE_CURRENT);
            Bundle bundle1 = intent.getExtras();
            String title = bundle1.getString(JPushInterface.EXTRA_TITLE, context.getResources()
                    .getString(R.string.app_name));
            title = TextUtils.isEmpty(title) ? context.getResources()
                    .getString(R.string.app_name) : title;
            String msg = bundle1.getString(JPushInterface.EXTRA_MESSAGE, "");
            builder.setTicker(title).setContentTitle(title).setContentText(msg);
            managerCompat.notify(1, builder.build());
        }
    }

}
