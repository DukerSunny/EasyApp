package com.harreke.easyapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/20
 */
public class ConnectionHelper {
    public static boolean mobileConnected = false;
    public static boolean wifiConnected = false;

    public static void checkConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobileInfo != null) {
            mobileConnected = mobileInfo.isConnected();
        }
        if (wifiInfo != null) {
            wifiConnected = wifiInfo.isConnected();
        }
    }

    public static boolean isConnected() {
        return mobileConnected || wifiConnected;
    }

    public static boolean isMobileConnected() {
        return mobileConnected;
    }

    public static boolean isWifiConnected() {
        return wifiConnected;
    }
}