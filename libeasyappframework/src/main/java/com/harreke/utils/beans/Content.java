package com.harreke.utils.beans;

import java.util.ArrayList;

public class Content {
    private int channelId;
    private int comments;
    private int contentId;
    private String cover;
    private String description;
    private long releaseDate;
    private int stows;
    private ArrayList<String> tags;
    private String title;
    private int topLevel;
    private User user;
    private int views;

    public Content() {
        channelId = 0;
        comments = 0;
        contentId = 0;
        cover = "/Misc/thumbnail";
        description = "";
        releaseDate = 0;
        stows = 0;
        tags = new ArrayList<String>();
        title = "";
        topLevel = 0;
        user = new User();
        views = 0;

    }

    public final int getChannelId() {
        return channelId;
    }

    public final int getComments() {
        return comments;
    }

    public final int getContentId() {
        return contentId;
    }

    public final String getCover() {
        if (cover == null) {
            cover = "/Misc/thumbnail";
        }

        return cover;
    }

    public final String getDescription() {
        return description;
    }

    public final long getReleaseDate() {
        return releaseDate;
    }

    public final int getStows() {
        return stows;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public int getTopLevel() {
        return topLevel;
    }

    public void setTopLevel(int topLevel) {
        this.topLevel = topLevel;
    }

    public final User getUser() {
        if (user == null) {
            user = new User();
        }

        return user;
    }

    public final int getViews() {
        return views;
    }
}