package com.harreke.easyappframework.imageloaders.volley;

import android.widget.ImageView;

import com.harreke.easyappframework.imageloaders.IImageLoader;
import com.harreke.easyappframework.imageloaders.ImageLoaderConfig;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Volley图片加载器
 */
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