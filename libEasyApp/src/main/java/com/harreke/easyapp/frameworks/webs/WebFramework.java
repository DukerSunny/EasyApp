package com.harreke.easyapp.frameworks.webs;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.widgets.pullablelayout.PullableLayout;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/16
 */
public class WebFramework implements PullableLayout.OnPullableListener {
    private IFramework mFramework;
    private PullableLayout mPullableLayout = null;
    private WebSettings mWebSettings;
    private WebView mWebView;

    public WebFramework(IFramework framework) {
        mFramework = framework;
        mWebView = (WebView) framework.findViewById(getWebViewId());
        mWebSettings = mWebView.getSettings();
        setAppCacheEnabled(true);
        setSupportZoom(true);
        setBuiltInZoomControls(true);
        setDisplayZoomControls(false);
        setJavaScriptEnabled(false);
        setJavaScriptCanOpenWindowsAutomatically(false);
        setCanLoad(true);
        setCanRefresh(true);
        mPullableLayout = (PullableLayout) framework.findViewById(getPullableLayoutId());
        if (mPullableLayout != null) {
            mPullableLayout.setOnPullableListener(this);
        }
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        mWebView.addJavascriptInterface(obj, interfaceName);
    }

    protected int getPullableLayoutId() {
        return R.id.web_pullable;
    }

    protected int getWebViewId() {
        return R.id.web_solid;
    }

    public void loadHtml(String baseUrl, String html) {
        mWebView.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onPullToLoad() {
    }

    @Override
    public void onPullToRefresh() {
    }

    public void setAppCacheEnabled(boolean appCacheEnabled) {
        mWebSettings.setAppCacheEnabled(appCacheEnabled);
    }

    public void setBuiltInZoomControls(boolean enabled) {
        mWebSettings.setBuiltInZoomControls(enabled);
    }

    public void setCanLoad(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setCanLoad(enabled);
        }
    }

    public void setCanRefresh(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setCanRefresh(enabled);
        }
    }

    @TargetApi(11)
    public void setDisplayZoomControls(boolean enabled) {
        if (Build.VERSION.SDK_INT >= 11) {
            mWebSettings.setDisplayZoomControls(enabled);
        }
    }

    public void setJavaScriptCanOpenWindowsAutomatically(boolean enabled) {
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(enabled);
    }

    public void setJavaScriptEnabled(boolean enabled) {
        mWebSettings.setJavaScriptEnabled(enabled);
    }

    public void setSupportZoom(boolean supportZoom) {
        mWebSettings.setSupportZoom(supportZoom);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        mWebView.setWebChromeClient(webChromeClient);
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        mWebView.setWebViewClient(webViewClient);
    }
}