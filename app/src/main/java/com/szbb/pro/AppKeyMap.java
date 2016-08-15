package com.szbb.pro;

import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/9/7.
 */
public class AppKeyMap {
    //手机正则
    public final static String PHONE_REGEX = "[1]\\d{10}";
    //身份证正则
    public final static String CITIZEN_ID_REGEX = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})" +
            "([0-9]|X||x)$";
    //中国人名正则
    public final static String CHINESE_PEOPLE_REGEX = "(([\\u4E00-\\u9FA5]|[·]){2,15})";
    //密码正则(只能输入数字字母)
    public final static String PWD_REGEX = "^[A-Za-z0-9]+$";
    //邮编正则
    public final static String ZIP_CODE_REGEX = "\\d{6}$";
    //Action Key
    public final static String ACTION_KEY = "ACTION_KEY";
    //定位Action
    public final static String LOCATION_ACTION = "android.intent.action.LOCATION";
    //抢单ACTION
    public final static String GRAB_ACTION = "android.intent.action.GRAB";
    //预约客户成功Action
    public final static String REFRESH_ORDER_ACTION = "android.intent.action" +
            ".REFRESH_ORDER";
    //联系不上客户
    public final static String APPOINTMENT_CAN_NOT_CONTENT_CLIENT = "android.intent.action" +
            ".APPOINTMENT_CAN_NOT_CONTENT_CLIENT";

    //待结算Action
    public final static String WAITING_COST_ACTION = "android.intent.action.COST";
    //检测无网络action
    public final static String NO_NETWORK_ACTION = "android.intent.action.NO_NETWORK";
    //默认动画执行时间 300ms
    public final static int DEFAULT_DURATION = 300;

    public final static boolean IS_DEBUG = BuildConfig.isDebug;

    //be flag
    public final static int CUPCAKE = 0x01f;
    public final static int DONUT = 0x02f;
    public final static int FROYO = 0x03f;
    public final static int GINGERBREAD = 0x04f;
    public final static int HONEYCOMB = 0x05f;
    public final static int ICECREAMSANDWICH = 0x06f;
    public final static int KITKAT = 0x07f;
    public final static int LOLLIPOP = 0x08f;
    public final static int MARSHMALLOW = 0x09f;
    //http://appbaba.jios.org:8081/
    //http://kf.szlb.cc/
    public final static String HEAD = BuildConfig.HEADURL;
    public final static String HEAD_API_LOGIN = HEAD + "ApiLogin/";
    public final static String HEAD_API_WORKER = HEAD + "ApiWorker/";
    public final static String HEAD_API_ORDER = HEAD + "ApiOrder/";
    public final static String HEAD_API_ACCE_ORDER = HEAD + "ApiAcceOrder/";
    public final static String HEAD_API_NEAR_ORDER = HEAD + "ApiNearOrder/";
    public final static String HEAD_API_MEMBER = HEAD + "ApiMember/";
    public final static String HEAD_API_PAGES = HEAD + "ApiPages/";
    public final static String HEAD_API_PUSH = HEAD + "ApiPush/";
    public final static String HEAD_API_BUG_COLLECT = HEAD + "ApiBugCollect/collect";
    public final static String HEAD_ABOUT_US = HEAD_API_PAGES + "aboutUs";
    public final static String HEAD_MUSTREAD = HEAD_API_PAGES + "mustRead";
    public final static String HEAD_JOINTPRICE = HEAD_API_PAGES + "jointPrice";
    public final static String HEAD_REGISTERAGREEMENT = HEAD_API_PAGES + "registerAgreement";
    public final static String HEAD_QUERYLOGISTICS = HEAD_API_PAGES + "queryLogistics";
    public final static String REGISTERAGREEMENT = HEAD_API_PAGES + "registerAgreement";


    public final static String CONTENT_JPG = "image/jpg";
    public final static String CONTENT_PNG = "image/png";
    public final static String CONTENT_TXT = "text/plain";
    public final static String CONTENT_MP3 = "audio/mp3";
    public final static String CONTENT_OCT = "application/octet-stream";

    //用户标识
    public final static String AUTH = "auth";
    public final static String SIG = "sig";
    public final static String IDENTIFIER = "identifier";

    public static boolean isAuthEmpty() {
        return AppTools.getStringSharedPreferences(AUTH,
                                                   "")
                       .isEmpty();
    }

}
