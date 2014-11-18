package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class Comment {
    private int cid;
    private String content;
    private int count;
    private int deep;
    private String postDate;
    private int quoteId;
    private String userID;
    private String userImg;
    private String userName;

    public int getCid() {
        return cid;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }

    public int getDeep() {
        return deep;
    }

    public String getPostDate() {
        return postDate;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getUserName() {
        return userName;
    }
}