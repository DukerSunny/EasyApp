package com.harreke.easyappframework.samples.bases.application;

import android.app.Application;

import com.harreke.easyappframework.samples.BuildConfig;
import com.harreke.easyappframework.tools.DevUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/30
 */
public class Samples extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DevUtil.setDebug(BuildConfig.DEBUG);
    }
}