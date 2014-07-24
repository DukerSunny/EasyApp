package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Drawable自适应大小的TextView
 *
 * 设置的Drawable将等比缩放至适合字体高度的大小
 */
public class AutoFitDrawableTextView extends TextView {
    public AutoFitDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable[] drawables = getCompoundDrawables();

        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    @Override
    public final void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        int width;
        int height;
        float textSize = getTextSize();

        if (left != null) {
            width = left.getIntrinsicWidth();
            height = left.getIntrinsicHeight();
            left.setBounds(0, 0, (int) (width * textSize / height), (int) textSize);
        }
        if (top != null) {
            width = top.getIntrinsicWidth();
            height = top.getIntrinsicHeight();
            top.setBounds(0, 0, (int) (width * textSize / height), (int) textSize);
        }
        if (right != null) {
            width = right.getIntrinsicWidth();
            height = right.getIntrinsicHeight();
            right.setBounds(0, 0, (int) (width * textSize / height), (int) textSize);
        }
        if (bottom != null) {
            width = bottom.getIntrinsicWidth();
            height = bottom.getIntrinsicHeight();
            bottom.setBounds(0, 0, (int) (width * textSize / height), (int) textSize);
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }
}