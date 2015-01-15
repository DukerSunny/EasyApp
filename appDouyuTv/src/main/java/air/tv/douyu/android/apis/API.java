package air.tv.douyu.android.apis;

import android.os.Build;

import com.harreke.easyapp.enums.RequestMethod;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.utils.StringUtil;

import air.tv.douyu.android.bases.application.DouyuTv;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class API {
    public final static String HOST = "http://www.douyutv.com";
    public final static String HOST_API = HOST + "/api/client";
    private final static String ANDROID = "android";
    private final static String APPID = "appid";
    private final static String CLIENT_SYS = "client_sys";
    private final static String COUNT = "count";
    private final static String DEVID = "devid";
    private final static String DYSJAPP = "dysjapp";
    private final static String KEY = "key";
    private final static String LIMIT = "limit";
    private final static String MD5 = "md5";
    private final static String MOBILE = "mobile";
    private final static String OFFSET = "offset";
    private final static String OSTYPE = "ostype";
    private final static String OSVERSION = "osversion";
    private final static String PASSWORD = "password";
    private final static String SIGN = "sign";
    private final static String TIME = "time";
    private final static String TOKEN = "token";
    private final static String TYPE = "type";
    private final static String USERID = "userid";
    private final static String USERNAME = "username";

    public static RequestBuilder getAdvertise() {
        long time = System.currentTimeMillis();
        String devid = DouyuTv.getInstance().getUUID();
        String sign = StringUtil.toMD5("devid=" + devid + "&key=dysjapp&time=" + time + "&type=android&");

        return new RequestBuilder(RequestMethod.GET, HOST_API + "/app_api/get_app_list").addQuery(DEVID, devid)
                .addQuery(TIME, time).addQuery(TYPE, ANDROID).addQuery("sign", sign);
    }

    public static RequestBuilder getAdvertiseAdd(int id, String android_id) {
        DouyuTv douyuTv = DouyuTv.getInstance();
        long time = System.currentTimeMillis();
        int uid = douyuTv.readUser().getUid();
        String devid = douyuTv.getUUID();
        String sign = StringUtil
                .toMD5("adid=" + id + "&appid=" + android_id + "&devid=" + devid + "&key=dysjapp&ostype=0&osversion=" +
                        Build.VERSION.RELEASE + "&time=" + time + "&userid=" + uid + "&");

        return new RequestBuilder(RequestMethod.GET, HOST_API + "/app_api/set_data").addQuery("adid", id)
                .addQuery(APPID, android_id).addQuery(DEVID, devid).addQuery(KEY, DYSJAPP).addQuery(OSTYPE, 0)
                .addQuery(OSVERSION, Build.VERSION.RELEASE).addQuery(TIME, time).addQuery(USERID, uid).addQuery(SIGN, sign);
    }

    public static RequestBuilder getFollow(int pageSize, int pageNo) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/follow").addQuery(COUNT, pageSize)
                .addQuery(OFFSET, (pageNo - 1) * pageSize).addQuery(TOKEN, DouyuTv.getInstance().getUser().getToken())
                .addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getFollowAdd(int roomId) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/follow/add/" + roomId)
                .addQuery(TOKEN, DouyuTv.getInstance().getUser().getToken()).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getFollowCheck(int roomId) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/follow/check/" + roomId)
                .addQuery(TOKEN, DouyuTv.getInstance().getUser().getToken()).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getFollowRemove(int roomId) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/follow/del/" + roomId)
                .addQuery(TOKEN, DouyuTv.getInstance().getUser().getToken()).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getForot() {
        return new RequestBuilder(RequestMethod.GET, HOST + "/member/findpassword").addQuery(MOBILE, true)
                .addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getGame() {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/game").addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getHistory() {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/history")
                .addQuery(TOKEN, DouyuTv.getInstance().getUser().getToken()).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getLive(int pageSize, int pageNo) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/live").addQuery(LIMIT, pageSize)
                .addQuery(OFFSET, (pageNo - 1) * pageSize).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getLive(int cateId, int limit, int pageNo) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/live/" + cateId).addQuery(LIMIT, limit)
                .addQuery(OFFSET, (pageNo - 1) * limit).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getLogin(String username, String password) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/login").addQuery(USERNAME, username)
                .addQuery(PASSWORD, StringUtil.toMD5(password)).addQuery(TYPE, MD5);
    }

    public static RequestBuilder getRecommend() {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/channel").addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getRoom(int roomId) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/room/" + roomId);
    }

    public static RequestBuilder getSearch(String query, int liveStatus, int pageSize, int pageNo) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/search/" + query + "/" + liveStatus).addQuery(LIMIT, pageSize)
                .addQuery(OFFSET, (pageNo - 1) * pageSize).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getSlide(int limit) {
        return new RequestBuilder(RequestMethod.GET, HOST_API + "/slide/" + limit).addQuery(CLIENT_SYS, ANDROID);
    }
}