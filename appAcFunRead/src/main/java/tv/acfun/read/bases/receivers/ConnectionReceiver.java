package tv.acfun.read.bases.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tv.acfun.read.helpers.ImageConnectionHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/20
 */
public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ImageConnectionHelper.checkConnection(context);
    }
}