package com.harreke.easyappframework.imageloaders.volley;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;
import com.harreke.easyappframework.requests.volley.VolleyImageListener;
import com.harreke.easyappframework.requests.volley.VolleyInstance;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Volley图片加载的执行器
 */
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