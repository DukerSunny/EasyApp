package com.harreke.easyappframework.imageloaders;

import com.harreke.easyappframework.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 图片加载器的全局配置
 */
public class ImageLoaderConfig {
    private static ImageLoaderConfig mInstance = null;
    public int imageCacheSize = 20 * 1024 * 1024;
    public int loadingImageId = R.drawable.shape_placeholder;
    public int retryImageId = R.drawable.shape_placeholder;

    public static ImageLoaderConfig getInstance() {
        if (mInstance == null) {
            mInstance = new ImageLoaderConfig();
        }

        return mInstance;
    }
}