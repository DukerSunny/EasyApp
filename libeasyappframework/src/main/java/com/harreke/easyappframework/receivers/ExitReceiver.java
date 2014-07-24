package com.harreke.easyappframework.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.harreke.easyappframework.frameworks.activity.ActivityFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架的退出广播接收器
 *
 * @see com.harreke.easyappframework.frameworks.activity.ActivityFramework
 */
public class ExitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityFramework activity = (ActivityFramework) context;

        activity.unregisterReceiver(this);
        activity.exit(false);
    }
}