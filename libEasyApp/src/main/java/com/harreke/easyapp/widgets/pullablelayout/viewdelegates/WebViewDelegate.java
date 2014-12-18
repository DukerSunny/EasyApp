package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.webkit.WebView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class WebViewDelegate implements IViewDelegate {
    private WebView mContent;

    public WebViewDelegate(WebView content) {
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
