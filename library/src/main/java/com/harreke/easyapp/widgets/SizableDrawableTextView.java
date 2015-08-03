package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * Drawable自适应大小的TextView
 * <p/>
 * 设置的Drawable将等比缩放至适合字体高度的大小
 */
public class SizableDrawableTextView extends TextView {
    private int mDrawableHeight;
    private int mDrawableWidth;

    public SizableDrawableTextView(Context context) {
        this(context, null);
    }

    public SizableDrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.sizableDrawableTextViewStyle);
    }

    public SizableDrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        Drawable[] drawables = getCompoundDrawables();
        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.SizableDrawableTextView, defStyle, 0);
        mDrawableWidth = (int) style.getDimension(R.styleable.SizableDrawableTextView_drawableWidth, 0);
        mDrawableHeight = (int) style.getDimension(R.styleable.SizableDrawableTextView_drawableHeight, 0);
        style.recycle();
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    @Override
    public final void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        setDrawable(left);
        setDrawable(top);
        setDrawable(bottom);
        setDrawable(right);
        super.setCompoundDrawables(left, top, right, bottom);
    }

    private void setDrawable(Drawable drawable) {
        if (drawable != null && mDrawableWidth > 0 && mDrawableHeight > 0) {
            drawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
    }
}