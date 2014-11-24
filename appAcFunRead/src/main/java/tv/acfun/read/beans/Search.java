package tv.acfun.read.beans;

import android.text.Spanned;

import com.google.gson.annotations.Expose;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/04
 */
public class Search {
    private int channelId;
    private int comments;
    private String conver;
    private String description;
    private int id;
    @Expose(serialize = false, deserialize = false)
    private Spanned mSpanned;
    private long releaseDate;
    private String sign;
    private int stows;
    private String title;
    private int userId;
    private String userImg;
    private String username;
    private int views;

    public int getChannelId() {
        return channelId;
    }

    public int getComments() {
        return comments;
    }

    public String getConver() {
        return conver;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }

        return description;
    }

    public int getId() {
        return id;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public String getSign() {
        return sign;
    }

    public Spanned getSpanned() {
        return mSpanned;
    }

    public int getStows() {
        return stows;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public String getUsername() {
        return username;
    }

    public int getViews() {
        return views;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setConver(String conver) {
        this.conver = conver;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setSpanned(Spanned spanned) {
        mSpanned = spanned;
    }

    public void setStows(int stows) {
        this.stows = stows;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
