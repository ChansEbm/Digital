package com.szbb.pro.tools;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class Formatter {

    public static String convertMillions(long millions) {
        if (millions < 1000) {
            return "00:00:00";
        }
        long seconds = millions / 1000;
        long minute = seconds / 60;
        long hour;
        if (minute < 60) {
            return "00:" + unitFormat(minute) + ":" + unitFormat(seconds % 60);
        } else {
            hour = minute / 60;
            minute = minute % 60;
            seconds = seconds - hour * 3600 - minute * 60;
            return unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(seconds);
        }
    }

    public static String formatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    public static String formatTime(Date date, @NonNull String regex) {
        if (regex.isEmpty())
            regex = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regex);
        return simpleDateFormat.format(date);
    }

    public static String formatPHPDiffTime(String phpTime) {
        long at = Long.parseLong(phpTime + "000");
        long rt = System.currentTimeMillis();
        return convertMillions(at - rt);
    }

    /**
     * 格式化时间
     *
     * @param unit
     * @return
     */
    private static String unitFormat(long unit) {
        if (unit >= 0 && unit < 10) {
            return "0" + unit;
        } else {
            return unit + "";
        }
    }
}
