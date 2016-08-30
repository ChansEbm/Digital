package com.szbb.pro.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.github.pwittchen.prefser.library.Prefser;
import com.google.gson.Gson;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseApplication;
import com.szbb.pro.entity.jpushinfo.JpushBean;
import com.szbb.pro.entity.vip.VipInfoBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.biz.NetworkBiz;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.login.LoginActivity;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.ui.activity.vip.system_msg.AccountCementActivity;
import com.szbb.pro.ui.activity.vip.system_msg.OrderHintActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ChanZeeBm on 3/2/2016.
 */
public class JpushReceiver
        extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogTools.w(intent.getAction());
        if (intent.getAction()
                  .equals("cn.jpush.android.intent.REGISTRATION")) {
            Prefser prefser = new Prefser(AppTools.getSharePreferences());
            final String extraRegistrationId = bundle.getString(
                    JPushInterface.EXTRA_REGISTRATION_ID, "");
            LogTools.i("receiver:" + extraRegistrationId);
            if (!TextUtils.isEmpty(extraRegistrationId)) {
                prefser.put("registrationId",
                            extraRegistrationId);
                NetworkBiz networkModel = new NetworkBiz(context);
                networkModel.setDevice(extraRegistrationId,
                                       NetworkParams.CUPCAKE);
            }
        } else if (intent.getAction()
                         .equals("cn.jpush.android.intent.MESSAGE_RECEIVED")) {
            LogTools.w("message");
            PendingIntent pendingIntent = getPendingIntent(context,
                                                           bundle);
            if (pendingIntent != null) {
                notifyMsg(context,
                          bundle,
                          pendingIntent);
            }
        }
    }

    private PendingIntent getPendingIntent (Context context, Bundle bundle) {
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogTools.w(extra);
        Intent intent = new Intent();
        TaskStackBuilder taskStackBuilder;
        if (!TextUtils.isEmpty(extra)) {
            JpushBean bean = new Gson().fromJson(extra,
                                                 JpushBean.class);
            final String id = bean.getId();
            final String turnto = bean.getTurnto();
            final String type = bean.getType();
            taskStackBuilder = TaskStackBuilder.create(context);
            if (type.equals("1")) {
                switch (turnto) {
                    case "1":
                        intent.setClass(context,
                                        OrderHintActivity.class)
                              .putExtra("type",
                                        "1");
                        taskStackBuilder.addParentStack(OrderHintActivity.class);
                        break;
                    case "2":
                        intent.setClass(context,
                                        OrderHintActivity.class)
                              .putExtra("type",
                                        "2");
                        taskStackBuilder.addParentStack(OrderHintActivity.class);
                        break;
                    case "3":
                        intent.setClass(context,
                                        OrderHintActivity.class)
                              .putExtra("type",
                                        "3");
                        taskStackBuilder.addParentStack(OrderHintActivity.class);
                        break;
                    case "4":
                        intent.setClass(context,
                                        AccountCementActivity.class)
                              .putExtra("type",
                                        "1");
                        taskStackBuilder.addParentStack(AccountCementActivity.class);
                        break;
                    case "5":
                        intent.setClass(context,
                                        AccountCementActivity.class)
                              .putExtra("type",
                                        "2");
                        taskStackBuilder.addParentStack(AccountCementActivity.class);
                        break;
                    case "6":
                        intent.setClass(context,
                                        AccountCementActivity.class)
                              .putExtra("type",
                                        "3");
                        taskStackBuilder.addParentStack(AccountCementActivity.class);
                        break;
                }
            } else if (type.equals("2")) {
                if (turnto.equals("1")) {
                    intent.setClass(context,
                                    MainActivity.class);
                    taskStackBuilder.addParentStack(MainActivity.class);
                } else if (turnto.equals("2")) {
                    intent.setClass(context,
                                    MainActivity.class);
                    taskStackBuilder.addParentStack(MainActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("flag",
                             AppKeyMap.MARSHMALLOW);
                    AppTools.sendBroadcast(b,
                                           AppKeyMap.GRAB_ACTION);
                }
            } else if (type.equals("99")) {
                final AppCompatActivity currentActivity = BaseApplication.getCurrentActivity();
                if (!(currentActivity instanceof LoginActivity)) {
                    context.startActivity(intent.setClass(context,
                                                          LoginActivity.class)
                                                .setFlags
                                                        (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                                                                .FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("isBeenKick",
                                                          true));
                    Prefser prefser = new Prefser(AppTools.getSharePreferences());
                    prefser.put("VipInfo",
                                new VipInfoBean());
                    prefser.put("loginPwd",
                                "");
                }
                return null;
            }
            taskStackBuilder.addNextIntent(intent);
            return taskStackBuilder.getPendingIntent(0,
                                                     PendingIntent
                                                             .FLAG_CANCEL_CURRENT);
        }
        return null;
    }


    private void notifyMsg (Context context, Bundle bundle1, PendingIntent pendingIntent) {
        String title = bundle1.getString(JPushInterface.EXTRA_TITLE,
                                         context.getResources()
                                                .getString(R.string.app_name));
        title = TextUtils.isEmpty(title) ? context.getResources()
                                                  .getString(R.string.app_name) : title;
        String msg = bundle1.getString(JPushInterface.EXTRA_MESSAGE,
                                       "");
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.status_icon);
        builder.setColor(context.getResources()
                                .getColor(R.color.theme_primary))
               .setWhen
                       (System.currentTimeMillis())
               .setDefaults(Notification.DEFAULT_ALL);
        builder.setTicker(title)
               .setContentTitle(title)
               .setContentText(msg);
        builder.setContentIntent(pendingIntent);
        managerCompat.notify(1,
                             builder.build());
    }
}
