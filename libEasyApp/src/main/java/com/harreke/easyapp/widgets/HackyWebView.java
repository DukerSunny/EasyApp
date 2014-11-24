package com.harreke.easyapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/11
 */
public class HackyWebView extends WebView {
    public final static int SCROLL_STATE_IDEL = 0;
    public final static int SCROLL_STATE_SCROLLING = 1;
    private boolean mPressed = false;
    private OnScrollListener mScrollListener = null;

    public HackyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int dx;
        int dy;

        if (mScrollListener != null) {
            dx = l - oldl;
            dy = t - oldt;
            if (dx != 0 || dy != 0 && mPressed) {
                mScrollListener.onScrollChange(dx, dy);
                mScrollListener.onScrollStateChange(SCROLL_STATE_SCROLLING);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScrollListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPressed = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mPressed = false;
                    mScrollListener.onScrollStateChange(SCROLL_STATE_IDEL);
            }
        }

        return super.onTouchEvent(event);
    }

    public final void setOnScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public interface OnScrollListener {
        public void onScrollChange(int dx, int dy);

        public void onScrollStateChange(int scrollState);
    }
}