package com.harreke.easyappframework.beans;

import java.util.ArrayList;

/**
 * 用于ExListView的复合条目
 *
 * 每个ExItem都有一个父条目和一个子条目列表
 *
 * @param <GROUP>
 *         父条目类型
 * @param <CHILD>
 *         子条目类型
 */
public class ExItem<GROUP, CHILD> {
    private ArrayList<CHILD> mChildList;
    private GROUP mGroup;

    public ExItem() {
        mGroup = null;
        mChildList = new ArrayList<CHILD>();
    }

    /**
     * 增加一个子条目
     */
    public void addChild(CHILD child) {
        mChildList.add(child);
    }

    /**
     * 获得指定位置的子条目
     *
     * @param position
     *         指定位置
     *
     * @return 子条目
     */
    public CHILD getChild(int position) {
        if (position >= 0 && position < mChildList.size()) {
            return mChildList.get(position);
        } else {
            return null;
        }
    }

    /**
     * 获得子条目列表
     *
     * @return 子条目列表
     */
    public ArrayList<CHILD> getChildList() {
        return mChildList;
    }

    /**
     * 获得父条目
     *
     * @return 父条目
     */
    public GROUP getGroup() {
        return mGroup;
    }

    /**
     * 设置子条目列表
     *
     * @param childList
     *         子条目列表
     */
    public void setChildList(ArrayList<CHILD> childList) {
        mChildList = childList;
    }

    /**
     * 设置父条目
     *
     * @param group
     *         父条目
     */
    public void setGroup(GROUP group) {
        mGroup = group;
    }
}