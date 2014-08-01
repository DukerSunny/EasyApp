package com.harreke.easyappframework.tools;

import android.util.Log;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 调试工具
 */
public class DevUtil {
    private static boolean DEBUG = false;

    public static int e(int message) {
        if (DEBUG) {
            Log.e("", String.valueOf(message));
        }

        return message;
    }

    public static String e(String message) {
        if (DEBUG) {
            if (message == null) {
                message = "";
            }
            Log.e("", message);
        }

        return message;
    }

    public static int e(String tag, int message) {
        if (DEBUG) {
            Log.e(tag, String.valueOf(message));
        }

        return message;
    }

    public static String e(String tag, String message) {
        if (DEBUG) {
            if (message == null) {
                message = "";
            }
            Log.e(tag, message);
        }

        return message;
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }
}