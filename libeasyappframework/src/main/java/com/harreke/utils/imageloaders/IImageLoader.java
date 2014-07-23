package com.harreke.utils.imageloaders;

import android.widget.ImageView;

import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.IRequestExecutor;

public interface IImageLoader {
    public IRequestExecutor load(ImageView image, String imageUrl, IRequestCallback<ImageView> callback);
}