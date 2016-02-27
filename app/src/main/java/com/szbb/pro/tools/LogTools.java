package com.szbb.pro.tools;

import com.orhanobut.logger.Logger;

/**
 * Created by ChanZeeBm on 2015/10/30.
 */
public class LogTools {
    public static void i(Object objects) {
        Logger.i(objects + "");
    }

    public static void w(Object objects) {
        Logger.w(objects + "");
    }

    public static void v(Object objects) {
        Logger.v(objects + "");
    }

    public static void e(Object objects) {
        Logger.e(objects + "");
    }

    public static void wtf(Object objects) {
        Logger.wtf(objects + "");
    }


    public static void json(Object objects) {
        Logger.json(objects + "");
    }

}
