package tv.douyu.misc.api;

import android.os.Build;

import com.harreke.easyapp.enums.RequestMethod;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.utils.StringUtil;

import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.util.EncryptionUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class API {
    //    public final static String HOST = "http://live.dz11.com";
    public final static String HOST = "http://www.douyutv.com";
    public final static String HOST_API = HOST + "/api/v1";

    public static RequestBuilder getAdvertise() {
        long time = System.currentTimeMillis();
        String devid = DouyuTv.getInstance().getUUID();
        String sign = StringUtil.toMD5("devid=" + devid + "&key=dysjapp&time=" + time + "&type=android&");

        return new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST, "api/app_api/get_app_list").addQuery("devid", devid)
                .addQuery("time", time).addQuery("type", "android").addQuery("sign", sign);
    }

    public static RequestBuilder getAdvertiseAdd(int id, String android_id) {
        DouyuTv douyuTv = DouyuTv.getInstance();
        long time = System.currentTimeMillis();
        int uid = douyuTv.readUser().getUid();
        String devid = douyuTv.getUUID();
        String sign = StringUtil
                .toMD5("adid=" + id + "&appid=" + android_id + "&devid=" + devid + "&key=dysjapp&ostype=0&osversion=" +
                        Build.VERSION.RELEASE + "&time=" + time + "&userid=" + uid + "&");

        return new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST, "api/app_api/set_data").addQuery("adid", id)
                .addQuery("appid", android_id).addQuery("devid", devid).addQuery("key", "dysjapp").addQuery("ostype", 0)
                .addQuery("osversion", Build.VERSION.RELEASE).addQuery("time", time).addQuery("userid", uid)
                .addQuery("sign", sign);
    }

    public static RequestBuilder getBindQQ(String qq) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "set_userinfo").addQuery("key", "qq")
                        .addQuery("val", qq).addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getCheckUpdate() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "apk"));
    }

    public static RequestBuilder getFollow(int pageSize, int pageNo) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "follow").addQuery("count", pageSize)
                        .addQuery("offset", (pageNo - 1) * pageSize)
                        .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getFollowAdd(int roomId) {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "follow/add/" + roomId)
                .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getFollowCheck(int roomId) {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "follow/check/" + roomId)
                .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getFollowRemove(int roomId) {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "follow/del/" + roomId)
                .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getForot() {
        return new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST, "/member/findpassword").addQuery("mobile", true);
    }

    public static RequestBuilder getGame() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "game"));
    }

    public static RequestBuilder getHistory() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "history")
                .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder getLive(int pageSize, int pageNo) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "live").addQuery("limit", pageSize)
                        .addQuery("offset", (pageNo - 1) * pageSize));
    }

    public static RequestBuilder getLive(int cateId, int limit, int pageNo) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "live/" + cateId).addQuery("limit", limit)
                        .addQuery("offset", (pageNo - 1) * limit));
    }

    public static RequestBuilder getLogin(String username, String password) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "login").addQuery("username", username)
                        .addQuery("password", StringUtil.toMD5(password)).addQuery("type", "md5"));
    }

    public static RequestBuilder getOauthQQ(String access_token) {
        String key = StringUtil.toMD5(DouyuTv.getInstance().getUUID() + access_token);

        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "oauthlogin/qq")
                .addQuery("access_token", access_token).addQuery("key", key));
    }

    public static RequestBuilder getRecommend() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "channel"));
    }

    public static RequestBuilder getRoom(int roomId) {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "room/" + roomId));
    }

    public static RequestBuilder getRoom(int roomId, String cdn) {
        return EncryptionUtil
                .encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "room/" + roomId).addQuery("cdn", cdn));
    }

    public static RequestBuilder getSearch(String query, int liveStatus, int pageSize, int pageNo) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "search/" + query + "/" + liveStatus)
                        .addQuery("limit", pageSize).addQuery("offset", (pageNo - 1) * pageSize));
    }

    public static RequestBuilder getSlide(int limit) {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "slide/" + limit));
    }

    public static RequestBuilder getUser() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.GET).setBaseUrl(HOST_API, "my_info")
                .addQuery("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postBindDomestic(String captchaCode, String validateCode) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "bindphone").addBody("yzphonenum", validateCode)
                        .addBody("phonenum_captcha", captchaCode).addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postBindForeign(String phone, String captchaCode) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "bindoutphone").addBody("bindoutphonenum", phone)
                        .addBody("outphonenum_captcha", captchaCode)
                        .addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postBindMail(String mail) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "verify_email").addBody("new_email", mail)
                        .addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postDomesticCaptchaImage() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "captcha")
                .addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postFeedBack(String title, String content, String deviceInfo) {
        DouyuTv douyuTv = DouyuTv.getInstance();

        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "report_message").addBody("title", title)
                        .addBody("content", content).addBody("type", "android").addBody("ua", deviceInfo)
                        .addBody("version", DouyuTv.Version).addBody("uid", douyuTv.getUUID())
                        .addBody("token", douyuTv.getUser().getToken()));

    }

    public static RequestBuilder postForeignCaptchaImage() {
        return EncryptionUtil.encrypt(new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "outcaptcha")
                .addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postValidateCodeViaMail(String phone, String captchaCode) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "sendphone").addBody("bindphonenum", phone)
                        .addBody("phonenum_captcha", captchaCode).addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }

    public static RequestBuilder postValidateCodeViaVoice(String phone, String captchaCode) {
        return EncryptionUtil.encrypt(
                new RequestBuilder(RequestMethod.POST).setBaseUrl(HOST_API, "sendphonevoid").addBody("bindphonenum", phone)
                        .addBody("phonenum_captcha", captchaCode).addBody("token", DouyuTv.getInstance().getUser().getToken()));
    }
}