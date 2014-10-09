package com.harreke.easyapp.frameworks.list.abslistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.harreke.easyapp.adapters.abslistview.ExListAdapter;
import com.harreke.easyapp.beans.ExItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.list.ListFramework;
import com.harreke.easyapp.holders.abslistview.IExListHolder;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * ExpandableListView框架
 *
 * @param <GROUP>
 *         父条目类型
 * @param <GROUPHOLDER>
 *         父条目容器类型
 * @param <CHILD>
 *         子条目类型
 * @param <CHILDHOLDER>
 *         子条目容器类型
 */
public abstract class ExListFramework<GROUP, GROUPHOLDER extends IExListHolder.Group<GROUP>, CHILD, CHILDHOLDER extends IExListHolder.Child<CHILD>>
        extends ListFramework<ExItem<GROUP, CHILD>>
        implements IExList<GROUP, GROUPHOLDER, CHILD, CHILDHOLDER>, IExItemClickListener<GROUP, CHILD>,
        ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {
    private Adapter mAdapter;
    private ExpandableListView mExpandableListView;

    public ExListFramework(IFramework framework, int listId) {
        super(framework, listId);
    }

    /**
     * 添加一个条目
     *
     * @param itemId
     *         条目Id，大于等于0，用于检测是否有重复条目
     *         若为-1，则不检测重复条目
     * @param item
     *         条目对象
     *
     * @return 如果添加成功，返回true，否则返回false
     */
    @Override
    public final boolean addItem(int itemId, ExItem<GROUP, CHILD> item) {
        return mAdapter.addItem(itemId, item);
    }

    /**
     * 设置数据适配器
     */
    @Override
    public void bindAdapter() {
        mAdapter = new Adapter();
        mExpandableListView.setAdapter(mAdapter);
    }

    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
        refresh();
    }

    /**
     * 折叠所有父条目
     */
    public void collapseAllGroups() {
        int i;

        for (i = 0; i < getItemCount(); i++) {
            mExpandableListView.collapseGroup(i);
        }
    }

    /**
     * 折叠指定父条目
     *
     * @param groupPosition
     *         父条目位置
     */
    public void collapseGroup(int groupPosition) {
        if (groupPosition >= 0 && groupPosition < getItemCount()) {
            mExpandableListView.collapseGroup(groupPosition);
        }
    }

    /**
     * 展开所有条目
     */
    public void expandAllGroups() {
        int i;

        for (i = 0; i < getItemCount(); i++) {
            mExpandableListView.expandGroup(i);
        }
    }

    /**
     * 展开指定父条目
     *
     * @param groupPosition
     *         父条目位置
     * @param unique
     *         是否为唯一展开
     *
     *         如果为true，则其他已经展开的父条目会被折叠，限制只有指定的父条目处于展开状态
     *         如果为false，则不进行限制
     */
    public final void expandGroup(int groupPosition, boolean unique) {
        int i;

        if (groupPosition >= 0 && groupPosition < getItemCount()) {
            if (unique) {
                for (i = 0; i < getItemCount(); i++) {
                    mExpandableListView.collapseGroup(i);
                }
            }
            mExpandableListView.expandGroup(groupPosition);
        }
    }

    /**
     * 获得指定子条目
     *
     * @param groupPosition
     *         父条目位置
     * @param childPosition
     *         子条目位置
     *
     * @return 子条目对象
     */
    public CHILD getChild(int groupPosition, int childPosition) {
        return mAdapter.getChild(groupPosition, childPosition);
    }

    /**
     * 获得指定父条目包含的子条目总数
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 包含的子条目总数
     */
    public final int getChildCount(int groupPosition) {
        return mAdapter.getChildrenCount(groupPosition);
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
        return mAdapter.getChildList(groupPosition);
    }

    /**
     * 获得指定父条目
     *
     * @param grouPosition
     *         父条目位置
     *
     * @return 父条目对象
     */
    public GROUP getGroup(int grouPosition) {
        return mAdapter.getGroup(grouPosition);
    }

    /**
     * 获得指定复合条目
     *
     * 复合条目由一个父条目和多个子条目复合而成
     *
     * @param groupPosition
     *         父条目位置
     *
     * @return 父条目与其包含子条目的复合条目
     */
    @Override
    public final ExItem<GROUP, CHILD> getItem(int groupPosition) {
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
        return groupPosition >= 0 && groupPosition < getItemCount() && mExpandableListView.isGroupExpanded(groupPosition);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        onChildItemClick(groupPosition, childPosition, getChild(groupPosition, childPosition));

        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (isGroupClickable(groupPosition)) {
            onGroupItemClick(groupPosition, getGroup(groupPosition));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public void setListView(View listView) {
        mExpandableListView = (ExpandableListView) listView;
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnChildClickListener(this);
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
        if (groupPosition >= 0 && groupPosition < getItemCount() && childPosition >= 0 &&
                childPosition < getChildCount(groupPosition)) {
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
        if (groupPosition >= 0 && groupPosition < getItemCount()) {
            mExpandableListView.setSelectedGroup(groupPosition);
        }
    }

    @Override
    public void sort(Comparator<ExItem<GROUP, CHILD>> comparator) {
        mAdapter.sort(comparator);
    }

    private class Adapter extends ExListAdapter<GROUP, CHILD> {
        @SuppressWarnings("unchecked")
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                ViewGroup parent) {
            CHILDHOLDER holder;
            CHILD child = getChild(groupPosition, childPosition);

            if (convertView != null) {
                holder = (CHILDHOLDER) convertView.getTag();
            } else {
                convertView = createChildView();
                holder = createChildHolder(convertView);
                convertView.setTag(holder);
            }
            holder.setItem(groupPosition, childPosition, child);

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
                convertView = createGroupView();
                holder = createGroupHolder(convertView);
                convertView.setTag(holder);
            }
            holder.setItem(groupPosition, group);

            return convertView;
        }
    }
}