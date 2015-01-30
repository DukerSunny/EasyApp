package tv.douyu.model.bean;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class Advertise {
    private String android_id;
    private long ctime = 0;
    private String depict = null;
    private String down_an_url = null;
    private boolean downloaded = false;
    private long end_time = 0;
    private String gift_icon = null;
    private String icon = null;
    private int id;
    private int send_gold = 0;
    private int sort = 0;
    private long start_time = 0;
    private int status = 0;
    private String title = null;

    public String getAndroid_id() {
        return android_id;
    }

    public long getCtime() {
        return ctime;
    }

    public String getDepict() {
        return depict;
    }

    public String getDown_an_url() {
        return down_an_url;
    }

    public long getEnd_time() {
        return end_time;
    }

    public String getGift_icon() {
        return gift_icon;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public int getSend_gold() {
        return send_gold;
    }

    public int getSort() {
        return sort;
    }

    public long getStart_time() {
        return start_time;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public void setDown_an_url(String down_an_url) {
        this.down_an_url = down_an_url;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public void setGift_icon(String gift_icon) {
        this.gift_icon = gift_icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSend_gold(int send_gold) {
        this.send_gold = send_gold;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
