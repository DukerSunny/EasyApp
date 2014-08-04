package com.harreke.easyappframework.requests.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Vollety实例
 */
public class VolleyInstance {
    public static VolleyInstance mInstance = null;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;

    public static VolleyInstance getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyInstance();
            mInstance.mQueue = Volley.newRequestQueue(context);
            mInstance.mImageLoader = new ImageLoader(mInstance.mQueue, new VolleyImageCache());
        }

        return mInstance;
    }

    public final Request add(Request<String> request) {
        return mQueue.add(request);
    }

    public final ImageLoader.ImageContainer loadImage(String imageUrl, ImageLoader.ImageListener imageListener) {
        return mImageLoader.get(imageUrl, imageListener);
    }

    public final void stop() {
        mQueue.stop();
    }
}