package com.harreke.easyapp.requests.universalimageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class UniversalImageExecutor implements ImageLoadingListener, IRequestExecutor {
    private IRequestCallback<ImageView> mCallback = null;
    private boolean mExecuting = false;
    private ImageView mImage;

    public UniversalImageExecutor(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        mImage = image;
        mCallback = callback;
        ImageLoader.getInstance().displayImage(imageUrl, image, this);
    }

    @Override
    public void cancel() {
        ImageLoader.getInstance().cancelDisplayTask(mImage);
        mExecuting = false;
        mCallback = null;
        mImage = null;
    }

    @Override
    public boolean isExecuting() {
        return mExecuting;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        mExecuting = false;
        mImage = null;
        mCallback = null;
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mExecuting = false;
        if (mCallback != null) {
            mCallback.onSuccess(mImage);
            mImage = null;
            mCallback = null;
        }
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        mExecuting = false;
        if (mCallback != null) {
            mCallback.onFailure();
            mImage = null;
            mCallback = null;
        }
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        mExecuting = true;
    }
}