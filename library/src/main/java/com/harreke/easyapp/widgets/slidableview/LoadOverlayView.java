package com.harreke.easyapp.widgets.slidableview;

import android.content.Context;
import android.util.AttributeSet;

import com.harreke.easyapp.widgets.slidableview.inners.OverlayView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public abstract class LoadOverlayView extends OverlayView implements ILoadOverlay {
    public LoadOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        queryLayout();
    }
}