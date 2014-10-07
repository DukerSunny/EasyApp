package com.harreke.easyapp.configs;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.universalimageloader.UniversalImageExecutor;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的全局配置
 */
public class ImageExecutorConfig {
    public static int imageCacheSize = 48 * 1024 * 1024;
    public static int loadingImageId = R.drawable.progress_loading;
    public static int retryImageId = R.drawable.progress_retry;

    public static void config(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).diskCache(
                new UnlimitedDiscCache(new File(ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_CACHES)))
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder().showImageForEmptyUri(retryImageId).showImageOnFail(retryImageId)
                                .showImageOnLoading(loadingImageId).cacheOnDisk(true).build()
                ).build();
        ImageLoader.getInstance().init(configuration);
    }

    //    /**
    //     * 创建一个Http图片请求执行器
    //     *
    //     * 默认使用Volley的Http图片库
    //     * 若需要使用其他Http图片库，请重写该函数
    //     *
    //     * @param image
    //     *         ImageView
    //     * @param imageUrl
    //     *         请求图片Url
    //     * @param loadingImageId
    //     *         加载中图片Id
    //     * @param retryImageId
    //     *         重试图片Id
    //     * @param callback
    //     *         请求回调
    //     *
    //     * @return Volley Http图片请求执行器
    //     */
    public static IRequestExecutor create(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        //        if (loadingImageId <= 0) {
        //            loadingImageId = ImageExecutorConfig.loadingImageId;
        //        }
        //        if (retryImageId <= 0) {
        //            retryImageId = ImageExecutorConfig.retryImageId;
        //        }

        return new UniversalImageExecutor(image, imageUrl, callback);
    }

    public static IRequestExecutor create(String imageUrl, IRequestCallback<Bitmap> callback) {
        return new UniversalImageExecutor(imageUrl, callback);
    }

    public static String getImageCachePathByUrl(String imageUrl) {
        if (imageUrl != null) {
            return ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_CACHES + "/" + imageUrl.hashCode();
        } else {
            return null;
        }
    }
}