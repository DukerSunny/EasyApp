package com.harreke.utils.adapters.viewpager;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class PageAdapter<ITEM, VIEW extends View> extends PagerAdapter implements IPageAdapter<ITEM, VIEW> {
    protected SparseArray<VIEW> mViewList = new SparseArray<VIEW>();
    private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();

    public final void add(ITEM item) {
        mItemList.add(item);
    }

    public final void clear() {
        mItemList.clear();
        mViewList.clear();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        VIEW view = createPage(position, mItemList.get(position));

        container.addView(view);
        mViewList.put(position, view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        VIEW view = mViewList.get(position);

        container.removeView(view);
        mViewList.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public final ITEM getItem(int position) {
        if (position >= 0 && position < mItemList.size()) {
            return mItemList.get(position);
        } else {
            return null;
        }
    }

    public final VIEW getView(int position) {
        return mViewList.get(position);
    }

    public final void refresh() {
        notifyDataSetChanged();
    }
}