package air.tv.douyu.android.bases.application;

import android.util.Log;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.utils.JsonUtil;

import java.util.UUID;

import air.tv.douyu.android.bases.activities.LoginActivity;
import air.tv.douyu.android.beans.Setting;
import air.tv.douyu.android.beans.User;
import air.tv.douyu.android.enums.AuthorizeStatus;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class DouyuTv extends ApplicationFramework {
    private final static String TAG = "DouyuTv";
    private static DouyuTv mInstance = null;
    private Setting mSetting = null;
    private User mUser = null;

    public static DouyuTv getInstance() {
        return mInstance;
    }

    public final void clearAuthorize() {
        setUser(null);
    }

    public Setting getSetting() {
        if (mSetting == null) {
            mSetting = readSetting();
        }

        return mSetting;
    }

    public String getUUID() {
        String uuid = readString("device_id", null);

        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            writeString("device_id", uuid);
        }

        return uuid;
    }

    public User getUser() {
        if (mUser == null) {
            mUser = readUser();
        }

        return mUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public Setting readSetting() {
        Setting setting = JsonUtil.toObject(readString("setting", null), Setting.class);

        if (setting == null) {
            setting = new Setting();
            writeSetting(setting);
        }

        return setting;
    }

    public final User readUser() {
        return JsonUtil.toObject(readString("user", null), User.class);
    }

    public void setSetting(Setting setting) {
        mSetting = setting;
        writeSetting(mSetting);
    }

    public final void setUser(User user) {
        mUser = user;
        writeUser(mUser);
    }

    /**
     * 验证授权信息
     *
     * 如果不通过授权，则自动启动登录页面
     *
     * @param framework
     *         要启动登录页面的框架
     *
     * @return 是否通过验证
     */
    public final boolean validateAuthorize(IFramework framework) {
        if (validateAuthorize() == AuthorizeStatus.Authorized) {
            return true;
        } else {
            framework.start(LoginActivity.create(framework.getContext()));

            return false;
        }
    }

    /**
     * 验证授权信息
     *
     * @return 授权状态
     *
     * @see air.tv.douyu.android.enums.AuthorizeStatus
     */
    public final AuthorizeStatus validateAuthorize() {
        User user = getUser();

        if (user == null) {
            Log.e(TAG, "Unauthorized!");

            return AuthorizeStatus.Unauthorized;
        }
        Log.e(null, "authorize " + System.currentTimeMillis() / 1000l + ":" + user.getToken_exp());
        if (System.currentTimeMillis() / 1000l > user.getToken_exp()) {
            Log.e(TAG, "Authorize Expired!");
            clearAuthorize();

            return AuthorizeStatus.AuthorizeExpired;
        }
        Log.e(TAG, "Authorized!");

        return AuthorizeStatus.Authorized;
    }

    public void writeSetting(Setting setting) {
        writeString("setting", JsonUtil.toString(setting));
    }

    public final void writeUser(User user) {
        writeString("user", JsonUtil.toString(user));
    }
}