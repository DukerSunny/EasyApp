package com.harreke.easyappframework.adapters.abslistview;

import android.widget.BaseExpandableListAdapter;

import com.harreke.easyappframework.beans.ExpandableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ExpandableListView的Adapter
 */
public abstract class ExpandableListAdapter<GROUP, CHILD> extends BaseExpandableListAdapter {
    private HashSet<Integer> keySet = new HashSet<Integer>();
    private ArrayList<ExpandableItem<GROUP, CHILD>> valueList = new ArrayList<ExpandableItem<GROUP, CHILD>>();

    /**
     * 添加项目
     *
     * @param key
     *         项目标识
     * @param value
     *         项目对象
     */
    public final void addItem(Integer key, ExpandableItem<GROUP, CHILD> value) {
        if (!keySet.contains(key)) {
            keySet.add(key);
            valueList.add(value);
        }
    }

    /**
     * 清空Adapter
     */
    public final void clear() {
        keySet.clear();
        valueList.clear();
    }

    @Override
    public int getGroupCount() {
        return valueList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < valueList.size()) {
            return valueList.get(groupPosition).getChildList().size();
        } else {
            return 0;
        }
    }

    @Override
    public GROUP getGroup(int groupPosition) {
        if (groupPosition > 0 && groupPosition < valueList.size()) {
            return valueList.get(groupPosition).getGroup();
        } else {
            return null;
        }
    }

    @Override
    public CHILD getChild(int groupPosition, int childPosition) {
        ExpandableItem<GROUP, CHILD> value;

        if (groupPosition > 0 && groupPosition < valueList.size()) {
            value = valueList.get(groupPosition);
            if (value != null && childPosition < value.getChildList().size()) {
                return value.getChild(childPosition);
            }
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 设置指定父项目的指定子项目是否可以被选中
     *
     * @param groupPosition
     *         父项目位置
     * @param childPosition
     *         子项目位置
     *
     * @return 是否可以被选中
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public final ExpandableItem<GROUP, CHILD> getItem(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < valueList.size()) {
            return valueList.get(groupPosition);
        } else {
            return null;
        }
    }

    public final void refresh() {
        notifyDataSetChanged();
    }

    /**
     * 排序项目
     *
     * @param comparator
     *         比较器
     */
    public final void sort(Comparator<ExpandableItem<GROUP, CHILD>> comparator) {
        Collections.sort(valueList, comparator);
    }
}