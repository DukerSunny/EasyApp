package com.harreke.easyapp.widgets.pullablelayout.viewdelegates;

import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/15
 */
public class WebViewDelegate implements IViewDelegate {
    private WeakReference<WebView> mWebViewRef;

    public WebViewDelegate(WebView webView) {
        mWebViewRef = new WeakReference<WebView>(webView);
    }

    @Override
    public boolean isScrollBottom() {
        WebView webView = mWebViewRef.get();

        return webView != null && webView.getScrollY() >= webView.getBottom();
    }

    @Override
    public boolean isScrollTop() {
        WebView webView = mWebViewRef.get();

        return webView != null && webView.getScrollY() <= 0;
    }
}