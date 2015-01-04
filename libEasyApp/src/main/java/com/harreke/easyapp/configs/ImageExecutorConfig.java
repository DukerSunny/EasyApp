package com.harreke.easyapp.configs;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.ion.IonBitmapExecutor;
import com.harreke.easyapp.requests.ion.IonImageExecutor;

import java.io.File;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的全局配置
 */
public class ImageExecutorConfig {
    public static void config(Context context) {
        //        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).diskCache(
        //                new UnlimitedDiscCache(new File(ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_TEMPS)))
        //                .defaultDisplayImageOptions(new DisplayImageOptions.Builder().cacheOnDisk(true).build()).build();
        //        ImageLoader.getInstance().init(configuration);
    }

    public static IRequestExecutor create(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId,
            IRequestCallback<ImageView> callback) {
        //        return new UniversalImageExecutor(image, imageUrl, loadingImageId, retryImageId, callback);
        return new IonImageExecutor(imageView, imageUrl, loadingImageId, retryImageId, callback);
    }

    public static IRequestExecutor create(Context context, String bitmapUrl, IRequestCallback<Bitmap> callback) {
        //        return new UniversalImageExecutor(imageUrl, callback);
        return new IonBitmapExecutor(context, bitmapUrl, callback);
    }

    public static String getImageCachePathByUrl(String imageUrl) {
        if (imageUrl != null) {
            return ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_TEMPS + "/" + imageUrl.hashCode();
        } else {
            return null;
        }
    }

    public static boolean isImageCacheAvailable(String imageUrl) {
        File file;

        if (imageUrl != null) {
            file = new File(ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_TEMPS + "/" + imageUrl.hashCode());

            return file.exists() && file.isFile();
        } else {
            return false;
        }
    }
}