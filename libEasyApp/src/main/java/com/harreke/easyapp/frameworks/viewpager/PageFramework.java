package com.harreke.easyapp.frameworks.viewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public abstract class PageFramework<ITEM, VIEW extends View> implements IPager<ITEM, VIEW>, IPageActionListener, ViewPager.OnPageChangeListener {
    private PageAdapter mAdapter;
    private ViewPager mPageView;

    public PageFramework(IFramework framework, int pageId) {
        View pageView;

        if (framework == null) {
            throw new IllegalArgumentException("Framework must not be null!");
        } else {
            pageView = framework.findViewById(pageId);
            if (pageView == null || !(pageView instanceof ViewPager)) {
                throw new IllegalArgumentException("Invalid listId!");
            }
            mAdapter = new PageAdapter();
            mPageView = (ViewPager) pageView;
            mPageView.setAdapter(mAdapter);
            mPageView.setOnPageChangeListener(this);
        }
    }

    public final void add(ITEM item) {
        mAdapter.add(item);
    }

    public void clear() {
        mAdapter.clear();
    }

    public final int getCount() {
        return mAdapter.getCount();
    }

    public final int getCurrentPage() {
        return mPageView.getCurrentItem();
    }

    public final ITEM getItem(int position) {
        return mAdapter.getItem(position);
    }

    public final VIEW getView(int position) {
        return mAdapter.getView(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        onPageChange(position);
    }

    public final void refresh() {
        mAdapter.refresh();
    }

    public final void setCurrentPage(int position, boolean smoothScroll) {
        mPageView.setCurrentItem(position, smoothScroll);
    }

    /**
     * PagerAdapter的封装
     */
    private class PageAdapter extends PagerAdapter {
        private ArrayList<ITEM> mItemList = new ArrayList<ITEM>();
        private SparseArray<VIEW> mViewList = new SparseArray<VIEW>();

        public final void add(ITEM item) {
            mItemList.add(item);
        }

        public final void clear() {
            mItemList.clear();
            mViewList.clear();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            VIEW view = mViewList.get(position);

            container.removeView(view);
            mViewList.remove(position);
            onPageDestroy(position);
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

        public final VIEW getView(int position) {
            return mViewList.get(position);
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            VIEW view = createPage(position, mItemList.get(position));

            container.addView(view);
            mViewList.put(position, view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public final void refresh() {
            notifyDataSetChanged();
        }
    }
}