package com.harreke.easyappframework.helpers;

import android.widget.ImageView;

import com.harreke.easyappframework.configs.ImageExecutorConfig;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载助手
 */
public class ImageLoaderHelper {
    public static IRequestExecutor loadImage(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        return loadImage(image, imageUrl, 0, 0, callback);
    }

    /**
     * 从网络获取图片
     *
     * @param image
     *         指定的图片视图
     * @param imageUrl
     *         图片url
     * @param callback
     *         图片加载回调
     */
    public static IRequestExecutor loadImage(ImageView image, String imageUrl, int loadingImageId, int retryImageId, IRequestCallback<ImageView> callback) {
        return ImageExecutorConfig.create(image, imageUrl, loadingImageId, retryImageId, callback);
    }

    public static IRequestExecutor loadImage(ImageView image, String imageUrl) {
        return loadImage(image, imageUrl, 0, 0, null);
    }
}