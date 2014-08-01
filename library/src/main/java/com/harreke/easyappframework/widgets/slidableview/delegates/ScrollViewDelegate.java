package com.harreke.easyappframework.widgets.slidableview.delegates;

import android.widget.ScrollView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public class ScrollViewDelegate implements IDelegate {
    private ScrollView mScrollView = null;

    public ScrollViewDelegate(ScrollView scrollView) {
        mScrollView = scrollView;
    }

    @Override
    public boolean isTouchTop() {
        return mScrollView == null || mScrollView.getChildCount() == 0 || mScrollView.getScrollY() <= 0;
    }
}