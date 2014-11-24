package tv.acfun.read.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/20
 */
public class ConnectionHelper {
    public static boolean mobileConnected = false;
    public static boolean wifiConnected = false;
    private static boolean mAutoLoadImage = false;

    public static void checkConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        mAutoLoadImage = AcFunRead.getInstance().readSetting().isAutoLoadImage();
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

    public static boolean shouldLoadImage() {
        return wifiConnected || (mobileConnected && mAutoLoadImage);
    }
}