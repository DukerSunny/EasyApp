package tv.acfun.read.helpers;

import android.content.Context;

import com.harreke.easyapp.helpers.ConnectionHelper;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/20
 */
public class ImageConnectionHelper extends ConnectionHelper {
    private static boolean mAutoLoadImage = false;

    public static void checkConnection(Context context) {
        ConnectionHelper.checkConnection(context);

        mAutoLoadImage = AcFunRead.getInstance().readSetting().isAutoLoadImage();
    }

    public static boolean shouldLoadImage() {
        return wifiConnected || (mobileConnected && mAutoLoadImage);
    }
}