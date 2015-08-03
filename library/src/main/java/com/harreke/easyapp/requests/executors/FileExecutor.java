package com.harreke.easyapp.requests.executors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/29
 */
public abstract class FileExecutor extends Executor<File> {
    private WeakReference<File> mFileRef = null;

    public FileExecutor download(@NonNull File file) {
        mFileRef = new WeakReference<File>(file);

        return this;
    }

    protected File getFile() {
        return mFileRef != null ? mFileRef.get() : null;
    }

    @Override
    public FileExecutor progressCallback(@NonNull IProgressCallback progressCallback) {
        return (FileExecutor) super.progressCallback(progressCallback);
    }

    @Override
    public FileExecutor request(@NonNull RequestBuilder requestBuilder) {
        return (FileExecutor) super.request(requestBuilder);
    }

    @Override
    public FileExecutor request(@NonNull String requestUrl) {
        return (FileExecutor) super.request(requestUrl);
    }

    @Override
    protected FileExecutor requestCallback(@Nullable IRequestCallback<File> requestCallback) {
        return (FileExecutor) super.requestCallback(requestCallback);
    }

    protected void clearFile() {
        if (mFileRef != null) {
            mFileRef.clear();
            mFileRef = null;
        }
    }

    @Override
    protected void reset() {
        super.reset();
        clearFile();
    }
}