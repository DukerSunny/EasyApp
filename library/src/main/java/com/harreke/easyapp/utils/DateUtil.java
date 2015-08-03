package com.harreke.easyapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 日期工具
 */
public class DateUtil {
    private static final String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};


    /**
     * 返回当前日期，例如2015-04-11
     * @param date
     * @return
     */
    public static String getCurentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 查询今日是星期几
     *
     * @return 星期几
     */
    public static String getDayNameOfWeek() {
        return weekDays[getDayOfWeek()];
    }

    /**
     * 查询指定日期是星期几
     *
     * @param millisec
     *         日期的格林威治时间
     *
     * @return 星期几
     */
    public static String getDayNameOfWeek(long millisec) {
        return weekDays[getDayOfWeek(millisec)];
    }

    /**
     * 查询指定日期是一周的第几天
     *
     * @param millisec
     *         日期的格林威治时间
     *
     * @return 一周的第几天
     */
    public static int getDayOfWeek(long millisec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millisec));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 2;

        if (w < 0) {
            w = 6;
        }

        return w;
    }

    /**
     * 查询今日是一周的第几天
     *
     * @return 一周的第几天
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(System.currentTimeMillis());
    }

    /**
     * 解析秒数并转换为时间文本
     *
     * @param seconds
     *         秒数
     * @param prefix
     *         文本前缀
     * @param suffix
     *         文本后缀
     * @param tooSoon
     *         时间小于一秒时显示的文本
     * @param tooLong
     *         时间大于一周时显示的文本
     *
     * @return 时间文本
     */
    public static String parseSeconds(int seconds, String prefix, String suffix, String tooSoon, String tooLong) {
        int minutes;
        int hours;
        int days;

        minutes = seconds / 60;
        hours = minutes / 60;
        days = hours / 24;

        if (days > 7) {
            return tooLong;
        } else if (days > 0) {
            return prefix + days + "天" + suffix;
        } else if (hours > 0) {
            return prefix + hours + "小时" + suffix;
        } else if (minutes > 0) {
            return prefix + minutes + "分钟" + suffix;
        } else if (seconds > 0) {
            return prefix + seconds + "秒" + suffix;
        } else {
            return tooSoon;
        }
    }

    /**
     * 计算从给定时间到现在的时间差文本
     *
     * @param milliseconds
     *         给定时间
     *
     * @return 时间差文本
     */
    public static String recentTime(long milliseconds) {
        int seconds = (int) ((System.currentTimeMillis() - milliseconds) / 1000);

        if (seconds < 0) {
            seconds = 0;
        }
        return parseSeconds(seconds, "", "前", "刚刚", "一周前");
    }
}