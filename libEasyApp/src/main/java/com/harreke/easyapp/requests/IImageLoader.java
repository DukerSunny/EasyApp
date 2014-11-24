package com.harreke.easyapp.requests;

import android.widget.ImageView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的接口
 */
public interface IImageLoader {
    public IRequestExecutor load(ImageView image, String imageUrl, IRequestCallback<ImageView> callback);
}