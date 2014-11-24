package com.harreke.easyapp.adapters.abslistview;

import android.widget.BaseExpandableListAdapter;

import com.harreke.easyapp.beans.ExItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ExListView的Adapter
 */
public abstract class ExListAdapter<GROUP, CHILD> extends BaseExpandableListAdapter {
    private List<ExItem<GROUP, CHILD>> mItemList = new ArrayList<ExItem<GROUP, CHILD>>();
    private HashSet<Integer> mKeySet = new HashSet<Integer>();

    /**
     * 添加条目
     *
     * @param key
     *         条目标识
     * @param value
     *         条目对象
     */
    public final boolean addItem(Integer key, ExItem<GROUP, CHILD> value) {
        if (!mKeySet.contains(key)) {
            mKeySet.add(key);
            mItemList.add(value);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 清空Adapter
     */
    public final void clear() {
        mKeySet.clear();
        mItemList.clear();
    }

    @Override
    public CHILD getChild(int groupPosition, int childPosition) {
        ExItem<GROUP, CHILD> value;

        if (groupPosition >= 0 && groupPosition < mItemList.size()) {
            value = mItemList.get(groupPosition);
            if (value != null && childPosition >= 0 && childPosition < value.getChildList().size()) {
                return value.getChild(childPosition);
            }
        }

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    /**
     * 获得指定父条目包含的子条目列表
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 包含的子条目列表
     */
    public ArrayList<CHILD> getChildList(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mItemList.size()) {
            return mItemList.get(groupPosition).getChildList();
        } else {
            return null;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mItemList.size()) {
            return mItemList.get(groupPosition).getChildList().size();
        } else {
            return 0;
        }
    }

    @Override
    public GROUP getGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mItemList.size()) {
            return mItemList.get(groupPosition).getGroup();
        } else {
            return null;
        }
    }

    @Override
    public int getGroupCount() {
        return mItemList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public final ExItem<GROUP, CHILD> getItem(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mItemList.size()) {
            return mItemList.get(groupPosition);
        } else {
            return null;
        }
    }

    public List<ExItem<GROUP, CHILD>> getItemList() {
        return mItemList;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 设置指定父条目的指定子条目是否可以被选中
     *
     * @param groupPosition
     *         父条目位置
     * @param childPosition
     *         子条目位置
     *
     * @return 是否可以被选中
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public final void refresh() {
        notifyDataSetChanged();
    }

    /**
     * 排序条目
     *
     * @param comparator
     *         比较器
     */
    public final void sort(Comparator<ExItem<GROUP, CHILD>> comparator) {
        Collections.sort(mItemList, comparator);
    }
}