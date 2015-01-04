package com.harreke.easyapp.utils;

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
     * 计算更新状态文字
     *
     * 输入更新时间，返回更新状态文字，用于刷新网络数据后的提示
     *
     * @param millisec
     *         更新的格林威治时间
     * @param exactTime
     *         是否显示精确的更新时间
     *
     *         例：
     *         更新完成于5分钟之前，如果设置为精确显示，则返回完整时间“5分钟前”；否则只粗略显示“几分钟前”
     *
     * @return 更新状态文字
     */
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