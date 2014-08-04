package com.harreke.easyappframework.requests.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Volley图片加载的执行器
 */
public class VolleyImageExecutor implements IRequestExecutor {
    private ImageLoader.ImageContainer mContainer = null;
    private VolleyImageListener mListener;

    public VolleyImageExecutor(ImageView image, String imageUrl, int loadingImageId, int retryImageId, IRequestCallback<ImageView> callback) {
        Drawable drawable;

        if (image != null) {
            if (imageUrl != null && !imageUrl.startsWith("http:")) {
                imageUrl = image.getContext().getCacheDir() + imageUrl;
                drawable = Drawable.createFromPath(imageUrl);
                if (drawable != null) {
                    image.setImageDrawable(drawable);
                } else {
                    image.setImageResource(retryImageId);
                }
            } else {
                mListener = new VolleyImageListener(image, loadingImageId, retryImageId, callback);
                mContainer = VolleyInstance.getInstance(image.getContext()).loadImage(imageUrl, mListener);
            }
        }
    }

    public VolleyImageExecutor(Context context, String imageUrl, IRequestCallback<Bitmap> callback) {
        mListener = new VolleyImageListener(callback);
        mContainer = VolleyInstance.getInstance(context).loadImage(imageUrl, mListener);
    }

    @Override
    public void cancel() {
        if (mContainer != null) {
            mContainer.cancelRequest();
        }
    }

    @Override
    public boolean isExecuting() {
        return mListener != null && mListener.isExecuting();
    }
}