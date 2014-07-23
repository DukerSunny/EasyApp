package com.harreke.utils.requests.volley;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.harreke.utils.requests.IRequestCallback;

public class VolleyImageListener implements ImageLoader.ImageListener {
    private boolean executing;
    private IRequestCallback<ImageView> mCallback = null;
    private ImageView mImage;
    private int mRetryImageId;

    public VolleyImageListener(ImageView image, int retryImageId, IRequestCallback<ImageView> callback) {
        if (image == null) {
            throw new IllegalArgumentException("Image must not be null!");
        }
        executing = true;
        mRetryImageId = retryImageId;
        mCallback = callback;
    }

    public final boolean isExecuting() {
        return executing;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        executing = false;
        if (mRetryImageId > 0) {
            mImage.setImageResource(mRetryImageId);
        }
        if (mCallback != null) {
            mCallback.onFailure();
        }
        mCallback = null;
        mImage = null;
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        executing = false;
        if (response.getBitmap() != null) {
            mImage.setImageBitmap(response.getBitmap());
        } else if (mRetryImageId > 0) {
            mImage.setImageResource(mRetryImageId);
        }
        if (mCallback != null) {
            mCallback.onSuccess(mImage);
        }
        mCallback = null;
        mImage = null;
    }
}