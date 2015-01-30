package tv.douyu.misc.util;

import android.text.TextUtils;

import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.utils.StringUtil;

import tv.douyu.control.application.DouyuTv;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/21
 */
public class EncryptionUtil {
    public static RequestBuilder encrypt(RequestBuilder requestBuilder) {
        StringBuilder stringBuilder;
        String bodyString;
        String key = CMessage.getInstance().getKeyEncryption(DouyuTv.getInstance());

        requestBuilder.addQuery("aid", "android").addQuery("client_sys", "android")
                .addQuery("time", System.currentTimeMillis() / 1000);
        stringBuilder = new StringBuilder(requestBuilder.getPath() + "?" + requestBuilder.getQueryString());
        stringBuilder.append(key);
        bodyString = requestBuilder.getBodyString();
        if (!TextUtils.isEmpty(bodyString)) {
            stringBuilder.append("&").append(bodyString);
        }
        requestBuilder.addQuery("auth", StringUtil.toMD5(stringBuilder.toString()));

        return requestBuilder;
    }
}