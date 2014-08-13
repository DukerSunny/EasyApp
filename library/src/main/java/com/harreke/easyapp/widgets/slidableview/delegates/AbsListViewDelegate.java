package com.harreke.easyapp.widgets.slidableview.delegates;

import android.widget.AbsListView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public class AbsListViewDelegate implements IDelegate {
    private AbsListView mAbsListView = null;

    public AbsListViewDelegate(AbsListView absListView) {
        mAbsListView = absListView;
    }

    @Override
    public boolean isTouchTop() {
        return mAbsListView == null || mAbsListView.getChildCount() == 0 ||
                mAbsListView.getFirstVisiblePosition() == 0 && mAbsListView.getChildAt(0).getTop() >= mAbsListView.getTop();
    }
}