package com.harreke.easyapp.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 网络工具
 */
public class NetUtil {
    public static boolean mMobConnected = false;
    public static boolean mWifiConnected = false;

    public static void checkConnection(Context context) {
        ConnectivityManager manager;
        NetworkInfo mobInfo;
        NetworkInfo wifiInfo;

        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobInfo != null) {
            mMobConnected = mobInfo.isConnected();
        }
        if (wifiInfo != null) {
            mWifiConnected = wifiInfo.isConnected();
        }
    }

    public static String getIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip;

        if (wifiInfo != null) {
            ip = wifiInfo.getIpAddress();

            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
        } else {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                return null;
            }

            return null;
        }
    }

    public static boolean isConnected() {
        return mMobConnected || mWifiConnected;
    }
}