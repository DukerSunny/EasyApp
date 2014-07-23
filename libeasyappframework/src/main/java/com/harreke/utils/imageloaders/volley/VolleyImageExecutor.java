package com.harreke.utils.imageloaders.volley;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.IRequestExecutor;
import com.harreke.utils.requests.volley.VolleyImageListener;
import com.harreke.utils.requests.volley.VolleyInstance;

public class VolleyImageExecutor implements IRequestExecutor {
    private ImageLoader.ImageContainer mContainer = null;
    private VolleyImageListener mListener;

    public VolleyImageExecutor(ImageView image, String imageUrl, int retryImageId, IRequestCallback<ImageView> callback) {
        mListener = new VolleyImageListener(image, retryImageId, callback);
        mContainer = VolleyInstance.getInstance(image.getContext()).loadImage(imageUrl, mListener);
    }

    @Override
    public void cancel() {
        if (mContainer != null) {
            mContainer.cancelRequest();
        }
    }

    @Override
    public boolean isExecuting() {
        return mListener.isExecuting();
    }
}