package com.harreke.easyapp.frameworks.recyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/01
 */
public abstract class RecyclerAdapter<ITEM> extends RecyclerView.Adapter<RecyclerHolder<ITEM>> {
    private List<ITEM> mItemList = new ArrayList<>();

    //    /**
    //     * 添加一个条目，并通知视图刷新
    //     *
    //     * @param item
    //     *         条目
    //     */
    //    public final void addItem(int position, ITEM item) {
    //        if (position >= 0 && position < mItemList.size()) {
    //            removeItem(item);
    //            mItemList.add(position, item);
    //            notifyItemChanged(position);
    //        }
    //        int hashCode;
    //        int index;
    //
    //        if (position >= 0 && position <= mHashList.size()) {
    //            hashCode = item.hashCode();
    //            index = mHashList.indexOf(hashCode);
    //            if (index > -1) {
    //                mHashList.remove(index);
    //                mItemMap.remove(hashCode);
    //                notifyItemRemoved(index);
    //                if (position > index) {
    //                    position--;
    //                }
    //            }
    //            mHashList.add(position, hashCode);
    //            mItemMap.put(hashCode, item);
    //            notifyItemInserted(position);
    //        }
    //    }

    /**
     * 添加一个条目，并通知视图刷新
     *
     * @param item 条目
     */
    public final void addItem(ITEM item) {
        int index = mItemList.indexOf(item);
        int position;

        if (index > -1) {
            mItemList.remove(item);
            notifyItemRemoved(index);
        }
        position = mItemList.size();
        mItemList.add(item);
        notifyItemInserted(position);
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param itemList 条目列表
     */
    public final void addItem(List<ITEM> itemList) {
        int position;
        int index;
        int i;

        for (i = 0; i < itemList.size(); i++) {
            index = findItem(itemList.get(i));
            if (index > -1) {
                mItemList.remove(index);
                notifyItemRemoved(index);
            }
        }
        position = mItemList.size();
        //        for (i = 0; i < itemList.size(); i ++) {
        //            mItemList.add(itemList.get(i));
        //            notifyItemInserted(position + i);
        //        }
        mItemList.addAll(itemList);
        notifyItemRangeInserted(position, itemList.size());
    }

    /**
     * 添加一组条目，并通知视图刷新
     *
     * @param items 条目数组
     */
    public final void addItem(ITEM[] items) {
        int position;
        int index;
        int i;

        for (i = 0; i < items.length; i++) {
            index = findItem(items[i]);
            if (index > -1) {
                mItemList.remove(index);
                notifyItemRemoved(index);
            }
        }
        position = mItemList.size();
        for (i = 0; i < items.length; i++) {
            mItemList.add(items[i]);
        }
        notifyItemRangeInserted(position, items.length);
    }

    /**
     * 清空所有条目，并通知视图刷新
     */
    public final void clear() {
        int count = mItemList.size();

        mItemList.clear();
        notifyItemRangeRemoved(0, count);
    }

    /**
     * 查询条目位置
     *
     * @param hashCode 条目哈希码
     * @return 条目位置
     */
    public final int findItem(int hashCode) {
        int i;

        for (i = 0; i < mItemList.size(); i++) {
            if (mItemList.get(i).hashCode() == hashCode) {
                return i;
            }
        }

        return -1;
    }


    /**
     * 查询条目位置
     *
     * @param item 条目
     * @return 条目位置
     */
    public final int findItem(ITEM item) {
        return mItemList.indexOf(item);
    }

    /**
     * 获得指定位置的条目
     *
     * @param position 指定位置
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
    public List<ITEM> getItemList() {
        return mItemList;
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param item 条目
     */
    public final void removeItem(ITEM item) {
        int index = mItemList.indexOf(item);

        if (index > -1) {
            removeItem(index);
        }
    }

    /**
     * 移除一个条目，并通知视图刷新
     *
     * @param position 条目位置
     */
    public final void removeItem(int position) {
        if (position >= 0 && position < mItemList.size()) {
            mItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 移除指定条目，并通知视图刷新
     *
     * @param start 条目起始位置
     * @param end   条目终止位置
     */
    public final void removeItem(int start, int end) {
        int i;

        if (start >= 0 && start <= end && end < mItemList.size()) {
            for (i = start; i <= end; i++) {
                mItemList.remove(i);
            }
            notifyItemRangeRemoved(start, end - start + 1);
        }
    }

    /**
     * 替换一个条目，并通知视图刷新
     *
     * @param item 条目
     */
    public final void replaceItem(int position, ITEM item) {
        if (position >= 0 && position < mItemList.size()) {
            removeItem(item);
            mItemList.set(position, item);
            notifyItemChanged(position);
        }
    }

    /**
     * 排序条目列表
     *
     * @param comparator 排序器
     */
    public final void sort(Comparator<ITEM> comparator) {
        if (comparator != null) {
            Collections.sort(mItemList, comparator);
            notifyItemRangeChanged(0, mItemList.size());
        }
    }
}