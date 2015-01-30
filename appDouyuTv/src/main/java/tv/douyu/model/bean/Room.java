package tv.douyu.model.bean;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class Room {
    private int cate_id;
    private int fans;
    private String game_name;
    private String game_url;
    private String nickname;
    private int online;
    private int owner_uid;
    private int room_id;
    private String room_name;
    private String room_src;
    private String show_details;
    private int show_status;
    private long show_time;
    private String specific_catalog;
    private int specific_status;
    private String subject;
    private String tags;
    private String url;
    private int vod_quality;

    public int getCate_id() {
        return cate_id;
    }

    public int getFans() {
        return fans;
    }

    public String getGame_name() {
        return game_name;
    }

    public String getGame_url() {
        return game_url;
    }

    public String getNickname() {
        return nickname;
    }

    public int getOnline() {
        return online;
    }

    public int getOwner_uid() {
        return owner_uid;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_src() {
        return room_src;
    }

    public String getShow_details() {
        return show_details;
    }

    public int getShow_status() {
        return show_status;
    }

    public long getShow_time() {
        return show_time;
    }

    public String getSpecific_catalog() {
        return specific_catalog;
    }

    public int getSpecific_status() {
        return specific_status;
    }

    public String getSubject() {
        return subject;
    }

    public String getTags() {
        return tags;
    }

    public String getUrl() {
        return url;
    }

    public int getVod_quality() {
        return vod_quality;
    }

    @Override
    public int hashCode() {
        return room_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setOwner_uid(int owner_uid) {
        this.owner_uid = owner_uid;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setRoom_src(String room_src) {
        this.room_src = room_src;
    }

    public void setShow_details(String show_details) {
        this.show_details = show_details;
    }

    public void setShow_status(int show_status) {
        this.show_status = show_status;
    }

    public void setShow_time(long show_time) {
        this.show_time = show_time;
    }

    public void setSpecific_catalog(String specific_catalog) {
        this.specific_catalog = specific_catalog;
    }

    public void setSpecific_status(int specific_status) {
        this.specific_status = specific_status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVod_quality(int vod_quality) {
        this.vod_quality = vod_quality;
    }
}
