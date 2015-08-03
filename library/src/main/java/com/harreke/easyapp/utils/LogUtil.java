package com.harreke.easyapp.utils;

/**
 * Copyright (c) 2015 All rights reserved
 * 名称：LogUtil
 * 描述：日志工具类
 *
 * @author cici
 * @date：2015/5/19 18:32
 */
public class LogUtil {

    private static boolean mDebug = true;

    private static String mTag = "cici";

    public static void setDebug(final boolean debug) {
        mDebug = debug;
    }

    public static void setTag(final String Tag) {
        mTag = Tag;
    }

    //默认Tag
    public static void e(final String msg) {
        e(mTag, msg);
    }

    public static void e(final String msg, final Throwable tr) {
        e(mTag, msg, tr);
    }

    public static void e(final int msg) {
        e(mTag, Integer.toString(msg));
    }

    public static void e(final boolean msg) {
        e(mTag, Boolean.toString(msg));
    }

    public static void e(final double msg) {
        e(mTag, Double.toString(msg));
    }

    public static void e(final long msg) {
        e(mTag, Long.toString(msg));
    }

    public static void w(final String msg) {
        w(mTag, msg);
    }

    public static void w(final String msg, final Throwable tr) {
        w(mTag, msg, tr);
    }

    public static void w(final int msg) {
        e(mTag, Integer.toString(msg));
    }

    public static void w(final boolean msg) {
        e(mTag, Boolean.toString(msg));
    }

    public static void w(final double msg) {
        e(mTag, Double.toString(msg));
    }

    public static void w(final long msg) {
        e(mTag, Long.toString(msg));
    }

    public static void d(final String msg) {
        d(mTag, msg);
    }

    public static void d(final String msg, final Throwable tr) {
        d(mTag, msg, tr);
    }

    public static void d(final int msg) {
        e(mTag, Integer.toString(msg));
    }

    public static void d(final boolean msg) {
        e(mTag, Boolean.toString(msg));
    }

    public static void d(final double msg) {
        e(mTag, Double.toString(msg));
    }

    public static void d(final long msg) {
        e(mTag, Long.toString(msg));
    }

    public static void v(final String msg) {
        v(mTag, msg);
    }

    public static void v(final String msg, final Throwable tr) {
        v(mTag, msg, tr);
    }

    public static void v(final int msg) {
        e(mTag, Integer.toString(msg));
    }

    public static void v(final boolean msg) {
        e(mTag, Boolean.toString(msg));
    }

    public static void v(final double msg) {
        e(mTag, Double.toString(msg));
    }

    public static void v(final long msg) {
        e(mTag, Long.toString(msg));
    }

    public static void i(final String msg) {
        i(mTag, msg);
    }

    public static void i(final String msg, final Throwable tr) {
        i(mTag, msg, tr);
    }

    public static void i(final int msg) {
        e(mTag, Integer.toString(msg));
    }

    public static void i(final boolean msg) {
        e(mTag, Boolean.toString(msg));
    }

    public static void i(final double msg) {
        e(mTag, Double.toString(msg));
    }

    public static void i(final long msg) {
        e(mTag, Long.toString(msg));
    }


    public static void e(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void e(final String tag, final int msg) {
        if (mDebug) {
            android.util.Log.e(tag, Integer.toString(msg));
        }
    }

    public static void e(final String tag, final boolean msg) {
        if (mDebug) {
            android.util.Log.e(tag, Boolean.toString(msg));
        }
    }

    public static void e(final String tag, final double msg) {
        if (mDebug) {
            android.util.Log.e(tag, Double.toString(msg));
        }
    }

    public static void e(final String tag, final long msg) {
        if (mDebug) {
            android.util.Log.e(tag, Long.toString(msg));
        }
    }

    public static void w(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.w(tag, msg, tr);
        }
    }

    public static void w(final String tag, final int msg) {
        if (mDebug) {
            android.util.Log.e(tag, Integer.toString(msg));
        }
    }

    public static void w(final String tag, final boolean msg) {
        if (mDebug) {
            android.util.Log.e(tag, Boolean.toString(msg));
        }
    }

    public static void w(final String tag, final double msg) {
        if (mDebug) {
            android.util.Log.e(tag, Double.toString(msg));
        }
    }

    public static void w(final String tag, final long msg) {
        if (mDebug) {
            android.util.Log.e(tag, Long.toString(msg));
        }
    }

    public static void d(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void d(final String tag, final int msg) {
        if (mDebug) {
            android.util.Log.e(tag, Integer.toString(msg));
        }
    }

    public static void d(final String tag, final boolean msg) {
        if (mDebug) {
            android.util.Log.e(tag, Boolean.toString(msg));
        }
    }

    public static void d(final String tag, final double msg) {
        if (mDebug) {
            android.util.Log.e(tag, Double.toString(msg));
        }
    }

    public static void d(final String tag, final long msg) {
        if (mDebug) {
            android.util.Log.e(tag, Long.toString(msg));
        }
    }

    public static void v(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.v(tag, msg, tr);
        }
    }

    public static void v(final String tag, final int msg) {
        if (mDebug) {
            android.util.Log.e(tag, Integer.toString(msg));
        }
    }

    public static void v(final String tag, final boolean msg) {
        if (mDebug) {
            android.util.Log.e(tag, Boolean.toString(msg));
        }
    }

    public static void v(final String tag, final double msg) {
        if (mDebug) {
            android.util.Log.e(tag, Double.toString(msg));
        }
    }

    public static void v(final String tag, final long msg) {
        if (mDebug) {
            android.util.Log.e(tag, Long.toString(msg));
        }
    }

    public static void i(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.i(tag, msg, tr);
        }
    }

    public static void i(final String tag, final int msg) {
        if (mDebug) {
            android.util.Log.e(tag, Integer.toString(msg));
        }
    }

    public static void i(final String tag, final boolean msg) {
        if (mDebug) {
            android.util.Log.e(tag, Boolean.toString(msg));
        }
    }

    public static void i(final String tag, final double msg) {
        if (mDebug) {
            android.util.Log.e(tag, Double.toString(msg));
        }
    }

    public static void i(final String tag, final long msg) {
        if (mDebug) {
            android.util.Log.e(tag, Long.toString(msg));
        }
    }
}
