package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyappframework.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 提示视图
 *
 * 提示视图是悬浮在内容层最上方的小型视图，用来提示一些临时的、非重要的消息
 */
public class ToastView extends LinearLayout {
    private AnimationDrawable mDrawable;
    private AlphaAnimation mFadeIn;
    private AlphaAnimation mFadeOut;
    private boolean mNeedsFadeOut = false;
    private boolean mProgress = false;
    private String mText;
    private Handler mToastHandler;
    private ImageView toast_progress;
    private TextView toast_text;

    public ToastView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toastViewStyle);
    }

    public ToastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources resources = getResources();
        TypedArray style;
        LayoutParams params;
        boolean showProgress;
        String text;
        int textColor;
        int textSize;
        int widget_xlarge;

        widget_xlarge = (int) resources.getDimension(R.dimen.widget_xlarge);

        style = context.obtainStyledAttributes(attrs, R.styleable.ToastView, defStyle, 0);
        showProgress = style.getBoolean(R.styleable.ToastView_showProgress, false);
        text = style.getString(R.styleable.ToastView_text);
        textColor = style.getColor(R.styleable.ToastView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.ToastView_textSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        toast_progress = new ImageView(context);
        toast_text = new TextView(context);

        params = new LayoutParams(textSize, textSize);
        params.setMargins(0, 0, widget_xlarge, 0);
        toast_progress.setLayoutParams(params);
        toast_progress.setImageResource(R.drawable.anim_loading);
        mDrawable = (AnimationDrawable) toast_progress.getDrawable();
        if (!showProgress) {
            toast_progress.setVisibility(GONE);
        }
        addView(toast_progress);

        toast_text.setLayoutParams(new LayoutParams(-2, -2));
        toast_text.setText(text);
        toast_text.setTextColor(textColor);
        toast_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(toast_text);

        mFadeIn = new AlphaAnimation(0, 1);
        mFadeIn.setDuration(400);
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


        });
        mFadeOut = new AlphaAnimation(1, 0);
        mFadeOut.setDuration(400);
        mFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        mToastHandler = new Handler();
    }

    /**
     * 隐藏提示视图
     */
    public final void hide() {
        hide(true);
    }

    /**
     * 隐藏提示视图
     *
     * @param animte
     *         是否使用渐隐动画
     */
    public final void hide(boolean animte) {
        mNeedsFadeOut = false;
        clearAnimation();
        if (animte) {
            startAnimation(mFadeOut);
        } else {
            setVisibility(GONE);
        }
    }

    /**
     * 判断是否正在显示提示视图
     *
     * @return 是否正在显示提示视图
     */
    public final boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    /**
     * 显示提示视图
     *
     * 可以选择临时性或持续性两种显示方式
     *
     * 临时性提示会在显示4秒后自动隐藏；持续性提示会一直显示，直到手动隐藏
     *
     * @param text
     *         提示文字
     * @param progress
     *         是否为持续性提示
     */
    public final void show(String text, boolean progress) {
        mText = text;
        mProgress = progress;
        mToastHandler.post(new Runnable() {
            @Override
            public void run() {
                toast_text.setText(mText);
                start(mProgress);
            }
        });
    }

    private void start(boolean progress) {
        clearAnimation();
        setVisibility(GONE);
        startAnimation(mFadeIn);
        if (progress) {
            toast_progress.setVisibility(View.VISIBLE);
            mDrawable.start();
        } else {
            mDrawable.stop();
            toast_progress.setVisibility(View.GONE);
            mNeedsFadeOut = true;
            mToastHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mNeedsFadeOut) {
                        startAnimation(mFadeOut);
                    }
                }
            }, 4000);
        }
    }
}