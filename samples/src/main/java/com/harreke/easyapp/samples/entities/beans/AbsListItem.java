package com.harreke.easyapp.samples.entities.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class AbsListItem {
    private String desc = null;
    private int id = 0;
    private String image = null;
    private String title = null;

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}