package tv.acfun.read.api;

import com.harreke.easyapp.requests.RequestBuilder;

import tv.acfun.read.beans.Token;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class API {
    public static String HOST = "http://api.acfun.tv/apiserver";
    private static String ACCESS_TOKEN = "access_token";
    private static String CHANNELID = "channelId";
    private static String CHANNELIDS = "channelIds";
    private static String CLIENT_ID = "client_id";
    private static String CONTENT = "content";
    private static String CONTENTID = "contentId";
    private static String FROMDEVICE = "fromDevice";
    private static String ORDERBY = "orderBy";
    private static String ORDERID = "orderId";
    private static String PAGENO = "pageNo";
    private static String PAGESIZE = "pageSize";
    private static String PASSWORD = "password";
    private static String QUERY = "query";
    private static String QUOTEID = "quoteId";
    private final static String RECEIVERID = "receiverId";
    private static String RESPONSE_TYPE = "response_type";
    private static String USERID = "userId";
    private static String USERNAME = "username";

    /**
     * 获取指定文章投稿的内容
     *
     * @param contentId
     *         投稿Id
     *
     * @return {@link com.harreke.easyapp.requests.RequestBuilder}
     */
    public static RequestBuilder getArticleContent(int contentId) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/article").addQuery(CONTENTID, contentId);
    }

    /**
     * 获取指定分区的列表
     *
     * @param channelId
     *         分区Id
     * @param pageSize
     *         页面项目数量
     * @param pageNo
     *         页面序号
     *
     * @return {@link com.harreke.easyapp.requests.RequestBuilder}
     */
    public static RequestBuilder getChannel(int channelId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/channel").addQuery(CHANNELID, channelId)
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
    }

    /**
     * 获取指定分区的推荐列表
     *
     * @param channelIds
     *         分区Id集合，不同Id用“，”分隔
     * @param pageSize
     *         页面项目数量
     *
     * @return {@link com.harreke.easyapp.requests.RequestBuilder}
     */
    public static RequestBuilder getChannelRecommend(String channelIds, int pageSize) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/recommend").addQuery(CHANNELIDS, channelIds)
                .addQuery(PAGESIZE, pageSize);
    }

    public static RequestBuilder getChat(Token token, int receiverId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/chat").addQuery(USERID, token.getUserId())
                .addQuery(RECEIVERID, receiverId).addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo)
                .addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getCommentAdd(int contentId, int userId, String content, int quoteId, Token token) {
        RequestBuilder builder =
                new RequestBuilder(RequestBuilder.Method.POST, HOST + "/user/comment/content/add").addBody(CONTENTID, contentId)
                        .addBody(USERID, userId).addBody(CONTENT, content).addBody(FROMDEVICE, 1)
                        .addBody(ACCESS_TOKEN, token.getAccess_token());

        if (quoteId > 0) {
            builder.addBody(QUOTEID, quoteId);
        }

        return builder;
    }

    /**
     * 获取指定投稿的评论列表
     *
     * @param contentId
     *         投稿Id
     * @param pageSize
     *         页面项目数量
     * @param pageNo
     *         页面序号
     *
     * @return {@link com.harreke.easyapp.requests.RequestBuilder}
     */
    public static RequestBuilder getContentComment(int contentId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/comment/content").addQuery(CONTENTID, contentId)
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
    }

    public static RequestBuilder getFavourite(Token token, String channelIds, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/fav/content").addQuery(USERID, token.getUserId())
                .addQuery(CHANNELIDS, channelIds).addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo)
                .addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getFullUser(int userId) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/profile").addQuery(USERID, userId);
    }

    public static RequestBuilder getMail(Token token, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/mail").addQuery(USERID, token.getUserId())
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo).addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    /**
     * 获取搜索结果列表
     *
     * @param query
     *         查询内容
     * @param channelId
     *         分区Id集合，不同Id用“,”分隔
     * @param orderBy
     *         排序方式
     * @param pageSize
     *         页面项目数量
     * @param pageNo
     *         页面序号
     *
     * @return {@link com.harreke.easyapp.requests.RequestBuilder}
     */
    public static RequestBuilder getSearch(String query, int channelId, int orderBy, int orderId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/search").addQuery(QUERY, query)
                .addQuery(CHANNELID, channelId).addQuery(ORDERBY, orderBy).addQuery(ORDERID, orderId)
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
    }

    public static RequestBuilder getToken(String username, String password) {
        return new RequestBuilder(RequestBuilder.Method.POST, "http://www.acfun.tv/oauth2/authorize.aspx")
                .addBody(USERNAME, username).addBody(PASSWORD, password).addBody(CLIENT_ID, "yU3geLTsD8vriBzy")
                .addBody(RESPONSE_TYPE, "token");
    }
}