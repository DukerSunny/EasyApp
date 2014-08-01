package com.harreke.easyappframework.beans;

import java.util.ArrayList;

/**
 用于ExpandableListView的复合项目

 每个CompoundItem都有一个父项目和一个子项目列表

 @param <GROUP>
 父项目
 @param <CHILD>
 子项目
 */
public class ExpandableItem<GROUP, CHILD> {
    private ArrayList<CHILD> mChildList;
    private GROUP mGroup;

    public ExpandableItem() {
        mGroup = null;
        mChildList = new ArrayList<CHILD>();
    }

    /**
     增加一个子项目
     */
    public void addChild(CHILD child) {
        mChildList.add(child);
    }

    /**
     获得指定位置的子项目

     @param position
     指定位置

     @return 子项目
     */
    public CHILD getChild(int position) {
        if (position >= 0 && position < mChildList.size()) {
            return mChildList.get(position);
        } else {
            return null;
        }
    }

    /**
     获得子项目列表

     @return 子项目列表
     */
    public ArrayList<CHILD> getChildList() {
        return mChildList;
    }

    /**
     设置子项目列表

     @param childList
     子项目列表
     */
    public void setChildList(ArrayList<CHILD> childList) {
        mChildList = childList;
    }

    /**
     获得父项目

     @return 父项目
     */
    public GROUP getGroup() {
        return mGroup;
    }

    /**
     设置父项目

     @param group
     父项目
     */
    public void setGroup(GROUP group) {
        mGroup = group;
    }
}