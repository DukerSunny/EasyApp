package com.harreke.utils.imageloaders.volley;

import android.widget.ImageView;

import com.harreke.utils.imageloaders.IImageLoader;
import com.harreke.utils.imageloaders.ImageLoaderConfig;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.IRequestExecutor;

public class VolleyImageLoader implements IImageLoader {
    private int mLoadingImageId;
    private int mRetryImageId;

    public VolleyImageLoader(int loadingImageId, int retryImageId) {
        mLoadingImageId = loadingImageId;
        mRetryImageId = retryImageId;
    }

    public VolleyImageLoader() {
        mLoadingImageId = ImageLoaderConfig.getInstance().loadingImageId;
        mRetryImageId = ImageLoaderConfig.getInstance().retryImageId;
    }

    @Override
    public IRequestExecutor load(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        if (image != null && imageUrl != null) {
            if (!imageUrl.startsWith("http:")) {
                imageUrl = "file:" + image.getContext().getCacheDir() + imageUrl;
            }
            if (mLoadingImageId > 0) {
                image.setImageResource(mLoadingImageId);
                return new VolleyImageExecutor(image, imageUrl, mRetryImageId, callback);
            }
        }

        return null;
    }
}