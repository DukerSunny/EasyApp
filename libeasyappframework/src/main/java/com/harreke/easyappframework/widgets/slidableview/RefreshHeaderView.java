package com.harreke.easyappframework.widgets.slidableview;

import android.content.Context;
import android.util.AttributeSet;

import com.harreke.easyappframework.widgets.slidableview.inners.HeaderView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public abstract class RefreshHeaderView extends HeaderView implements IRefreshHeader {
    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        queryLayout();
    }
}