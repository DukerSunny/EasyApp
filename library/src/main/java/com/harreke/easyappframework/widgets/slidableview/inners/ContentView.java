package com.harreke.easyappframework.widgets.slidableview.inners;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.harreke.easyappframework.widgets.slidableview.delegates.AbsListViewDelegate;
import com.harreke.easyappframework.widgets.slidableview.delegates.IDelegate;
import com.harreke.easyappframework.widgets.slidableview.delegates.ScrollViewDelegate;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/30
 */
public class ContentView extends FrameLayout {
    private IDelegate mDelegate = null;

    public ContentView(Context context) {
        super(context);
    }

    public final boolean isSlidableTouchTop() {
        return mDelegate == null || mDelegate.isTouchTop();
    }

    public final void setSlidableView(int slidableViewId) {
        View slidableView;

        if (slidableViewId > 0) {
            slidableView = findViewById(slidableViewId);
            if (slidableView != null) {
                if (slidableView instanceof AbsListView) {
                    mDelegate = new AbsListViewDelegate((AbsListView) slidableView);
                } else if (slidableView instanceof ScrollView) {
                    mDelegate = new ScrollViewDelegate((ScrollView) slidableView);
                }
            }
        }
    }
}