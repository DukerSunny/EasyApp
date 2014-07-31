package com.harreke.easyappframework.pulltorefreshes.abslistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.harreke.easyappframework.adapters.abslistview.AbsListAdapter;
import com.harreke.easyappframework.frameworks.bases.IFramework;
import com.harreke.easyappframework.holders.abslistview.IAbsListHolder;
import com.harreke.easyappframework.loaders.ILoader;
import com.harreke.easyappframework.pulltorefreshes.PullToRefresh;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 下拉刷新AbsListView
 */
public abstract class ListPullToRefresh<ITEM, HOLDER extends IAbsListHolder<ITEM>, LOADER extends ILoader<ITEM>> extends PullToRefresh<ListView, ITEM, LOADER>
        implements IList<ITEM, HOLDER>, AdapterView.OnItemClickListener {
    private AdapterAbs mAdapter;
    private int mHeaderCount = 0;

    /**
     * 初始化PullToRefresh助手
     *
     * @param framework
     *         Activity或Fragment框架
     * @param listId
     *         列表的视图Id
     * @param pullToRefreshId
     *         PullToRefresh的视图Id，如果不需要下拉刷新特性则传入0
     */
    public ListPullToRefresh(IFramework framework, int listId, int pullToRefreshId) {
        super(framework, listId, pullToRefreshId);

        mAdapter = new AdapterAbs();
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    public final void addFooterView(View view) {
        mList.addFooterView(view, null, false);
    }

    public final void addHeaderView(View view) {
        mList.addHeaderView(view, null, false);
        mHeaderCount++;
    }

    public final void addItem(int key, ITEM value) {
        mAdapter.addItem(key, value);
    }

    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
        refresh();
    }

    public final ITEM getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getCount();
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
    public void sort(Comparator<ITEM> comparator) {
        mAdapter.sort(comparator);
    }

    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= mHeaderCount;
        onListItemClick(position, mAdapter.getItem(position));
    }

    public final void setSelection(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            mList.setSelection(position);
        }
    }

    private class AdapterAbs extends AbsListAdapter<ITEM> {
        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HOLDER holder;
            ITEM item = getItem(position);

            if (convertView != null) {
                holder = (HOLDER) convertView.getTag();
            } else {
                convertView = createView(position, item);
                holder = createHolder(position, convertView);
                convertView.setTag(holder);
            }
            holder.setItem(item);

            return convertView;
        }
    }
}