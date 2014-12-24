package tv.acfun.read.beans;

import android.text.Spanned;

import com.google.gson.annotations.Expose;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class Search {
    private String avatar;
    private int channelId;
    private int comments;
    private String contentId;
    private String description;
    @Expose(serialize = false, deserialize = false)
    private Spanned mSpanned;
    private long releaseDate;
    private int stows;
    private String[] tags;
    private String title;
    private String titleImg;
    private int userId;
    private String username;
    private int views;

    public String getAvatar() {
        return avatar;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getComments() {
        return comments;
    }

    public String getContentId() {
        return contentId;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }

        return description;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public Spanned getSpanned() {
        return mSpanned;
    }

    public int getStows() {
        return stows;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getViews() {
        return views;
    }

    @Override
    public int hashCode() {
        return contentId.hashCode();
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSpanned(Spanned spanned) {
        mSpanned = spanned;
    }

    public void setStows(int stows) {
        this.stows = stows;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
