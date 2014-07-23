package com.harreke.utils.imageloaders.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.harreke.utils.imageloaders.ImageLoaderConfig;

public class VolleyImageCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    public VolleyImageCache() {
        mCache = new LruCache<String, Bitmap>(ImageLoaderConfig.getInstance().imageCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}