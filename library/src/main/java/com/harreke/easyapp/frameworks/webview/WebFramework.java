package com.harreke.easyapp.frameworks.webview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.helpers.LoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.requests.executors.StringExecutor;
import com.harreke.easyapp.widgets.pullablelayout.OnPullableListener;
import com.harreke.easyapp.widgets.pullablelayout.PullableLayout;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/16
 */
public class WebFramework implements OnPullableListener {
    private String mBaseUrl = null;
    private EmptyHelper mEmptyHelper = null;
    private IFramework mFramework = null;
    private View.OnClickListener mOnEmptyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRequestAction();
        }
    };
    private PullableLayout mPullableLayout = null;
    private StringExecutor mRequestExecutor = null;
    private WebSettings mWebSettings;
    private WebView mWebView;
    private IRequestCallback<String> mRequestCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(IRequestExecutor executor, String requestUrl) {
            if (mPullableLayout != null) {
                mPullableLayout.setRefreshComplete();
            }
            mRequestExecutor = null;
            if (mEmptyHelper != null) {
                mEmptyHelper.showEmptyFailureIdle();
            }
        }

        @Override
        public void onSuccess(IRequestExecutor executor, String requestUrl, String result) {
            if (mPullableLayout != null) {
                mPullableLayout.setRefreshComplete();
            }
            mRequestExecutor = null;
            if (!TextUtils.isEmpty(result)) {
                if (mEmptyHelper != null) {
                    mEmptyHelper.hide();
                }
                load(mBaseUrl, result);
            } else {
                if (mEmptyHelper != null) {
                    mEmptyHelper.showEmptyFailureIdle();
                }
            }
        }
    };

    public WebFramework(IFramework framework) {
        View empty_root;

        mFramework = framework;
        mWebView = (WebView) mFramework.findViewById(getWebViewId());
        mWebSettings = mWebView.getSettings();
        setAppCacheEnabled(true);
        setCanRefresh(true);
        mPullableLayout = (PullableLayout) mFramework.findViewById(getPullableLayoutId());
        if (mPullableLayout != null) {
            mPullableLayout.setOnPullableListener(this);
        }
        empty_root = mFramework.findViewById(R.id.empty_root);
        if (empty_root != null) {
            mEmptyHelper = new EmptyHelper(mFramework);
            mEmptyHelper.showEmptyIdle();
            mEmptyHelper.setOnClickListener(mOnEmptyClickListener);
        }
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        mWebView.addJavascriptInterface(obj, interfaceName);
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    private void cancel() {
        if (mRequestExecutor != null && mRequestExecutor.isExecuting()) {
            mRequestExecutor.cancel();
            mRequestExecutor = null;
        }
    }

    public final void from(@Nullable String baseUrl, @NonNull RequestBuilder requestBuilder) {
        cancel();
        if (mEmptyHelper != null) {
            mEmptyHelper.showLoading();
        }
        mBaseUrl = baseUrl;
        mRequestExecutor = LoaderHelper.makeStringExecutor();
        mRequestExecutor.request(requestBuilder).execute(mFramework, mRequestCallback);
    }

    public final void from(@NonNull RequestBuilder requestBuilder) {
        from(null, requestBuilder);
    }

    public final void from(@Nullable String baseUrl, @NonNull String requestUrl) {
        cancel();
        if (mEmptyHelper != null) {
            mEmptyHelper.showLoading();
        }
        mBaseUrl = baseUrl;
        mRequestExecutor = LoaderHelper.makeStringExecutor();
        mRequestExecutor.request(requestUrl).execute(mFramework, mRequestCallback);
    }

    public final void from(@NonNull String requestUrl) {
        from(null, requestUrl);
    }

    protected int getPullableLayoutId() {
        return R.id.web_pullable;
    }

    protected int getWebViewId() {
        return R.id.web_solid;
    }

    public void goBack() {
        mWebView.goBack();
    }

    public final void load(String baseUrl, String html) {
        if (mPullableLayout != null) {
            mPullableLayout.setRefreshComplete();
        }
        if (mEmptyHelper != null) {
            mEmptyHelper.hide();
        }
        mWebView.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null);
    }

    public final void load(String requestUrl) {
        if (mPullableLayout != null) {
            mPullableLayout.setRefreshComplete();
        }
        if (mEmptyHelper != null) {
            mEmptyHelper.hide();
        }
        mWebView.loadUrl(requestUrl);
    }

    @Override
    public void onPullToLoad() {
    }

    @Override
    public void onPullToRefresh() {
        onRequestAction();
    }

    protected void onRequestAction() {
        mFramework.startAction();
    }

    public void setAppCacheEnabled(boolean appCacheEnabled) {
        mWebSettings.setAppCacheEnabled(appCacheEnabled);
    }

    public void setBuiltInZoomControls(boolean enabled) {
        mWebSettings.setBuiltInZoomControls(enabled);
    }

    public void setCanLoad(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setLoadable(enabled);
        }
    }

    public void setCanRefresh(boolean enabled) {
        if (mPullableLayout != null) {
            mPullableLayout.setRefreshable(enabled);
        }
    }

    public void setDisplayZoomControls(boolean enabled) {
        mWebSettings.setDisplayZoomControls(enabled);
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