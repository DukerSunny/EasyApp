package tv.acfun.read.api;

import com.harreke.easyapp.requests.RequestBuilder;

import tv.acfun.read.beans.Token;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class API {
    public final static String HOST = "http://api.acfun.tv/apiserver";
    private final static String ACCESS_TOKEN = "access_token";
    private final static String CHANNELID = "channelId";
    private final static String CHANNELIDS = "channelIds";
    private final static String CLIENT_ID = "client_id";
    private final static String CONTENT = "content";
    private final static String CONTENTID = "contentId";
    private final static String FIELD = "field";
    private final static String FROMDEVICE = "fromDevice";
    private final static String OPERATOR = "operator";
    private final static String PAGENO = "pageNo";
    private final static String PAGESIZE = "pageSize";
    private final static String PARENTCHANNELID = "parentChannelId";
    private final static String PASSWORD = "password";
    private final static String Q = "q";
    private final static String QUOTEID = "quoteId";
    //    private final static String RECEIVERID = "receiverId";
    private final static String RESPONSE_TYPE = "response_type";
    private final static String SORTFIELD = "sortField";
    private final static String TYPE = "type";
    private final static String USERID = "userId";
    private final static String USERNAME = "username";

    public static RequestBuilder getArticleContent(int contentId) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/article").addQuery(CONTENTID, contentId);
    }

    public static RequestBuilder getChannel(int channelId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/channel").addQuery(CHANNELID, channelId)
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
    }

    public static RequestBuilder getChannelRecommend(String channelIds, int pageSize) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/content/recommend").addQuery(CHANNELIDS, channelIds)
                .addQuery(PAGESIZE, pageSize);
    }

    //    public static RequestBuilder getChat(Token token, int receiverId, int pageSize, int pageNo) {
    //        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/chat").addQuery(USERID, token.getUserId())
    //                .addQuery(RECEIVERID, receiverId).addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo)
    //                .addQuery(ACCESS_TOKEN, token.getAccess_token());
    //    }

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

    public static RequestBuilder getContentComment(int contentId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, "http://www.acfun.tv/comment/content/web/list")
                .addQuery(CONTENTID, contentId).addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
    }

    public static RequestBuilder getContribution(int userId, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/contribution").addQuery(USERID, userId)
                .addQuery(PAGENO, pageNo).addQuery(PAGESIZE, pageSize).addQuery(TYPE, "0");
    }

    public static RequestBuilder getFavourite(Token token, String channelIds, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/fav/content").addQuery(USERID, token.getUserId())
                .addQuery(CHANNELIDS, channelIds).addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo)
                .addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getFavouriteAdd(Token token, int contentId) {
        return new RequestBuilder(RequestBuilder.Method.POST, HOST + "/user/fav/content/add").addBody(USERID, token.getUserId())
                .addBody(CONTENTID, contentId).addBody(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getFavouriteCheck(Token token, int contentId) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/fav/content/exist")
                .addQuery(USERID, token.getUserId()).addQuery(CONTENTID, contentId)
                .addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getFavouriteRemove(Token token, int contentId) {
        return new RequestBuilder(RequestBuilder.Method.POST, HOST + "/user/fav/content/remove")
                .addBody(USERID, token.getUserId()).addBody(CONTENTID, contentId).addBody(ACCESS_TOKEN, token.getAccess_token())
                .addBody(OPERATOR, 0);
    }

    public static RequestBuilder getFullUser(int userId) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/profile").addQuery(USERID, userId);
    }

    public static RequestBuilder getFullUserByName(String username) {
        return new RequestBuilder(RequestBuilder.Method.GET, "http://hengyang.acfun.tv/usercard.aspx")
                .addQuery(USERNAME, username);
    }

    public static RequestBuilder getMail(Token token, int pageSize, int pageNo) {
        return new RequestBuilder(RequestBuilder.Method.GET, HOST + "/user/mail").addQuery(USERID, token.getUserId())
                .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo).addQuery(ACCESS_TOKEN, token.getAccess_token());
    }

    public static RequestBuilder getSearch(String q, ChannelId channelId, SortField sortField, Field field, int pageSize,
            int pageNo) {
        RequestBuilder builder =
                new RequestBuilder(RequestBuilder.Method.GET, "http://search.acfun.tv/search").addQuery(TYPE, 2).addQuery(Q, q)
                        .addQuery(FIELD, field).addQuery(PARENTCHANNELID, 63).addQuery(SORTFIELD, sortField)
                        .addQuery(PAGESIZE, pageSize).addQuery(PAGENO, pageNo);
        if (channelId.getIndex() > 0) {
            builder.addQuery(CHANNELID, channelId);
        }

        return builder;
    }

    public static RequestBuilder getToken(String username, String password) {
        return new RequestBuilder(RequestBuilder.Method.POST, "http://www.acfun.tv/oauth2/authorize.aspx")
                .addBody(USERNAME, username).addBody(PASSWORD, password).addBody(CLIENT_ID, "yU3geLTsD8vriBzy")
                .addBody(RESPONSE_TYPE, "token");
    }

    public enum ChannelId {
        all("63", 0), misc("110", 1),
        work_emotion("73", 2), dramaculture("74", 3),
        comic_novel("75", 4);
        private int mIndex;
        private String mValue;

        private ChannelId(String value, int index) {
            mValue = value;
            mIndex = index;
        }

        public static ChannelId getChannelIdByIndex(int index) {
            for (ChannelId channelId : ChannelId.values()) {
                if (channelId.mIndex == index) {
                    return channelId;
                }
            }

            return null;
        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum Field {
        title("title", 0), description("description", 1), username("username", 2);
        private int mIndex;
        private String mValue;

        private Field(String value, int index) {
            mValue = value;

            mIndex = index;
        }

        public static Field getFieldByIndex(int index) {
            for (Field field : Field.values()) {
                if (field.mIndex == index) {
                    return field;
                }
            }

            return null;
        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public enum SortField {
        score("score", 0), releaseDate("releaseDate", 1), views("views", 2), comments("comments", 3), stows("stows", 4);
        private int mIndex;
        private String mValue;

        private SortField(String value, int index) {
            mValue = value;
            mIndex = index;
        }

        public static SortField getSortFieldByIndex(int index) {
            for (SortField sortField : SortField.values()) {
                if (sortField.mIndex == index) {
                    return sortField;
                }
            }

            return null;
        }

        public int getIndex() {
            return mIndex;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }
}