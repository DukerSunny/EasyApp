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
    private IRequestCallback<Bitmap> mBitmapCallback = null;
    private boolean mExecuting = false;
    private ImageView mImage;
    private IRequestCallback<ImageView> mImageCallback = null;

    public UniversalImageExecutor(ImageView image, String imageUrl, IRequestCallback<ImageView> imageCallback) {
        mImage = image;
        mImageCallback = imageCallback;
        ImageLoader.getInstance().displayImage(imageUrl, image, this);
    }

    public UniversalImageExecutor(String imageUrl, IRequestCallback<Bitmap> bitmapCallback) {
        mBitmapCallback = bitmapCallback;
        ImageLoader.getInstance().loadImage(imageUrl, this);
    }

    @Override
    public void cancel() {
        ImageLoader.getInstance().cancelDisplayTask(mImage);
        mExecuting = false;
        mImageCallback = null;
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
        mImageCallback = null;
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mExecuting = false;
        if (mImageCallback != null) {
            mImageCallback.onSuccess(imageUri, mImage);
            mImage = null;
            mImageCallback = null;
        } else if (mBitmapCallback != null) {
            mBitmapCallback.onSuccess(imageUri, loadedImage);
            mBitmapCallback = null;
        }
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        mExecuting = false;
        if (mImageCallback != null) {
            mImageCallback.onFailure(imageUri);
            mImage = null;
            mImageCallback = null;
        } else if (mBitmapCallback != null) {
            mBitmapCallback.onFailure(imageUri);
            mBitmapCallback = null;
        }
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        mExecuting = true;
    }
}