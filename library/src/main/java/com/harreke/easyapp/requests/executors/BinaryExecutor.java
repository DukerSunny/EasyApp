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
public abstract class BinaryExecutor extends Executor<byte[]> {
    @Override
    public BinaryExecutor progressCallback(@NonNull IProgressCallback progressCallback) {
        return (BinaryExecutor) super.progressCallback(progressCallback);
    }

    @Override
    public BinaryExecutor request(@NonNull String requestUrl) {
        Log.e(null, "load " + requestUrl);
        return (BinaryExecutor) super.request(requestUrl);
    }

    @Override
    public BinaryExecutor request(@NonNull RequestBuilder requestBuilder) {
        requestBuilder.print();
        return (BinaryExecutor) super.request(requestBuilder);
    }

    @Override
    protected BinaryExecutor requestCallback(@Nullable IRequestCallback<byte[]> requestCallback) {
        return (BinaryExecutor) super.requestCallback(requestCallback);
    }
}