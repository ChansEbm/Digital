package com.szbb.pro.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.dialog.DialDialog;
import com.szbb.pro.dialog.LoadingDialog;
import com.szbb.pro.impl.DialListener;
import com.szbb.pro.impl.ImageLoadComplete;

import java.sql.Date;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChanZeeBm on 2015/9/10.
 */
public class AppTools {
    /**
     * loadingDialog
     *
     * @param fragmentActivity
     */
    public static LoadingDialog loadingDialog;
    public static SharedPreferencesTools sharedPreferencesTools;
    private static Context context;
    private static LocationTools locationTools;
    private static AppCompatActivity loadingActivity;
    private static FileTools fileTools = FileTools.getInstance();

    public static void init(Context context) {
        AppTools.context = context;
    }


    /**
     * dial somebody number
     *
     * @param phoneNum the number will be dial
     */
    public static void CALL(String phoneNum) {
        context.startActivity(SystemTools.CALL(phoneNum)
                                         .addFlags(Intent
                                                           .FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * hide soft input
     *
     * @param view the view which need to be get token
     */
    public static void hideSoftInputMethod(View view) {
        SystemTools.hideSoftInput(context,
                                  view);
    }

    public static void showSoftInputMethod(View view) {
        SystemTools.showSoftInput(context,
                                  view);
    }

    /**
     * pick single photo in phone
     *
     * @param appCompatActivity the activity which will be call onActivityResult after picked
     *                          picture
     * @param requestCode       requestCode
     */
    public static void PHOTO(AppCompatActivity appCompatActivity, int requestCode) {
        appCompatActivity.startActivityForResult(SystemIntentTools.PICTURE(),
                                                 requestCode);
    }

    /**
     * Open Setting
     *
     * @return Open Setting intent
     */
    public static void SETTING() {
        context.startActivity(SystemTools.SETTING()
                                         .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Open WIFI
     *
     * @return Open wifi intent
     */
    public static Intent WIFI() {
        return SystemTools.WIFI();
    }


    /**
     * convert millions to HH:MM:SS
     *
     * @param millions who need be format
     * @return the time after formatter
     */
    public static String convertMillions(long millions) {
        return DataFormatter.convertMillions(millions);
    }

    /**
     * format the php and java diff time
     *
     * @param phpTime phpTime
     * @return the real time and the appoint time's distance
     */
    public static String formatPHPDiffTime(@NonNull String phpTime) {
        return DataFormatter.formatPHPDiffTime(phpTime);
    }

    /**
     * format time (yyyy-MM-dd HH:mm)
     *
     * @return the time after formatter
     */
    public static String formatTime(String time, boolean isPhp) {
        return DataFormatter.formatTimeDefaultRegex(time,
                                                    isPhp);
    }

    /**
     * format Time (custom type)
     *
     * @param date  date
     * @param regex the format
     * @return the time after formatter
     */
    public static String fotmatTime(Date date, String regex) {
        return DataFormatter.formatTimeDefaultRegex(date,
                                                    regex);
    }

    /**
     * format string to html
     *
     * @param html the html character
     * @return the formatter html character
     */
    public static Spanned fromHtml(String html) {
        return Html.fromHtml(html);
    }

    /**
     * px 2 dip
     *
     * @param px the px need to be dp
     * @return the dp depend on px
     */
    public static int px2dp(int px) {
        return DensityTools.px2dip(context,
                                   px);
    }

    /**
     * dip 2 px
     *
     * @param dip the dip need to be px
     * @return the px depend on dp
     */
    public static int dip2px(int dip) {
        return DensityTools.dip2px(context,
                                   dip);
    }

    /**
     * @return status bar height
     * @see DensityTools#getStatusHeight(android.content.Context)
     */
    public static int getStatusBarHeight() {
        return ScreenTools.getStatusHeight(context);
    }

    /**
     * @return screen width
     * @see ScreenTools#getScreenWidth(android.content.Context)
     */
    public static int getScreenWidth() {
        return ScreenTools.getScreenWidth(context);
    }

    /**
     * @return screen height
     * @see ScreenTools#getScreenHeight(android.content.Context)
     */
    public static int getScreenHeight() {
        return ScreenTools.getScreenHeight(context);
    }

    /**
     * @param appCompatActivity activity
     * @return snapShotWithStatusBar
     * @see ScreenTools#snapShotWithStatusBar(android.app.Activity)
     */
    public static Bitmap snapShotWithStatusBar(AppCompatActivity appCompatActivity) {
        return ScreenTools.snapShotWithStatusBar(appCompatActivity);
    }

    /**
     * @param appCompatActivity activity
     * @return the snapShotWithoutStatusBar
     * @see ScreenTools#snapShotWithoutStatusBar(android.app.Activity)
     */
    public static Bitmap snapShotWithoutStatusBar(AppCompatActivity appCompatActivity) {
        return ScreenTools.snapShotWithoutStatusBar(appCompatActivity);
    }

    /**
     * remove all activitys
     *
     * @see UITools#removeAllActivities()
     */
    public static void removeAllActivitys() {
        UITools.removeAllActivities();
    }

    /**
     * default refresh style
     *
     * @param bgaRefreshLayout         the refresh view
     * @param bgaRefreshLayoutDelegate the refresh callback
     */
    public static void defaultRefresh(BGARefreshLayout bgaRefreshLayout, BGARefreshLayout
            .BGARefreshLayoutDelegate bgaRefreshLayoutDelegate) {
        RefreshTools.defaultRefresh(bgaRefreshLayout,
                                    context,
                                    bgaRefreshLayoutDelegate);
    }

    /**
     * verify phone number with phone regex
     *
     * @param view  the view attach with snackBar
     * @param phone the phone which need to verify
     * @return the phone qualified or not
     */
    public static boolean verifyPhone(View view, String phone) {
        return VerificationTools.verifyPhone(context,
                                             view,
                                             phone);
    }

    public static boolean verifyCitizenId(View view, String citizenId) {
        return VerificationTools.verifyCitizenID(context,
                                                 view,
                                                 citizenId);
    }

    public static boolean verifyChineseName(View view, String name) {
        return VerificationTools.verifyChineseName(context,
                                                   view,
                                                   name);
    }

    public static boolean verifyVerificationCode(View view, String verificationCode) {
        return VerificationTools.verifyVerificationCode(context,
                                                        view,
                                                        verificationCode);
    }

    public static boolean verifyZipCode(View view, String zipCode) {
        return VerificationTools.verifyZipCode(context,
                                               view,
                                               zipCode);
    }

    /**
     * verify the password
     *
     * @param view the view attach with snackBar
     * @param pwd  the passwords pwds[0] is the password,password[1] is the rePassword
     * @return the password qualified or not
     */
    public static boolean verifyPwd(View view, String... pwd) {
        return VerificationTools.verifyPwd(context,
                                           view,
                                           pwd);
    }

    /**
     * remove single activity
     *
     * @param fragmentActivity the activity which need remove
     */
    public static void removeSingleActivity(FragmentActivity fragmentActivity) {
        UITools.removeSingleActivity(fragmentActivity);
    }

    /**
     * add the activity to activity list
     *
     * @param fragmentActivity the activity which need to add
     */
    public static void addActivity(FragmentActivity fragmentActivity) {
        UITools.addActivity(fragmentActivity);
    }

    /**
     * show dial dialog
     *
     * @param fragmentActivity the attach window
     * @param phoneNum         the phone number whcich need to dial
     * @param listener         the callback
     */
    public static void showDialDialog(FragmentActivity fragmentActivity, String phoneNum,
                                      DialListener listener) {
        DialDialog dialDialog = new DialDialog(fragmentActivity,
                                               listener);
        dialDialog.call(phoneNum);
    }

    /**
     * show snackBar whom can be with action or not
     *
     * @param view               the view which snackBar attach
     * @param charSequence       the shown msg
     * @param actionCharSequence the action msg
     * @param onClickListener    the action callback
     */
    public static void showActionSnackBar(View view, CharSequence charSequence, CharSequence
            actionCharSequence, View.OnClickListener
                                                  onClickListener) {
        SnakeTools.getInstance()
                  .showSnackBar(context,
                                view,
                                charSequence,
                                actionCharSequence,
                                onClickListener);
    }

    public static void showNormalSnackBar(View view, CharSequence charSequence) {
        showActionSnackBar(view,
                           charSequence,
                           null,
                           null);
    }

    public static void showSettingSnackBar(View view, CharSequence charSequence) {
        new SnakeTools().showSettingSnackBar(context,
                                             view,
                                             charSequence);
    }

    /**
     * show snack bar at specified location
     *
     * @param view               the view which snackBar need to attach with
     * @param charSequence       the show charSequence
     * @param actionCharSequence the action charSequence
     * @param listener           the action click listener
     * @param gravity            the show location
     * @see android.view.Gravity
     * .lang.CharSequence, java.lang.CharSequence, android.view.View.OnClickListener, int)
     */
    public static void showSnackBarAtLocation(View view, CharSequence charSequence,
                                              CharSequence actionCharSequence, View.OnClickListener
                                                      listener, int gravity) {
        new SnakeTools().showSnackBarAtLocation(context,
                                                view,
                                                charSequence,
                                                actionCharSequence,
                                                listener,
                                                gravity);
    }

    /**
     * get file from assets
     *
     * @param fileName the fileName
     * @return the text in file
     */
    public static String getFromAssets(@NonNull String fileName) {
        return ParserTools.getFromAssets(context,
                                         fileName);
    }

    /**
     * start locate
     *
     * @param listener the callback when locate is finished
     */
    public static void locate(BDLocationListener listener) {
        if (CheckNetworkTools.isNetworkAvailable(context)) {
            locationTools = new LocationTools(context,
                                              listener);
            locationTools.start();
        } else {
            Toast.makeText(context,
                           "请先打开网络",
                           Toast.LENGTH_SHORT)
                 .show();
            sendBroadcast(new Bundle(),
                          AppKeyMap.NO_NETWORK_ACTION);
        }
    }

    /**
     * stop locate
     */
    public static void stopLocate() {
        if (locationTools == null) {
            return;
        }
        locationTools.stop();
    }

    /**
     * send receiver
     *
     * @param bundle the params in receiver
     * @param action the action be filtered
     */
    public static void sendBroadcast(Bundle bundle, @NonNull String action) {
        BroadcastTools.sendBroadcast(context,
                                     bundle,
                                     action);
    }

    /**
     * register receiver
     *
     * @param receiver the receiver will be register
     * @param actions  which action must be filtered
     */
    public static void registerBroadcast(@NonNull BroadcastReceiver receiver, @NonNull String...
            actions) {
        BroadcastTools.registerReceiver(context,
                                        receiver,
                                        actions);
    }

    /**
     * unregister the receiver
     *
     * @param receiver the receiver which will be unregister(cannot be null)
     */
    public static void unregisterBroadcast(@NonNull BroadcastReceiver receiver) {
        BroadcastTools.unRegisterReceiver(context,
                                          receiver);
    }

    /**
     * default horizontal decoration in RecyclerView's ItemDecoration
     *
     * @return default horizontal decoration
     */
    public static RecyclerView.ItemDecoration defaultHorizontalDecoration() {
        return DefaultDecorationTools.defaultHorizontalDecoration(context);
    }

    /**
     * default vertical decoration in RecyclerView's ItemDecoration
     *
     * @return default vertical decoration
     */
    public static RecyclerView.ItemDecoration defaultVerticalDecoration() {
        return DefaultDecorationTools.defaultVerticalDecoration(context);
    }

    /**
     * set window background color (e.g use for popup window when it shown)
     *
     * @param appCompatActivity activity -- which background will be changed
     * @param alpha             alpha take from 0.0f-1.0f
     */
    public static void setWindowBackground(AppCompatActivity appCompatActivity, float alpha) {
        WindowManagerTools.setWindowBackground(appCompatActivity,
                                               alpha);
    }

    /**
     * show the loading dialog
     *
     * @param appCompatActivity the dialog attach window
     */
    public static void showLoadingDialog(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != loadingActivity) {
            loadingDialog = new LoadingDialog(appCompatActivity);
        }
        if (!loadingDialog.isShowing() && !appCompatActivity.isFinishing()) {
            loadingDialog.show();
        }
        loadingActivity = appCompatActivity;
    }

    /**
     * dismiss loading dialog
     */
    public static void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingActivity = null;
        }
    }


    /**
     * get picture cache dir
     *
     * @return the picture cache dir
     */
    public static String getPictureCacheDir() {
        return fileTools.getPictureCacheDir();
    }

    public static boolean isFileOverLimitSize(String filePath, long sizeLimit) {
        return fileTools.isFileOverLimitSize(filePath,
                                             sizeLimit);
    }

    /**
     * String format to md5, for avoid the wrong char in android
     *
     * @param str the string which need to convert
     * @return the converted md5 String
     */
    public static String stringToMD5(String str) {
        return StringFormatTools.stringToMD5(str);
    }

    public static SharedPreferencesTools putStringSharedPreferences(String key, String value) {
        initSharedPreferencesTools();
        sharedPreferencesTools.putString(key,
                                         value)
                              .commit();
        return sharedPreferencesTools;
    }


    public static String getStringSharedPreferences(String key, String
            defaultValue) {
        initSharedPreferencesTools();
        return sharedPreferencesTools.getString(key,
                                                defaultValue);
    }

    private static void initSharedPreferencesTools() {
        if (sharedPreferencesTools == null) {
            sharedPreferencesTools = new SharedPreferencesTools(context);
        }
    }

    public static SharedPreferences getSharePreferences() {
        initSharedPreferencesTools();
        return sharedPreferencesTools.getSharedPreferences();
    }

    public static void deleteDrawable(TextView textView) {
        ViewUtils.deleteDrawable(textView);
    }

    protected static void setCompoundDrawable(TextView textView, int resId, int side) {
        ViewUtils.setCompoundDrawable(textView,
                                      resId,
                                      side);
    }


    /**
     * @return true if one of network is Available false otherwise
     * @see com.szbb.pro.tools.CheckNetworkTools#isNetworkAvailable(android.content.Context)
     */
    public static boolean isNetworkConnected() {
        return CheckNetworkTools.isNetworkAvailable(context);
    }

    /**
     * @return true if wifi is connected false otherwise
     * @see CheckNetworkTools#isWifiState(android.content.Context)
     */
    public static boolean isWifiConnected() {
        return CheckNetworkTools.isWifiState(context);
    }

    /**
     * @see CheckNetworkTools#openWifi(android.content.Context)
     */
    public static void openWifi() {
        CheckNetworkTools.openWifi(context);
    }
}

