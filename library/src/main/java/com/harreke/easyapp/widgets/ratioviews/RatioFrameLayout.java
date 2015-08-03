package com.harreke.easyapp.widgets.ratioviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * 指定高宽比图像视图
 * <p/>
 * 在布局的时候即可指定高宽比（ratio）
 */
public class RatioFrameLayout extends FrameLayout {
    private float mRatio;

    public RatioFrameLayout(Context context) {
        this(context, null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ratioFrameLayoutStyle);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray style;

        style = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout, defStyle, 0);
        mRatio = style.getFloat(R.styleable.RatioFrameLayout_ratio, 0);
        style.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父容器传过来的宽度方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽度的值
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // 父容器传过来的高度的值
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft() - getPaddingRight();

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && mRatio != 0f) {
            // 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
            // 且图片的宽高比已经赋值完毕，不再是0.0f
            // 表示宽度确定，要测量高度
            height = (int) (width / mRatio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && mRatio != 0f) {
            // 判断条件跟上面的相反，宽度方向和高度方向的条件互换
            // 表示高度确定，要测量宽度
            width = (int) (height * mRatio + 0.5f);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //    @Override
    //    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        ViewGroup.LayoutParams layoutParams;
    //        boolean fitWidth;
    //        boolean fitHeight;
    //        int width;
    //        int height;
    //
    //        if (mRatio > 0) {
    //            layoutParams = getLayoutParams();
    //            fitWidth = (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT);
    //            fitHeight = (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT);
    //            if (fitWidth != fitHeight) {
    //                if (fitWidth) {
    //                    width = MeasureSpec.getSize(widthMeasureSpec);
    //                    height = (int) (width / mRatio);
    //                } else {
    //                    height = MeasureSpec.getSize(heightMeasureSpec);
    //                    width = (int) (height * mRatio);
    //                }
    //                setMeasuredDimension(width, height);
    //            } else {
    //                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //            }
    //        } else {
    //            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //        }
    //    }
}