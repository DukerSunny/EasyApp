package com.harreke.easyapp.requests.executors;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/29
 */
public abstract class ImageExecutor extends Executor<Bitmap> {
    private WeakReference<ImageView> mImageViewRef = null;
    private int mLoadingImageId = 0;
    private int mRetryImageId = 0;

    protected ImageView getImageView() {
        return mImageViewRef != null ? mImageViewRef.get() : null;
    }

    protected int getLoadingImageId() {
        return mLoadingImageId;
    }

    protected int getRetryImageId() {
        return mRetryImageId;
    }

    public ImageExecutor imageView(@NonNull ImageView imageView) {
        mImageViewRef = new WeakReference<>(imageView);

        return this;
    }

    public ImageExecutor loadingImageId(int loadingImageId) {
        mLoadingImageId = loadingImageId;

        return this;
    }

    @Override
    public ImageExecutor progressCallback(@NonNull IProgressCallback progressCallback) {
        return (ImageExecutor) super.progressCallback(progressCallback);
    }

    @Override
    public ImageExecutor request(@NonNull String requestUrl) {
//        Log.e(null, "load " + requestUrl);
        return (ImageExecutor) super.request(requestUrl);
    }

    @Override
    public ImageExecutor request(@NonNull RequestBuilder requestBuilder) {
//        requestBuilder.print();
        return (ImageExecutor) super.request(requestBuilder);
    }

    @Override
    protected ImageExecutor requestCallback(@Nullable IRequestCallback<Bitmap> requestCallback) {
        return (ImageExecutor) super.requestCallback(requestCallback);
    }

    @Override
    protected void reset() {
        super.reset();
        if (mImageViewRef != null) {
            mImageViewRef.clear();
            mImageViewRef = null;
        }
        mLoadingImageId = 0;
        mRetryImageId = 0;
    }

    public ImageExecutor retryImageId(int retryImageId) {
        mRetryImageId = retryImageId;

        return this;
    }

    protected void startFadeIn(ImageView imageView, Bitmap result) {
        startFadeIn(imageView, new BitmapDrawable(imageView.getResources(), result));
    }

    protected void startFadeIn(ImageView imageView, int drawableId) {
        if (drawableId > 0) {
            startFadeIn(imageView, imageView.getResources().getDrawable(drawableId));
        }
    }

    protected void startFadeIn(ImageView imageView, Drawable drawable) {
        Drawable imageDrawable;
        TransitionDrawable transitionDrawable;

        if (imageView instanceof CircleImageView) {
            imageView.setImageDrawable(drawable);
        } else {
            imageDrawable = imageView.getDrawable();
            if (imageDrawable == null) {
                imageDrawable = new ColorDrawable(Color.TRANSPARENT);
            }
            transitionDrawable = new TransitionDrawable(new Drawable[]{imageDrawable, drawable});
            imageView.setImageDrawable(transitionDrawable);
            transitionDrawable.setCrossFadeEnabled(true);
            transitionDrawable.startTransition(300);
        }
    }
}