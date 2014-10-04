package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 可设置Drawable大小的TextView
 */
public class DrawableTextView extends TextView {
    private float mDrawableHeight;
    private float mDrawableWidth;

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.drawableTextViewStyle);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Drawable[] drawables;

        TypedArray style = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView, defStyle, 0);
        mDrawableWidth = (int) style.getDimension(R.styleable.DrawableTextView_drawableWidth, -1);
        mDrawableHeight = (int) style.getDimension(R.styleable.DrawableTextView_drawableHeight, -1);
        style.recycle();

        drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    private Drawable resize(Drawable drawable) {
        float width;
        float height;

        if (drawable != null) {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
            if (width != 0 && height != 0 && mDrawableWidth != 0 && mDrawableHeight != 0) {
                if (mDrawableWidth < 0 && mDrawableHeight > 0) {
                    width *= mDrawableHeight / height;
                    height = mDrawableHeight;
                } else if (mDrawableWidth > 0 && mDrawableHeight < 0) {
                    height *= mDrawableWidth / width;
                    width = mDrawableWidth;
                } else if (mDrawableWidth > 0 && mDrawableHeight > 0) {
                    width = mDrawableWidth;
                    height = mDrawableHeight;
                }
                drawable.setBounds(0, 0, (int) width, (int) height);
            }
        }

        return drawable;
    }

    @Override
    public final void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(resize(left), resize(top), resize(right), resize(bottom));
    }
}