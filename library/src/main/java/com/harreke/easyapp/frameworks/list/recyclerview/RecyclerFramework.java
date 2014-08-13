package com.harreke.easyapp.frameworks.list.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.holders.recyclerview.RecyclerHolder;
import com.harreke.easyapp.adapters.recyclerview.RecyclerAdapter;
import com.harreke.easyapp.frameworks.list.ListFramework;

import java.util.Comparator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 *
 * RecyclerView框架
 *
 * @param <ITEM>
 *         条目类型
 * @param <HOLDER>
 *         条目容器类型
 */
@Deprecated
public abstract class RecyclerFramework<ITEM, HOLDER extends RecyclerHolder<ITEM>> extends ListFramework<ITEM>
        implements IRecycler<ITEM, HOLDER>, IRecyclerItemClickListener, RecyclerView.OnScrollListener {
    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mScrollState = RecyclerView.SCROLL_STATE_IDLE;

    public RecyclerFramework(IFramework framework, int listId, int slidableViewId) {
        super(framework, listId, slidableViewId);
    }

    @Override
    public boolean addItem(int itemId, ITEM item) {
        return mAdapter.addItem(itemId, item);
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        super.clear();
        mAdapter.clear();
    }

    @Override
    public ITEM getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.getItemCount() == 0;
    }

    @Override
    public void onScrollStateChanged(int newState) {
        mScrollState = newState;
    }

    @Override
    public void onScrolled(int dx, int dy) {
    }

    /**
     * 刷新列表
     */
    @Override
    public void refresh() {
        mAdapter.refresh();
    }

    @Override
    public void setListView(View listView) {
        mAdapter = new Adapter();
        mRecyclerView = (RecyclerView) listView;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(listView.getContext()));
        mRecyclerView.setOnScrollListener(this);
    }

    @Override
    public void sort(Comparator<ITEM> comparator) {
        mAdapter.sort(comparator);
    }

    private class Adapter extends RecyclerAdapter<ITEM, HOLDER> {
        @Override
        public void onBindViewHolder(HOLDER holder, int position) {
            holder.setOnItemClickListener(RecyclerFramework.this);
            holder.setItem(getItem(position));
        }

        @Override
        public HOLDER onCreateViewHolder(ViewGroup parent, int position) {
            return createHolder(position, createView(position, getItem(position)));
        }
    }
}