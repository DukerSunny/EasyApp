package air.tv.douyu.android.bases.application;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;

import java.util.UUID;

import air.tv.douyu.android.beans.User;
import air.tv.douyu.android.enums.AuthorizeStatus;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class DouyuTv extends ApplicationFramework {
    private final static String TAG = "DouyuTv";
    private static DouyuTv mInstance = null;

    public static DouyuTv getInstance() {
        return mInstance;
    }

    public String getUUID() {
        String uuid = readString("device_id", null);

        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            writeString("device_id", uuid);
        }

        return uuid;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public final User readUser() {
        return JSON.parseObject(readString("user", null), User.class);
    }

    public final AuthorizeStatus validateAuthorize() {
        return validateAuthorize(readUser());
    }

    public final AuthorizeStatus validateAuthorize(User user) {
        if (user == null) {
            Log.e(TAG, "Unauthorized!");

            return AuthorizeStatus.Unauthorized;
        }
        Log.e(null, "authorize " + System.currentTimeMillis() / 1000l + ":" + user.getToken_exp());
        if (System.currentTimeMillis() / 1000l >= user.getToken_exp()) {
            Log.e(TAG, "Authorize Expired!");
            writeUser(null);

            return AuthorizeStatus.AuthorizeExpired;
        }
        Log.e(TAG, "Authorized!");

        return AuthorizeStatus.Authorized;
    }

    public final void writeUser(User user) {
        writeString("user", JSON.toJSONString(user));
    }
}