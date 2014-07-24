package com.harreke.easyappframework.pulltorefreshes.abslistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.harreke.easyappframework.adapters.abslistview.ExpandableListAdapter;
import com.harreke.easyappframework.beans.ExpandableItem;
import com.harreke.easyappframework.frameworks.IFramework;
import com.harreke.easyappframework.holders.abslistview.IExpandableListChildHolder;
import com.harreke.easyappframework.holders.abslistview.IExpandableListGroupHolder;
import com.harreke.easyappframework.loaders.ILoader;
import com.harreke.easyappframework.pulltorefreshes.PullToRefresh;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 下拉刷新ExpandableListView
 *
 * @param <GROUP>
 *         父项目类型
 * @param <GROUPHOLDER>
 *         父项目Holder类型
 * @param <CHILD>
 *         子项目类型
 * @param <CHILDHOLDER>
 *         子项目Holder类型
 * @param <LOADER>
 *         ExpandableListView的Loader类型
 */
public abstract class ExpandableListPullToRefresh<GROUP, GROUPHOLDER extends IExpandableListGroupHolder<GROUP>, CHILD, CHILDHOLDER extends IExpandableListChildHolder<CHILD>, LOADER extends ILoader<ExpandableItem<GROUP, CHILD>>>
        extends PullToRefresh<ExpandableListView, ExpandableItem<GROUP, CHILD>, LOADER>
        implements IExpandableList<GROUP, GROUPHOLDER, CHILD, CHILDHOLDER>, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
    private Adapter mAdapter;
    private int mHeaderCount = 0;

    /**
     * 初始化PullToRefresh助手
     *
     * @param framework
     *         Activity或Fragment框架
     * @param listId
     *         列表的视图Id
     * @param pulltorefreshId
     *         PullToRefresh的视图Id，如果不需要下拉刷新特性则传入0
     */
    public ExpandableListPullToRefresh(IFramework framework, int listId, int pulltorefreshId) {
        super(framework, listId, pulltorefreshId);

        mAdapter = new Adapter();
        mList.setAdapter(mAdapter);
        mList.setOnGroupClickListener(this);
        mList.setOnChildClickListener(this);
    }

    public final void addFooterView(View view) {
        mList.addFooterView(view, null, false);
    }

    public final void addHeaderView(View view) {
        mList.addHeaderView(view, null, false);
        mHeaderCount++;
    }

    public final void addItem(int key, ExpandableItem<GROUP, CHILD> value) {
        mAdapter.addItem(key, value);
    }

    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
        refresh();
    }

    public final int getChildrenCount(int groupPosition) {
        return mAdapter.getChildrenCount(groupPosition);
    }

    public final ExpandableItem<GROUP, CHILD> getItem(int groupPosition) {
        return mAdapter.getItem(groupPosition);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getGroupCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public void sort(Comparator<ExpandableItem<GROUP, CHILD>> comparator) {
        mAdapter.sort(comparator);
    }

    public final boolean isGroupExpanded(int groupPosition) {
        return groupPosition >= 0 && groupPosition < mAdapter.getGroupCount() && mList.isGroupExpanded(groupPosition);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        onChildItemClick(groupPosition, childPosition, mAdapter.getChild(groupPosition, childPosition));

        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        groupPosition -= mHeaderCount;
        if (isGroupClickable(groupPosition)) {
            onGroupItemClick(groupPosition, mAdapter.getGroup(groupPosition));

            return true;
        } else {
            return false;
        }
    }

    public final void setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        if (groupPosition >= 0 && groupPosition < mAdapter.getGroupCount() && childPosition >= 0 && childPosition < mAdapter.getChildrenCount(groupPosition)) {
            mList.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
        }
    }

    public final void setSelectedGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mAdapter.getGroupCount()) {
            mList.setSelectedGroup(groupPosition);
        }
    }

    private class Adapter extends ExpandableListAdapter<GROUP, CHILD> {
        @SuppressWarnings("unchecked")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GROUPHOLDER holder;
            GROUP group = getGroup(groupPosition);

            if (convertView != null) {
                holder = (GROUPHOLDER) convertView.getTag();
            } else {
                convertView = createGroupView(groupPosition, group);
                holder = createGroupHolder(groupPosition, convertView);
                convertView.setTag(holder);
            }
            holder.setItem(group);

            return convertView;
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            CHILDHOLDER holder;
            CHILD child = getChild(groupPosition, childPosition);

            if (convertView != null) {
                holder = (CHILDHOLDER) convertView.getTag();
            } else {
                convertView = createChildView(groupPosition, childPosition, child);
                holder = createChildHolder(groupPosition, childPosition, convertView);
                convertView.setTag(holder);
            }
            holder.setItem(child);

            return convertView;
        }
    }
}