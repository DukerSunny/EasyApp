package com.harreke.easyappframework.requests;

import android.widget.ImageView;

import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的接口
 */
public interface IImageLoader {
    public IRequestExecutor load(ImageView image, String imageUrl, IRequestCallback<ImageView> callback);
}