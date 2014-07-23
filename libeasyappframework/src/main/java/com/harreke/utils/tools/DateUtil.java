package com.harreke.utils.tools;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

    public static String getDayNameOfWeek() {
        return weekDays[getDayOfWeek()];
    }

    public static int getDayOfWeek() {
        return getDayOfWeek(System.currentTimeMillis());
    }

    public static int getDayOfWeek(long millisec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millisec));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 2;

        if (w < 0) {
            w = 6;
        }

        return w;
    }

    public static String getDayNameOfWeek(long millisec) {
        return weekDays[getDayOfWeek(millisec)];
    }

    public static String lastUpdate(long millisec, boolean exactTime) {
        String time;
        int sec = (int) ((System.currentTimeMillis() - millisec) / 1000);

        if (sec < 900) {
            time = "刚刚更新";
        } else if (sec >= 900 && sec < 3600) {
            if (exactTime) {
                time = String.valueOf(sec / 60) + "分钟前";
            } else {
                time = "几分钟前";
            }
        } else if (sec >= 3600 && sec < 86400) {
            if (exactTime) {
                time = String.valueOf(sec / 3600) + "小时前";
            } else {
                time = "几小时前";
            }
        } else if (sec >= 86400 && sec < 604800) {
            if (exactTime) {
                time = String.valueOf(sec / 86400) + "天之前";
            } else {
                time = "几天之前";
            }
        } else {
            time = "超过一周";
        }

        return time;
    }
}