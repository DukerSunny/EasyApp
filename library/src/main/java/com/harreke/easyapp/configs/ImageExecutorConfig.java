package com.harreke.easyapp.configs;

import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.volley.VolleyImageExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的全局配置
 */
public class ImageExecutorConfig {
    public static int imageCacheSize = 20 * 1024 * 1024;
    public static int loadingImageId = R.drawable.anim_progress_radiant;
    public static int retryImageId = R.drawable.progress_retry;

    /**
     * 创建一个Http图片请求执行器
     *
     * 默认使用Volley的Http图片库
     * 若需要使用其他Http图片库，请重写该函数
     *
     * @param image
     *         ImageView
     * @param imageUrl
     *         请求图片Url
     * @param loadingImageId
     *         加载中图片Id
     * @param retryImageId
     *         重试图片Id
     * @param callback
     *         请求回调
     *
     * @return Volley Http图片请求执行器
     */
    public static IRequestExecutor create(ImageView image, String imageUrl, int loadingImageId, int retryImageId, IRequestCallback<ImageView> callback) {
        if (loadingImageId <= 0) {
            loadingImageId = ImageExecutorConfig.loadingImageId;
        }
        if (retryImageId <= 0) {
            retryImageId = ImageExecutorConfig.retryImageId;
        }

        return new VolleyImageExecutor(image, imageUrl, loadingImageId, retryImageId, callback);
    }
}