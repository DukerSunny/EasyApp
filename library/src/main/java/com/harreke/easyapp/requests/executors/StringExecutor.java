package com.harreke.easyapp.requests.executors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/29
 */
public abstract class StringExecutor extends Executor<String> {
    private WeakReference<UploadPair> mUploadPairRef = null;

    protected UploadPair getUploadPair() {
        return mUploadPairRef != null ? mUploadPairRef.get() : null;
    }

    @Override
    public StringExecutor progressCallback(@NonNull IProgressCallback progressCallback) {
        return (StringExecutor) super.progressCallback(progressCallback);
    }

    @Override
    public StringExecutor request(@NonNull String requestUrl) {
        Log.e(null, "load " + requestUrl);
        return (StringExecutor) super.request(requestUrl);
    }

    @Override
    public StringExecutor request(@NonNull RequestBuilder requestBuilder) {
        requestBuilder.print();
        return (StringExecutor) super.request(requestBuilder);
    }

    @Override
    protected StringExecutor requestCallback(@Nullable IRequestCallback<String> requestCallback) {
        return (StringExecutor) super.requestCallback(requestCallback);
    }

    @Override
    protected void reset() {
        super.reset();
        if (mUploadPairRef != null) {
            mUploadPairRef.clear();
            mUploadPairRef = null;
        }
    }

    public StringExecutor upload(@NonNull String key, @NonNull String type, @NonNull File file) {
        mUploadPairRef = new WeakReference<UploadPair>(new UploadPair(key, type, file));

        return this;
    }
}