package com.harreke.easyapp.requests.asynchttpclient;

import android.content.Context;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/27
 */
public class AsyncHttpClientExecutor extends TextHttpResponseHandler implements IRequestExecutor {
    private IRequestCallback<String> mCallback = null;
    private boolean mExecuting = false;
    private RequestHandle mHandle = null;
    private String mRequestUrl;

    public AsyncHttpClientExecutor(Context context, RequestBuilder builder, IRequestCallback<String> callback) {
        AsyncHttpClient client;
        HashMap<String, String> map;
        Iterator<String> iterator;
        String key;

        mCallback = callback;
        client = new AsyncHttpClient();
        client.setTimeout(15000);

        map = builder.getHeader();
        iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            client.addHeader(key, map.get(key));
        }
        mRequestUrl = builder.getUrl();
        switch (builder.getMethod()) {
            case GET:
                mHandle = client.get(context, mRequestUrl, this);
                break;
            case POST:
                mHandle = client.post(context, mRequestUrl, new RequestParams(builder.getBody()), this);
        }
    }

    @Override
    public void cancel() {
        if (mHandle != null) {
            mHandle.cancel(true);
        }
        mExecuting = false;
        mCallback = null;
    }

    @Override
    public boolean isExecuting() {
        return mExecuting;
    }

    @Override
    public void onCancel() {
        mExecuting = false;
        mHandle = null;
        mCallback = null;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        mExecuting = false;
        if (mCallback != null) {
            mCallback.onFailure(mRequestUrl);
            mHandle = null;
            mCallback = null;
            mRequestUrl = null;
        }
    }

    @Override
    public void onStart() {
        mExecuting = true;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        mExecuting = false;
        if (mCallback != null) {
            mCallback.onSuccess(mRequestUrl, responseString);
            mHandle = null;
            mCallback = null;
            mRequestUrl = null;
        }
    }
}
