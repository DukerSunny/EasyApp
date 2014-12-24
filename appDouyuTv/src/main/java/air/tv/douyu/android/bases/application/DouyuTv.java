package air.tv.douyu.android.bases.application;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class DouyuTv extends ApplicationFramework {
    private static DouyuTv mInstance = null;

    public static DouyuTv getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
}
