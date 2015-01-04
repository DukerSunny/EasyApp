package com.harreke.easyapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载助手
 */
public class ImageLoaderHelper {
    public static IRequestExecutor loadBitmap(Context context, String imageUrl, IRequestCallback<Bitmap> callback) {
        return ImageExecutorConfig.create(context, imageUrl, callback);
    }

    public static IRequestExecutor loadImage(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId,
            IRequestCallback<ImageView> callback) {
        return ImageExecutorConfig.create(imageView, imageUrl, loadingImageId, retryImageId, callback);
    }

    public static IRequestExecutor loadImage(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId) {
        return loadImage(imageView, imageUrl, loadingImageId, retryImageId, null);
    }
}