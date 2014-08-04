package com.harreke.easyappframework.requests.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.harreke.easyappframework.configs.ImageExecutorConfig;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Volley图片加载的Lru缓存
 */
public class VolleyImageCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    public VolleyImageCache() {
        mCache = new LruCache<String, Bitmap>(ImageExecutorConfig.imageCacheSize) {
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