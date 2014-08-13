package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/23
 *
 * 主界面
 */
public class MainActivity extends ActivityFramework {
    /**
     * 创建启动器
     *
     * 创建用来启动主界面的Intent
     * 每一个非安卓系统调用启动的Activity都需要一个启动器
     *
     * 注：安卓系统调用启动的Activity为AndroidManifest.xml文件中，注册了启动事件的Activity
     * <intent-filter>
     * <action android:name="android.intent.action.MAIN" />
     * <category android:name="android.intent.category.LAUNCHER" />
     * </intent-filter>
     *
     * 未完成
     *
     * @param context
     *         用来启动的context
     */
    public static Intent create(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void queryLayout() {
    }

    @Override
    public void setLayout() {
    }

    @Override
    public void startAction() {
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }
}