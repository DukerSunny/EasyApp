package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.widget.ScrollView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class ScrollViewDelegate implements IViewDelegate {
    private ScrollView mContent;

    public ScrollViewDelegate(ScrollView content) {
        mContent = content;
    }

    @Override
    public boolean isScrollBottom() {
        return mContent.getScrollY() >= mContent.getBottom();
    }

    @Override
    public boolean isScrollTop() {
        return mContent.getScrollY() <= 0;
    }
}
