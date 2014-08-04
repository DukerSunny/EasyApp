package com.harreke.easyappframework.samples.bases.application;

import com.harreke.easyappframework.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyappframework.samples.BuildConfig;
import com.harreke.easyappframework.tools.DevUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/30
 */
public class Samples extends ApplicationFramework {
    @Override
    public void onCreate() {
        super.onCreate();

        DevUtil.setDebug(BuildConfig.DEBUG);

        copyAsset("placeholder_1x1.png");
        copyAsset("placeholder_3x4.png");
        copyAsset("placeholder_4x3.png");
        copyAsset("placeholder_16x9.png");
    }
}