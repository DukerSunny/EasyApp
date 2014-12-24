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
    private IRequestCallback<ImageView> mImageCallback = null;
    private int mLoadingImageId;
    private int mRetryImageId;

    public UniversalImageExecutor(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId,
            IRequestCallback<ImageView> imageCallback) {
        mImageCallback = imageCallback;
        mLoadingImageId = loadingImageId;
        mRetryImageId = retryImageId;
        ImageLoader.getInstance().displayImage(imageUrl, imageView, this);
    }

    public UniversalImageExecutor(String imageUrl, IRequestCallback<Bitmap> bitmapCallback) {
        mBitmapCallback = bitmapCallback;
        ImageLoader.getInstance().loadImage(imageUrl, this);
    }

    @Override
    public void cancel() {
        mExecuting = false;
        mImageCallback = null;
        mBitmapCallback = null;
    }

    @Override
    public boolean isExecuting() {
        return mExecuting;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        mExecuting = false;
        if (view != null) {
            ((ImageView) view).setImageResource(mRetryImageId);
        }
        if (mImageCallback != null) {
            mImageCallback.onSuccess(imageUri, (ImageView) view);
            mImageCallback = null;
        } else if (mBitmapCallback != null) {
            mBitmapCallback.onSuccess(imageUri, null);
            mBitmapCallback = null;
        }
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mExecuting = false;
        if (mImageCallback != null) {
            mImageCallback.onSuccess(imageUri, (ImageView) view);
            mImageCallback = null;
        } else if (mBitmapCallback != null) {
            mBitmapCallback.onSuccess(imageUri, loadedImage);
            mBitmapCallback = null;
        }
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        mExecuting = false;
        if (view != null) {
            ((ImageView) view).setImageResource(mRetryImageId);
        }
        if (mImageCallback != null) {
            mImageCallback.onFailure(imageUri);
            mImageCallback = null;
        } else if (mBitmapCallback != null) {
            mBitmapCallback.onFailure(imageUri);
            mBitmapCallback = null;
        }
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        mExecuting = true;
        if (view != null) {
            ((ImageView) view).setImageResource(mLoadingImageId);
        }
    }
}