package tv.douyu.control.application;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.utils.JsonUtil;

import java.util.List;
import java.util.UUID;

import tv.douyu.model.bean.Setting;
import tv.douyu.model.bean.User;
import tv.douyu.model.enumeration.AuthorizeStatus;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class DouyuTv extends ApplicationFramework {
    public static String Version;
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

    public void clearSearchKeywordList() {
        writeSearchKeywordList(null);
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

    private String getVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info;
        String version;

        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
        }

        return version;
    }

    //    /**
    //     * 验证授权信息
    //     *
    //     * 如果不通过授权，则自动启动登录页面
    //     *
    //     * @param framework
    //     *         要启动登录页面的框架
    //     *
    //     * @return 是否通过验证
    //     */
    //    public final boolean validateAuthorize(IFramework framework) {
    //        if (validateAuthorize() == AuthorizeStatus.Authorized) {
    //            return true;
    //        } else {
    //            framework.start(LoginActivity.create(framework.getContext()), 0);
    //
    //            return false;
    //        }
    //    }
    public final boolean isAuthorized() {
        return validateAuthorize() == AuthorizeStatus.Authorized;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        Version = getVersion();
    }

    public List<String> readSearchKeywordList() {
        return JsonUtil.toList(readString("searchKeywordList", null), String.class);
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

    public final void updateUser(User user) {
        String password = null;

        if (mUser != null) {
            password = mUser.getPassword();
        }
        mUser = user;
        if (password != null) {
            mUser.setPassword(password);
        }
        writeUser(mUser);
    }

    /**
     * 验证授权信息
     *
     * @return 授权状态
     *
     * @see tv.douyu.model.enumeration.AuthorizeStatus
     */
    public final AuthorizeStatus validateAuthorize() {
        mUser = getUser();

        if (mUser == null) {
            //            Log.e(TAG, "Unauthorized!");

            return AuthorizeStatus.Unauthorized;
        }
        //        Log.e(null, "authorize " + System.currentTimeMillis() / 1000l + ":" + user.getToken_exp());
        if (System.currentTimeMillis() / 1000l > mUser.getToken_exp()) {
            //            Log.e(TAG, "Authorize Expired!");
            clearAuthorize();

            return AuthorizeStatus.AuthorizeExpired;
        }
        //        Log.e(TAG, "Authorized!");

        return AuthorizeStatus.Authorized;
    }

    public void writeSearchKeywordList(List<String> list) {
        writeString("searchKeywordList", JsonUtil.toString(list));
    }

    public void writeSetting(Setting setting) {
        writeString("setting", JsonUtil.toString(setting));
    }

    public final void writeUser(User user) {
        writeString("user", JsonUtil.toString(user));
    }
}