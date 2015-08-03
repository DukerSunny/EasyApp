package com.harreke.easyapp.requests.executors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.requests.IExecutor;
import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/30
 */
public abstract class Executor<RESULT> implements IRequestExecutor, IExecutor<RESULT> {
    private WeakReference<IProgressCallback> mProgressCallbackRef = null;
    private WeakReference<RequestBuilder> mRequestBuilderRef = null;
    private WeakReference<IRequestCallback<RESULT>> mRequestCallbackRef = null;

    protected IRequestExecutor getExecutor() {
        return this;
    }

    protected IProgressCallback getProgressCallback() {
        return mProgressCallbackRef != null ? mProgressCallbackRef.get() : null;
    }

    protected RequestBuilder getRequestBuilder() {
        return mRequestBuilderRef != null ? mRequestBuilderRef.get() : null;
    }

    protected IRequestCallback<RESULT> getRequestCallback() {
        return mRequestCallbackRef != null ? mRequestCallbackRef.get() : null;
    }

    protected String getRequestUrl() {
        RequestBuilder requestBuilder = getRequestBuilder();

        return requestBuilder != null ? requestBuilder.getUrl() : null;
    }

    public Executor<RESULT> progressCallback(@NonNull IProgressCallback progressCallback) {
        mProgressCallbackRef = new WeakReference<>(progressCallback);

        return this;
    }

    public Executor<RESULT> request(@NonNull String requestUrl) {
        return request(new RequestBuilder(requestUrl));
    }

    public Executor<RESULT> request(@NonNull RequestBuilder requestBuilder) {
        mRequestBuilderRef = new WeakReference<>(requestBuilder);

        return this;
    }

    protected Executor<RESULT> requestCallback(@Nullable IRequestCallback<RESULT> requestCallback) {
        mRequestCallbackRef = new WeakReference<>(requestCallback);

        return this;
    }

    protected void reset() {
        if (mProgressCallbackRef != null) {
            mProgressCallbackRef.clear();
            mProgressCallbackRef = null;
        }
        if (mRequestCallbackRef != null) {
            mRequestCallbackRef.clear();
            mRequestCallbackRef = null;
        }
        if (mRequestBuilderRef != null) {
            mRequestBuilderRef.clear();
            mRequestBuilderRef = null;
        }
    }
}