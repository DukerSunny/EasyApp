package tv.douyu.model.bean;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public class Danmaku {
    private String content;
    private int id;
    private long time;
    private int type;

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }
}