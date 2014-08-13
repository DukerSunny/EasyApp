package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 指定高宽比图像视图
 *
 * 在布局的时候即可指定高宽比（ratio）
 */
public class RatioImageView extends ImageView {
    private float mRatio;

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ratioImageViewStyle);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyle, 0);
        mRatio = style.getFloat(R.styleable.RatioImageView_ratio, 0);
        style.recycle();

        setScaleType(ScaleType.FIT_XY);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams;
        boolean fitWidth;
        boolean fitHeight;
        int width;
        int height;

        if (mRatio > 0) {
            layoutParams = getLayoutParams();
            fitWidth = layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT;
            fitHeight = layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT;
            if (fitWidth != fitHeight) {
                if (fitWidth) {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    height = (int) (width / mRatio);
                } else {
                    height = MeasureSpec.getSize(heightMeasureSpec);
                    width = (int) (height * mRatio);
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