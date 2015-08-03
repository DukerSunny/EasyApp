package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.widget.ScrollView;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class ScrollViewDelegate implements IViewDelegate {
    private WeakReference<ScrollView> mScrollViewRef;

    public ScrollViewDelegate(ScrollView scrollView) {
        mScrollViewRef = new WeakReference<ScrollView>(scrollView);
    }

    @Override
    public boolean isScrollBottom() {
        ScrollView scrollView = mScrollViewRef.get();

        return scrollView != null && scrollView.getScrollY() >= scrollView.getBottom();
    }

    @Override
    public boolean isScrollTop() {
        ScrollView scrollView = mScrollViewRef.get();

        return scrollView != null && scrollView.getScrollY() <= 0;
    }
}
