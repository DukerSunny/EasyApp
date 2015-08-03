package com.harreke.easyapp.requests.universalimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.requests.IProgressCallback;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.executors.ImageExecutor;
import com.harreke.easyapp.utils.FileUtil;
import com.harreke.easyapp.utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by 启圣 on 2015/6/26.
 */
public class UniversalImageLoaderImageExecutor extends ImageExecutor {
    private final static String TAG = "UniversalImageLoader";
    private WeakReference<ImageLoader> mImageLoaderRef = null;
    private ImageLoadingListener mImageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            reset();
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            ImageView imageView;
            IRequestCallback<Bitmap> callback;

            imageView = getImageView();
            callback = getRequestCallback();
            if (loadedImage == null) {
                LogUtil.e(TAG, "Image " + imageUri + " failure!");
                if (imageView != null) {
                    startFadeIn(imageView, getRetryImageId());
                }
                if (callback != null) {
                    callback.onFailure(getExecutor(), imageUri);
                }
            } else {
                if (imageView != null) {
                    startFadeIn(imageView, loadedImage);
                }
                if (callback != null) {
                    callback.onSuccess(getExecutor(), imageUri, loadedImage);
                }
            }
            reset();
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            ImageView imageView;
            IRequestCallback<Bitmap> callback;

            imageView = getImageView();
            callback = getRequestCallback();
            LogUtil.e(TAG, "Image " + imageUri + " failure!");
            if (imageView != null) {
                startFadeIn(imageView, getRetryImageId());
            }
            if (callback != null) {
                callback.onFailure(getExecutor(), imageUri);
            }
            reset();
        }
    };
    private ImageLoadingProgressListener mImageLoadingProgressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {
            IProgressCallback callback = getProgressCallback();

            if (callback != null) {
                callback.onProgress(current, total);
            }
        }
    };

    public static void init(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiskCache(new File(ApplicationFramework.CacheDir + "/" + ApplicationFramework.DIR_TEMPS)))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024)).build();

        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public void cancel() {
        ImageLoader imageLoader = getImageLoader();
        ImageView imageView = getImageView();

        if (imageLoader != null && imageView != null) {
            imageLoader.cancelDisplayTask(imageView);
        }
        reset();
    }

    private void clearImageLoaderRef() {
        if (mImageLoaderRef != null) {
            mImageLoaderRef.clear();
            mImageLoaderRef = null;
        }
    }

    @Override
    public void execute(@NonNull IFramework framework, @Nullable IRequestCallback<Bitmap> requestCallback) {
        ImageView imageView = getImageView();
        Bitmap result;
        ImageLoader imageLoader;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        String requestUrl = getRequestUrl();

        if (requestUrl != null) {
            cancel();
            imageLoader = ImageLoader.getInstance();
            mImageLoaderRef = new WeakReference<>(imageLoader);
            result = getCachedBitmap(imageLoader, requestUrl);
            if (result != null) {
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
                if (requestCallback != null) {
                    requestCallback.onSuccess(this, getRequestUrl(), result);
                }
            } else {
                requestCallback(requestCallback);
                if (imageView != null) {
                    startFadeIn(imageView, getLoadingImageId());
                    imageLoader
                            .displayImage(requestUrl, imageView, options, mImageLoadingListener, mImageLoadingProgressListener);
                } else {
                    imageLoader.loadImage(requestUrl, null, options, mImageLoadingListener, mImageLoadingProgressListener);
                }
            }
        }
    }

    private Bitmap getCachedBitmap(ImageLoader imageLoader, String requestUrl) {
        MemoryCache memoryCache;
        DiskCache diskCache;
        File file;
        Bitmap bitmap;

        memoryCache = imageLoader.getMemoryCache();
        bitmap = memoryCache.get(requestUrl);
        if (bitmap != null) {
            return bitmap;
        } else {
            diskCache = imageLoader.getDiskCache();
            file = DiskCacheUtils.findInCache(requestUrl, diskCache);
            if (file != null) {
                return FileUtil.readBitmap(file);
            } else {
                return null;
            }
        }
    }

    private ImageLoader getImageLoader() {
        return mImageLoaderRef != null ? mImageLoaderRef.get() : null;
    }

    @Override
    public boolean isExecuting() {
        return getImageLoader() != null;
    }

    @Override
    protected void reset() {
        super.reset();
        clearImageLoaderRef();
    }
}