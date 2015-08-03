package com.harreke.easyapp.widgets.circluarprogresses;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/20
 */
public class CircularProgressView extends ImageView {
    private CircularProgressDrawable mProgressDrawable = null;

    public CircularProgressView(Context context) {
        super(context);

        mProgressDrawable = new CircularProgressDrawable(context);
        mProgressDrawable.setProgress(0f);
        setImageDrawable(mProgressDrawable);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressViewStyle);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray style;
        float progress;
        int progressColor;

        style = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView, defStyleAttr, 0);
        progress = style.getFloat(R.styleable.CircularProgressView_progress, 0f);
        progressColor = style.getColor(R.styleable.CircularProgressView_progressColor, 0);
        style.recycle();

        mProgressDrawable = new CircularProgressDrawable(progressColor);
        mProgressDrawable.setProgress(progress);
        setImageDrawable(mProgressDrawable);
    }

    public void setProgress(float progress) {
        mProgressDrawable.setProgress(progress);
    }
}