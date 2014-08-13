package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 消息视图
 *
 * 此视图可以显示三种消息：正在加载、加载出错和无内容，用于提示页面运作状态
 */
public class InfoView extends LinearLayout {
    /**
     * 空内容
     */
    public static final int INFO_EMPTY = 2;
    /**
     * 加载出错
     */
    public static final int INFO_ERROR = 3;
    /**
     * 隐藏
     */
    public static final int INFO_HIDE = 0;
    /**
     * 正在加载
     */
    public static final int INFO_LOADING = 1;

    private ImageView info_empty;
    private ImageView info_loading;
    private ImageView info_retry;
    private TextView info_retryhint;
    private TextView info_text;
    private AnimationDrawable mDrawable = null;
    private String mEmptyText;
    private String mLoadingText;
    private String mRetryText;
    private boolean mShowRetryWhenEmpty;

    public InfoView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.infoViewStyle);
    }

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        LayoutParams params;
        Drawable loadingImage;
        Drawable emptyImage;
        String retryHint;
        int retryHintColor;
        Drawable retryImage;
        int textColor;
        int textSize;

        style = context.obtainStyledAttributes(attrs, R.styleable.InfoView, defStyle, 0);
        loadingImage = style.getDrawable(R.styleable.InfoView_loadingImage);
        mLoadingText = style.getString(R.styleable.app_loadingText);
        emptyImage = style.getDrawable(R.styleable.InfoView_emptyImage);
        mEmptyText = style.getString(R.styleable.InfoView_emptyText);
        retryHint = style.getString(R.styleable.InfoView_retryHint);
        retryHintColor = style.getColor(R.styleable.InfoView_retryHintColor, 0);
        retryImage = style.getDrawable(R.styleable.InfoView_retryImage);
        mRetryText = style.getString(R.styleable.InfoView_retryText);
        mShowRetryWhenEmpty = style.getBoolean(R.styleable.InfoView_showRetryHintWhenEmpty, false);
        textColor = style.getColor(R.styleable.InfoView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.InfoView_textSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        info_loading = new ImageView(context);
        info_loading.setLayoutParams(new LayoutParams(textSize, textSize));
        info_loading.setVisibility(GONE);
        if (loadingImage != null) {
            info_loading.setImageDrawable(loadingImage);
            if (loadingImage instanceof AnimationDrawable) {
                mDrawable = (AnimationDrawable) loadingImage;
            }
        }
        addView(info_loading);

        info_empty = new ImageView(context);
        info_empty.setLayoutParams(new LayoutParams(textSize, textSize));
        if (emptyImage != null) {
            info_empty.setImageDrawable(emptyImage);
        }
        addView(info_empty);

        info_retry = new ImageView(context);
        info_retry.setLayoutParams(new LayoutParams(textSize, textSize));
        if (retryImage != null) {
            info_retry.setImageDrawable(retryImage);
        }
        info_retry.setVisibility(GONE);
        addView(info_retry);

        params = new LayoutParams(-2, -2);
        info_text = new TextView(context);
        info_text.setLayoutParams(params);
        info_text.setText(mEmptyText);
        info_text.setTextColor(textColor);
        info_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(info_text);

        info_retryhint = new TextView(context);
        info_retryhint.setLayoutParams(new LayoutParams(-2, -2));
        info_retryhint.setText(retryHint);
        info_retryhint.setTextColor(retryHintColor);
        info_retryhint.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if (!mShowRetryWhenEmpty) {
            info_retryhint.setVisibility(GONE);
        }
        addView(info_retryhint);
    }

    private void hide() {
        setVisibility(GONE);
    }

    /**
     * 判断是否正在显示“重试”按钮
     *
     * @return 是否正在显示“重试”按钮
     */
    public final boolean isShowingRetry() {
        return info_retryhint.getVisibility() == VISIBLE;
    }

    /**
     * 设置消息视图可见方式
     *
     * @param infoVisibility
     *         可见方式
     *         {@link #INFO_HIDE}
     *         {@link #INFO_LOADING}
     *         {@link #INFO_EMPTY}
     *         {@link #INFO_ERROR}
     */
    public final void setInfoVisibility(int infoVisibility) {
        switch (infoVisibility) {
            case INFO_HIDE:
                hide();
                break;
            case INFO_LOADING:
                showLoading();
                break;
            case INFO_EMPTY:
                showEmpty();
                break;
            case INFO_ERROR:
                showRetry();
        }
    }

    /**
     * 设置“空内容”时是否显示“重试”按钮
     *
     * @param showRetryHintWhenEmpty
     *         “空内容”时是否需要显示“重试”按钮
     */
    public final void setShowRetryWhenEmpty(boolean showRetryHintWhenEmpty) {
        if (mShowRetryWhenEmpty != showRetryHintWhenEmpty) {
            mShowRetryWhenEmpty = showRetryHintWhenEmpty;
            if (showRetryHintWhenEmpty) {
                info_retryhint.setVisibility(View.VISIBLE);
            } else {
                info_retryhint.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示“空内容”消息
     */
    private void showEmpty() {
        setVisibility(VISIBLE);
        if (mDrawable != null) {
            mDrawable.stop();
        }
        info_loading.setVisibility(View.GONE);
        info_empty.setVisibility(View.VISIBLE);
        info_retry.setVisibility(View.GONE);
        info_text.setText(mEmptyText);
        if (mShowRetryWhenEmpty) {
            info_retryhint.setVisibility(View.VISIBLE);
        } else {
            info_retryhint.setVisibility(View.GONE);
        }
    }

    /**
     * 显示“正在加载”消息
     */
    private void showLoading() {
        setVisibility(VISIBLE);
        if (mDrawable != null) {
            mDrawable.start();
        }
        info_loading.setVisibility(View.VISIBLE);
        info_empty.setVisibility(View.GONE);
        info_retry.setVisibility(View.GONE);
        info_text.setText(mLoadingText);
        info_retryhint.setVisibility(View.GONE);
    }

    /**
     * 显示“加载出错”消息
     */
    private void showRetry() {
        setVisibility(VISIBLE);
        if (mDrawable != null) {
            mDrawable.stop();
        }
        info_loading.setVisibility(View.GONE);
        info_empty.setVisibility(View.GONE);
        info_retry.setVisibility(View.VISIBLE);
        info_text.setText(mRetryText);
        info_retryhint.setVisibility(View.VISIBLE);
    }
}