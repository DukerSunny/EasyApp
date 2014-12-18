package tv.acfun.read.beans;

import android.text.Spanned;

import com.google.gson.annotations.Expose;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class Content {
    private int channelId;
    private int comments;
    private int contentId;
    private String cover;
    private String description;
    @Expose(serialize = false, deserialize = false)
    private Spanned mSpanned;
    private long releaseDate;
    private int stows;
    private String[] tags;
    private String title;
    private User user;
    private int views;

    public int getChannelId() {
        return channelId;
    }

    public int getComments() {
        return comments;
    }

    public int getContentId() {
        return contentId;
    }

    public String getCover() {
        return cover;
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

    public User getUser() {
        return user;
    }

    public int getViews() {
        return views;
    }

    @Override
    public int hashCode() {
        return contentId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
