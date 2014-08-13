package com.harreke.easyapp.requests.volley;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.harreke.easyapp.requests.IRequestCallback;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Volley图片加载监听器
 */
public class VolleyImageListener implements ImageLoader.ImageListener {
    private boolean executing;
    private IRequestCallback<Bitmap> mBitmapCallback = null;
    private ImageView mImage;
    private IRequestCallback<ImageView> mImageCallback = null;
    private int mRetryImageId;

    public VolleyImageListener(IRequestCallback<Bitmap> imageCallback) {
        executing = true;
        mBitmapCallback = imageCallback;
    }

    public VolleyImageListener(ImageView image, int loadingImageId, int retryImageId, IRequestCallback<ImageView> imageCallback) {
        mImage = image;
        mImage.setImageResource(loadingImageId);
        executing = true;
        mRetryImageId = retryImageId;
        mImageCallback = imageCallback;
    }

    public final boolean isExecuting() {
        return executing;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        executing = false;
        mImage.setImageResource(mRetryImageId);
        if (mImageCallback != null) {
            mImageCallback.onFailure();
        }
        mImageCallback = null;
        mImage = null;
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        Bitmap bitmap = response.getBitmap();

        executing = false;
        if (mImage == null && mBitmapCallback != null) {
            if (bitmap != null) {
                mBitmapCallback.onSuccess(bitmap);
            } else {
                mBitmapCallback.onFailure();
            }
            mBitmapCallback = null;
        } else if (mImage != null) {
            if (bitmap != null) {
                mImage.setImageBitmap(bitmap);
            } else if (mRetryImageId > 0) {
                mImage.setImageResource(mRetryImageId);
            }
            if (mImageCallback != null) {
                mImageCallback.onSuccess(mImage);
            }
            mImage = null;
            mImageCallback = null;
        }
    }
}