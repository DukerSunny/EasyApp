package com.harreke.easyapp.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 自适应图像高宽比的ImageView
 */
public class AutoFitImageView extends ImageView {

    public AutoFitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        ViewGroup.LayoutParams layoutParams;
        boolean fitWidth;
        boolean fitHeight;
        int width;
        int height;
        float scale;

        if (drawable != null) {
            scale = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
            layoutParams = getLayoutParams();
            fitWidth = layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT;
            fitHeight = layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT;
            if (fitWidth != fitHeight) {
                if (fitWidth) {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    height = (int) (width / scale);
                } else {
                    height = MeasureSpec.getSize(widthMeasureSpec);
                    width = (int) (height * scale);
                }
                setMeasuredDimension(width, height);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}