package com.harreke.easyapp.adapters.recyclerview;

import android.support.v7.widget.RecyclerView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public abstract class RecyclerAdapter<ITEM> extends RecyclerView.Adapter<RecyclerHolder<ITEM>> {
    private boolean mDiscardRepeat = true;
    private HashSet<Integer> mHashSet = new HashSet<Integer>();
    private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();

    /**
     * 添加一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否添加成功
     *
     * 注：
     * 如果设置丢弃重复条目，则会判断该条目是否重复，若重复，则丢弃该条目并导致添加失败
     */
    public final boolean addItem(int position, ITEM item) {
        if (!mDiscardRepeat || mHashSet.add(item.hashCode()) && position >= 0 && position <= mItemList.size()) {
            mItemList.add(position, item);
            notifyItemInserted(position);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否添加成功
     *
     * 注：
     * 如果设置丢弃重复条目，则会判断该条目是否重复，若重复，则丢弃该条目并导致添加失败
     */
    public final boolean addItem(ITEM item) {
        int position = mItemList.size();

        if (!mDiscardRepeat || mHashSet.add(item.hashCode())) {
            mItemList.add(item);
            notifyItemInserted(position);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param itemList
     *         条目列表
     *
     * @return 成功添加的条目数量
     */
    public final int addItem(List<ITEM> itemList) {
        ITEM item;
        int position = mItemList.size();
        int count;
        int i;

        if (!mDiscardRepeat) {
            count = itemList.size();
            mItemList.addAll(itemList);
            notifyItemRangeInserted(position, count);

            return count;
        } else {
            count = 0;
            for (i = 0; i < itemList.size(); i++) {
                item = itemList.get(i);
                if (mHashSet.add(item.hashCode())) {
                    mItemList.add(item);
                    count++;
                }
            }
            notifyItemRangeInserted(position, count);

            return count;
        }
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param items
     *         条目数组
     *
     * @return 成功添加的条目数量
     */
    public final int addItem(ITEM[] items) {
        ITEM item;
        int position = mItemList.size();
        int count;
        int i;

        if (!mDiscardRepeat) {
            count = items.length;
            for (i = 0; i < count; i++) {
                mItemList.add(items[i]);
            }
            notifyItemRangeInserted(position, count);

            return count;
        } else {
            count = 0;
            for (i = 0; i < items.length; i++) {
                item = items[i];
                if (mHashSet.add(item.hashCode())) {
                    mItemList.add(item);
                    count++;
                }
            }
            notifyItemRangeInserted(position, count);

            return count;
        }
    }

    /**
     * 清空所有条目，并通知视图刷新
     */
    public final void clear() {
        int count = mItemList.size();

        mHashSet.clear();
        mItemList.clear();
        notifyItemRangeRemoved(0, count);
    }

    /**
     * 查询条目位置
     *
     * @param hashCode
     *         条目哈希码
     *
     * @return 条目位置
     */
    public int findItem(int hashCode) {
        ITEM item;
        int i;

        for (i = 0; i < mItemList.size(); i++) {
            item = mItemList.get(i);
            if (item.hashCode() == hashCode) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 获得指定位置的条目
     *
     * @param position
     *         指定位置
     *
     * @return 条目
     */
    public final ITEM getItem(int position) {
        if (position >= 0 && position < mItemList.size()) {
            return mItemList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * 返回所有条目的列表
     *
     * @return 所有条目的列表
     */
    public ArrayList<ITEM> getItemList() {
        return mItemList;
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param item
     *         条目
     *
     * @return 是否移除成功
     */
    public final boolean removeItem(ITEM item) {
        int position = mItemList.indexOf(item);

        if (position > -1 && !mDiscardRepeat || mHashSet.remove(item.hashCode())) {
            mItemList.remove(item);
            notifyItemRemoved(position);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param position
     *         条目位置
     *
     * @return 是否移除成功
     */
    public final boolean removeItem(int position) {
        ITEM item;

        if (position >= 0 && position < mItemList.size()) {
            item = mItemList.get(position);
            if (!mDiscardRepeat || mHashSet.remove(item.hashCode())) {
                mItemList.remove(position);
                notifyItemRemoved(position);

                return true;
            }
        }

        return false;
    }

    /**
     * 移除指定条目，并通知视图刷新
     *
     * @param start
     *         条目起始位置
     * @param end
     *         条目终止位置
     *
     * @return 是否移除成功
     */
    public final boolean removeItem(int start, int end) {
        int count;
        int i;

        if (start >= 0 && start <= end && end < mItemList.size()) {
            if (mDiscardRepeat) {
                for (i = start; i <= end; i++) {
                    mHashSet.remove(mItemList.get(i).hashCode());
                }
            }
            count = end - start + 1;
            while (count > -1) {
                mItemList.remove(getItem(start));
                count--;
            }
            notifyItemRangeRemoved(start, end - start + 1);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置是否丢弃重复的条目
     *
     * 通过条目的hashCode来判断是否重复，默认丢弃
     *
     * @param discardRepeat
     *         是否丢弃重复的条目
     */
    public void setDiscardRepeat(boolean discardRepeat) {
        mDiscardRepeat = discardRepeat;
    }

    /**
     * 排序条目列表
     *
     * @param comparator
     *         排序器
     */
    public final void sort(Comparator<ITEM> comparator) {
        if (comparator != null) {
            Collections.sort(mItemList, comparator);
        }
    }
}
