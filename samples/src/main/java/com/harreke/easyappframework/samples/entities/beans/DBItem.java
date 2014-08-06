package com.harreke.easyappframework.samples.entities.beans;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/04
 */
public class DBItem {
    private String mDesc;
    private int mId;
    private ArrayList<SubItem> mSubList;
    private String mTitle;

    public String getDesc() {
        return mDesc;
    }

    public int getId() {
        return mId;
    }

    public ArrayList<SubItem> getSubList() {
        return mSubList;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setSubList(ArrayList<SubItem> subList) {
        mSubList = subList;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static class SubItem {
        private String mSubDesc;
        private String mSubTitle;

        public String getSubDesc() {
            return mSubDesc;
        }

        public String getSubTitle() {
            return mSubTitle;
        }

        public void setSubDesc(String subDesc) {
            mSubDesc = subDesc;
        }

        public void setSubTitle(String subTitle) {
            mSubTitle = subTitle;
        }
    }
}