package com.harreke.easyapp.helpers;

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
    public static IRequestExecutor loadBitmap(String imageUrl, IRequestCallback<Bitmap> callback) {
        return ImageExecutorConfig.create(imageUrl, callback);
    }

    public static IRequestExecutor loadImage(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        return ImageExecutorConfig.create(image, imageUrl, callback);
    }

    public static IRequestExecutor loadImage(ImageView image, String imageUrl) {
        return loadImage(image, imageUrl, null);
    }
}