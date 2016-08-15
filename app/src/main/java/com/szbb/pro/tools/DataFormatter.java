package com.szbb.pro.tools;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class DataFormatter {

    public static String convertMillions(long millions) {
        if (millions < 1000) {
            return "-1";
        }
        long seconds = millions / 1000;
        long minute = seconds / 60;
        long hour;
        if (minute < 60) {
            return "00时" + unitFormat(minute) + "分";
        } else {
            hour = minute / 60;
            minute = minute % 60;
//            seconds = seconds - hour * 3600 - minute * 60;
            return unitFormat(hour) + "时" + unitFormat(minute) + "分";
        }
    }

    public static String formatTimeDefaultRegex(String time, boolean isPhp) {
        try {
            long l = Long.parseLong(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return time;
        }
        Date date = new Date(Long.parseLong("" + time));
        if (isPhp) {
            date = new Date(Long.parseLong(time + "000"));
        }
        SimpleDateFormat simpleDateFormat;
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                                                    Locale.CHINA);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return simpleDateFormat.format(date);
    }

    public static String formatTimeDefaultRegex(Date date, String regex) {
        if (regex.isEmpty()) {
            regex = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regex,
                                                                 Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String formatPHPDiffTime(String phpTime) {
        long at = Long.parseLong(phpTime + "000");
        long rt = System.currentTimeMillis();
        return convertMillions(at - rt);
    }

    public static String getCurrentTime() {
        String regex = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regex,
                                                                 Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
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
