package com.harreke.easyappframework.frameworks.list.abslistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.harreke.easyappframework.adapters.abslistview.ExpandableListAdapter;
import com.harreke.easyappframework.beans.ExpandableItem;
import com.harreke.easyappframework.frameworks.bases.IFramework;
import com.harreke.easyappframework.frameworks.list.ListFramework;
import com.harreke.easyappframework.holders.abslistview.IExpandableListChildHolder;
import com.harreke.easyappframework.holders.abslistview.IExpandableListGroupHolder;
import com.harreke.easyappframework.loaders.ILoader;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * ExpandableListView框架
 *
 * @param <GROUP>
 *         父条目类型
 * @param <GROUPHOLDER>
 *         父条目Holder类型
 * @param <CHILD>
 *         子条目类型
 * @param <CHILDHOLDER>
 *         子条目Holder类型
 * @param <LOADER>
 *         列表Loader类型
 */
public abstract class ExpandableListFramework<GROUP, GROUPHOLDER extends IExpandableListGroupHolder<GROUP>, CHILD, CHILDHOLDER extends IExpandableListChildHolder<CHILD>, LOADER extends ILoader<ExpandableItem<GROUP, CHILD>>>
        extends ListFramework<ExpandableItem<GROUP, CHILD>, LOADER>
        implements IExpandableList<GROUP, GROUPHOLDER, CHILD, CHILDHOLDER>, IExpandableItemClickListener<GROUP, CHILD>, ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener, AbsListView.OnScrollListener {
    private Adapter mAdapter;
    private ExpandableListView mExpandableListView;
    private int mScrollState = SCROLL_STATE_IDLE;

    public ExpandableListFramework(IFramework framework, int listId, int slidableViewId) {
        super(framework, listId, slidableViewId);
    }

    /**
     * 向Adapter添加一个条目
     *
     * @param itemId
     *         条目Id（唯一）
     * @param item
     *         条目对象
     */
    public final void addItem(int itemId, ExpandableItem<GROUP, CHILD> item) {
        mAdapter.addItem(itemId, item);
    }

    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
        refresh();
    }

    /**
     * 获得指定父条目包含的子条目总数
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 包含的子条目总数
     */
    public final int getChildrenCount(int groupPosition) {
        return mAdapter.getChildrenCount(groupPosition);
    }

    /**
     * 获得指定复合Item
     *
     * 复合Item由一个父条目和多个子条目复合而成
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 父条目与其包含子条目的复合Item
     */
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

    /**
     * 判断指定父条目是否已经展开
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 是否已经展开
     */
    public final boolean isGroupExpanded(int groupPosition) {
        return groupPosition >= 0 && groupPosition < mAdapter.getGroupCount() && mExpandableListView.isGroupExpanded(groupPosition);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        onChildItemClick(groupPosition, childPosition, mAdapter.getChild(groupPosition, childPosition));

        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (isGroupClickable(groupPosition)) {
            onGroupItemClick(groupPosition, mAdapter.getGroup(groupPosition));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isLoadEnabled()) {
            if (mScrollState != SCROLL_STATE_IDLE && !isLastPage() && !isLoading()) {
                if (!isReverseScroll() && firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                    onLoadTrigger();
                } else if (isReverseScroll() && firstVisibleItem == 0) {
                    onLoadTrigger();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
    }

    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public void setListView(View listView) {
        mAdapter = new Adapter();
        mExpandableListView = (ExpandableListView) listView;
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnChildClickListener(this);
        mExpandableListView.setOnScrollListener(this);
    }

    /**
     * 设置选中子条目
     *
     * @param groupPosition
     *         父条目位置
     * @param childPosition
     *         子条目位置
     * @param shouldExpandGroup
     *         是否需要展开父条目
     */
    public final void setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        if (groupPosition >= 0 && groupPosition < mAdapter.getGroupCount() && childPosition >= 0 &&
                childPosition < mAdapter.getChildrenCount(groupPosition)) {
            mExpandableListView.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
        }
    }

    /**
     * 设置选中父条目
     *
     * @param groupPosition
     *         父条目位置
     */
    public final void setSelectedGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < mAdapter.getGroupCount()) {
            mExpandableListView.setSelectedGroup(groupPosition);
        }
    }

    @Override
    public void sort(Comparator<ExpandableItem<GROUP, CHILD>> comparator) {
        mAdapter.sort(comparator);
    }

    private class Adapter extends ExpandableListAdapter<GROUP, CHILD> {
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
    }
}