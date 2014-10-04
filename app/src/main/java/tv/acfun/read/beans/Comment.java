package tv.acfun.read.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class Comment {
    private String content;
    private int floorindex;
    private int id;
    private int quoteId;
    private long time;
    private User user;

    public String getContent() {
        return content;
    }

    public int getFloorindex() {
        return floorindex;
    }

    public int getId() {
        return id;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public long getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFloorindex(int floorindex) {
        this.floorindex = floorindex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
