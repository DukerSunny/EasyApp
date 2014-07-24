package com.harreke.easyappframework.adapters.abslistview;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * AbsListView的Adapter
 */
public abstract class ListAdapter<ITEM> extends BaseAdapter {
    private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();
    private HashSet<Integer> mKeySet = new HashSet<Integer>();

    /**
     * 添加一个项目
     *
     * @param itemId
     *         项目Id（唯一）
     * @param item
     *         项目对象
     */
    public final void addItem(int itemId, ITEM item) {
        /**
         通过项目Id判断是否重复添加
         */
        if (!mKeySet.contains(itemId)) {
            mKeySet.add(itemId);
            mItemList.add(item);
        }
    }

    public final void clear() {
        mItemList.clear();
        mKeySet.clear();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    public final ITEM getItem(int position) {
        if (position >= 0 && position < mItemList.size()) {
            return mItemList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public final void sort(Comparator<ITEM> comparator) {
        Collections.sort(mItemList, comparator);
    }
}