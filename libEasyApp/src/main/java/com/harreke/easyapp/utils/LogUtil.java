package com.harreke.easyapp.utils;

import android.util.Log;

import com.harreke.easyapp.BuildConfig;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/08
 */
public class LogUtil {
    private String mTag;

    public LogUtil() {
        this(null);
    }

    public LogUtil(Object object) {
        mTag = object.getClass().getSimpleName();
    }

    public void show(Object message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.e(mTag, message.toString());
        }
    }
}