package com.harreke.easyapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.application.ApplicationFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.requests.ion.IonBitmapExecutor;
import com.harreke.easyapp.requests.ion.IonImageExecutor;

import java.io.File;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载助手
 */
public class ImageLoaderHelper {
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

    public static IRequestExecutor loadBitmap(Context context, String bitmapUrl) {
        return loadBitmap(context, bitmapUrl, null);
    }

    public static IRequestExecutor loadBitmap(Context context, String bitmapUrl, IRequestCallback<Bitmap> callback) {
        return new IonBitmapExecutor(context, bitmapUrl, callback);
    }

    public static IRequestExecutor loadImage(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId,
            IRequestCallback<ImageView> callback) {
        return new IonImageExecutor(imageView, imageUrl, loadingImageId, retryImageId, callback);
    }

    public static IRequestExecutor loadImage(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId) {
        return loadImage(imageView, imageUrl, loadingImageId, retryImageId, null);
    }

    public static IRequestExecutor loadImage(ImageView imageView, RequestBuilder requestBuilder,
            IRequestCallback<ImageView> callback) {
        return new IonImageExecutor(imageView, requestBuilder, callback);
    }

    public static IRequestExecutor loadImage(ImageView imageView, RequestBuilder requestBuilder) {
        return loadImage(imageView, requestBuilder, null);
    }
}