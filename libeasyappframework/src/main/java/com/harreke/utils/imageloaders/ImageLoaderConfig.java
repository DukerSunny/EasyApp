package com.harreke.utils.imageloaders;

import com.harreke.utils.R;

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