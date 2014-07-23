package com.harreke.utils.adapters.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.harreke.utils.holders.recyclerview.RecyclerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 RecyclerView的Adapter

 @param <ITEM>
 项目类型
 @param <HOLDER>
 项目容器类型
 */
public abstract class RecyclerAdapter<ITEM, HOLDER extends RecyclerHolder<ITEM>> extends RecyclerView.Adapter<HOLDER> implements
        IRecyclerAdapter<ITEM, HOLDER> {
    private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();
    private HashSet<Integer> mKeySet = new HashSet<Integer>();

    /**
     添加一个项目

     @param itemId
     项目Id（唯一）
     @param item
     项目对象
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

    public final ITEM getItem(int position) {
        if (position >= 0 && position < mItemList.size()) {
            return mItemList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public HOLDER onCreateViewHolder(ViewGroup parent, int position) {
        return createHolder(position);
    }

    @Override
    public void onBindViewHolder(HOLDER holder, int position) {
        if (position >= 0 && position < mItemList.size()) {
            holder.setItem(mItemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     排序项目

     @param comparator
     比较器
     */
    public final void sort(Comparator<ITEM> comparator) {
        Collections.sort(mItemList, comparator);
    }
}