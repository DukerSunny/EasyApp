package air.tv.douyu.android.api;

import com.harreke.easyapp.requests.RequestBuilder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class API {
    public final static String HOST = "http://api.douyutv.com/api/client";
    private final static String ANDROID = "android";
    private final static String CLIENT_SYS = "client_sys";
    private final static String LIMIT = "limit";
    private final static String OFFSET = "offset";

    public static RequestBuilder getGame() {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/game").addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getLive(int cateId, int limit, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/live/" + cateId).addQuery(LIMIT, limit)
                .addQuery(OFFSET, (pageNo - 1) * limit).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getLive(int limit, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/live").addQuery(LIMIT, limit)
                .addQuery(OFFSET, (pageNo - 1) * limit).addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getRecommend() {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/channel").addQuery(CLIENT_SYS, ANDROID);
    }

    public static RequestBuilder getSlide(int limit) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/slide/" + limit);
    }
}
