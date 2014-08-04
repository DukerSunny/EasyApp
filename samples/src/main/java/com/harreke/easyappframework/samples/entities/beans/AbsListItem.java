package com.harreke.easyappframework.samples.entities.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class AbsListItem {
    private String mDesc = null;
    private int mId = 0;
    private String mImage = null;
    private String mTitle = null;

    public String getDesc() {
        return mDesc;
    }

    public int getId() {
        return mId;
    }

    public String getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}