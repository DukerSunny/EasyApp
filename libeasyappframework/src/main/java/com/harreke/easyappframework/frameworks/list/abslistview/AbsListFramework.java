package com.harreke.easyappframework.frameworks.list.abslistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.harreke.easyappframework.adapters.abslistview.AbsListAdapter;
import com.harreke.easyappframework.frameworks.bases.IFramework;
import com.harreke.easyappframework.frameworks.list.ListFramework;
import com.harreke.easyappframework.holders.abslistview.IAbsListHolder;
import com.harreke.easyappframework.loaders.ILoader;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * AbsListView框架
 *
 * @param <ITEM>
 *         列表条目类型
 * @param <HOLDER>
 *         列表Holder类型
 * @param <LOADER>
 *         列表Loader类型
 */
public abstract class AbsListFramework<ITEM, HOLDER extends IAbsListHolder<ITEM>, LOADER extends ILoader<ITEM>> extends ListFramework<ITEM, LOADER>
        implements IAbsList<ITEM, HOLDER>, IAbsListItemClickListener<ITEM>, AdapterView.OnClickListener, AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener {
    private AbsListView mAbsListView;
    private Adapter mAdapter;
    private int mScrollState = SCROLL_STATE_IDLE;

    public AbsListFramework(IFramework framework, int listId, int slidableViewId) {
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
    public final void addItem(int itemId, ITEM item) {
        mAdapter.addItem(itemId, item);
    }

    /**
     * 获得条目
     *
     * @param position
     *         条目位置
     *
     * @return 条目对象
     */
    public final ITEM getItem(int position) {
        return mAdapter.getItem(position);
    }

    /**
     * 获得条目总数
     *
     * @return 条目总数
     */
    @Override
    public int getItemCount() {
        return mAdapter.getCount();
    }

    /**
     * 判断列表是否为空
     *
     * @return boolean
     */
    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    /**
     * 刷新列表
     */
    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    /**
     * 设置列表视图
     *
     * @param listView
     *         列表视图
     */
    @Override
    public void setListView(View listView) {
        mAdapter = new Adapter();
        mAbsListView = (AbsListView) listView;
        mAbsListView.setAdapter(mAdapter);
        mAbsListView.setOnItemClickListener(this);
        mAbsListView.setOnScrollListener(this);
    }

    /**
     * 排序列表条目
     *
     * @param comparator
     *         比较器
     */
    @Override
    public void sort(Comparator<ITEM> comparator) {
        mAdapter.sort(comparator);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemClick(position, mAdapter.getItem(position));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mLoadEnabled) {
            if (mScrollState != SCROLL_STATE_IDLE && !isLastPage() && !isLoading()) {
                if (!mReverse && firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
                    onLoadStart();
                } else if (mReverse && firstVisibleItem == 0) {
                    onLoadStart();
                }
            }
        }
    }

    /**
     * 设置选中条目
     *
     * @param position
     *         条目位置
     */
    public final void setSelection(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            mAbsListView.setSelection(position);
        }
    }

    private class Adapter extends AbsListAdapter<ITEM> {
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