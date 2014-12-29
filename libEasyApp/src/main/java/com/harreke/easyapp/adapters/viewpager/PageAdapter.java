package com.harreke.easyapp.adapters.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * PagerAdapter的封装
 *
 * @param <ITEM>
 *         页面的数据类型
 * @param <VIEW>
 *         页面的类型
 */
public abstract class PageAdapter<ITEM, VIEW extends View> extends PagerAdapter {
    private List<ITEM> mItemList = new ArrayList<ITEM>();

    public final void add(ITEM item) {
        mItemList.add(item);
    }

    public final void add(List<ITEM> itemList) {
        mItemList.addAll(itemList);
    }

    public final void clear() {
        mItemList.clear();
    }

    protected abstract VIEW createView(ViewGroup container, int position, ITEM item);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
    public View instantiateItem(ViewGroup container, int position) {
        VIEW view = createView(container, position, mItemList.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}