package com.harreke.easyapp.widgets.transitions;

import android.graphics.PointF;
import android.widget.ImageView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/30
 */
public class ImageViewInfo {
    private String mImageUrl;
    private PointF mPosition;
    private PointF mSize;

    public ImageViewInfo(ImageView imageView) {
        int[] location = new int[2];

        imageView.getLocationOnScreen(location);
        mPosition = new PointF(location[0], location[1]);
        mSize = new PointF(imageView.getWidth(), imageView.getHeight());
        mImageUrl = (String) imageView.getTag(R.id.imageUrl);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public PointF getSize() {
        return mSize;
    }
}
