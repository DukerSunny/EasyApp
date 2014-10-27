package com.harreke.easyapp.adapters.swipelayout;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public abstract class AbsListSwipeAdapter<ITEM> extends BaseSwipeAdapter {
    private boolean mEnabled = true;
    private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();
    private HashSet<Integer> mKeySet = new HashSet<Integer>();

    /**
     * 添加一个项目
     *
     * @param itemId
     *         项目Id，大于等于0，用于检测是否有重复项目
     *         若为-1，则不检测重复项目
     * @param item
     *         项目对象
     *
     * @return 是否添加成功
     */
    public final boolean addItem(int itemId, ITEM item) {
        /**
         通过项目Id判断是否重复添加
         */
        if (itemId < 0) {
            mItemList.add(item);

            return true;
        } else if (!mKeySet.contains(itemId)) {
            mKeySet.add(itemId);
            mItemList.add(item);

            return true;
        } else {
            return false;
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

    @Override
    public boolean isEnabled(int position) {
        return mEnabled;
    }

    public final void refresh() {
        notifyDataSetChanged();
    }

    /**
     * 移除一个项目
     *
     * @param itemId
     *         项目Id，大于等于0，用于检测是否有重复项目
     *         若为-1，则不检测重复项目
     * @param item
     *         项目对象
     *
     * @return 是否移除成功
     */
    public final boolean removeItem(int itemId, ITEM item) {
        if (!mKeySet.contains(itemId)) {
            return false;
        } else {
            mKeySet.remove(itemId);
            mItemList.remove(item);

            return true;
        }
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    /**
     * 排序项目
     *
     * @param comparator
     *         比较器
     */
    public final void sort(Comparator<ITEM> comparator) {
        if (comparator != null) {
            Collections.sort(mItemList, comparator);
        }
    }
}
