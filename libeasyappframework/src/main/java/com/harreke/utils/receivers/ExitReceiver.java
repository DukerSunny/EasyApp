package com.harreke.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.harreke.utils.frameworks.activity.ActivityFramework;

public class ExitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityFramework activity = (ActivityFramework) context;

        activity.unregisterReceiver(this);
        activity.exit(false);
    }
}