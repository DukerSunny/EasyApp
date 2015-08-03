package com.harreke.easyapp.requests.androidasynchttp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.requests.RequestMethod;
import com.harreke.easyapp.requests.executors.BinaryExecutor;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 启圣 on 2015/7/22.
 */
public class AndroidAsyncHttpBinaryExecutor extends BinaryExecutor {
    private WeakReference<RequestHandle> mRequestHandleRef = null;
    private BinaryHttpResponseHandler mResponseHandler = new BinaryHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
            IRequestCallback<byte[]> callback = getRequestCallback();

            clearRequestHandleRef();
            if (callback != null) {
                callback.onFailure(getExecutor(), getRequestUrl());
            }
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            IProgressCallback callback = getProgressCallback();

            if (callback != null) {
                callback.onProgress(bytesWritten, totalSize);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
            IRequestCallback<byte[]> callback = getRequestCallback();

            clearRequestHandleRef();
            if (callback != null) {
                callback.onSuccess(getExecutor(), getRequestUrl(), binaryData);
            }
        }
    };

    @Override
    public void cancel() {
        RequestHandle requestHandle = getRequestHandle();

        if (requestHandle != null) {
            requestHandle.cancel(true);
        }
        clearRequestHandleRef();
    }

    private void clearRequestHandleRef() {
        if (mRequestHandleRef != null) {
            mRequestHandleRef.clear();
            mRequestHandleRef = null;
        }
    }

    @Override
    public void execute(@NonNull IFramework framework, @Nullable IRequestCallback<byte[]> requestCallback) {
        AndroidAsyncHttpClient asyncHttpClient = AndroidAsyncHttpClient.getInstance();
        Context context = framework.getContext();
        String requestUrl = getRequestUrl();
        Header[] headers = getHeaders();
        RequestHandle requestHandle;

        if (requestUrl != null) {
            cancel();
            requestCallback(requestCallback);
            if (getRequestBuilder().getMethod() == RequestMethod.GET) {
                requestHandle = asyncHttpClient.get(context, requestUrl, headers, mResponseHandler);
            } else {
                requestHandle = asyncHttpClient.post(context, requestUrl, headers, getParams(), mResponseHandler);
            }
            mRequestHandleRef = new WeakReference<>(requestHandle);
        }
    }

    private Header[] getHeaders() {
        RequestBuilder requestBuilder = getRequestBuilder();
        Map<String, String> headerMap = requestBuilder.getHeader();
        Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
        Map.Entry<String, String> headerEntry;
        Header[] headers = new Header[headerMap.size()];
        int i = 0;

        while (iterator.hasNext()) {
            headerEntry = iterator.next();
            headers[i] = new BasicHeader(headerEntry.getKey(), headerEntry.getValue());
            i++;
        }

        return headers;
    }

    private RequestParams getParams() {
        RequestBuilder requestBuilder = getRequestBuilder();
        RequestParams requestParams = null;

        if (requestBuilder.getMethod() == RequestMethod.POST) {
            requestParams = new RequestParams(requestBuilder.getBody());
        }

        return requestParams;
    }

    private RequestHandle getRequestHandle() {
        return mRequestHandleRef != null ? mRequestHandleRef.get() : null;
    }

    @Override
    public boolean isExecuting() {
        RequestHandle requestHandle = getRequestHandle();

        return requestHandle != null && !requestHandle.isFinished();
    }
}
