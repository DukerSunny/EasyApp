package com.harreke.easyapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 提示视图
 *
 * 提示视图是悬浮在内容层最上方的小型视图，用来提示一些临时的、非重要的消息
 */
public class ToastView extends LinearLayout {
    private final static String TAG = "ToastView";
    private AlphaAnimation mFadeIn;
    private AlphaAnimation mFadeOut;
    private Runnable mFadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            startAnimation(mFadeOut);
        }
    };
    private Handler mHandler;
    private boolean mShowing = false;
    private ImageView mToastProgress;
    private TextView mToastText;

    public ToastView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toastViewStyle);
    }

    public ToastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray style;
        LayoutParams params;
        Drawable drawable;
        boolean showProgress;
        String text;
        int textColor;
        int textSize;
        int drawablePadding;

        style = context.obtainStyledAttributes(attrs, R.styleable.ToastView, defStyle, 0);
        drawable = style.getDrawable(R.styleable.ToastView_drawable);
        drawablePadding = (int) style.getDimension(R.styleable.ToastView_drawablePadding, 0);
        showProgress = style.getBoolean(R.styleable.ToastView_showProgress, false);
        text = style.getString(R.styleable.ToastView_text);
        textColor = style.getColor(R.styleable.ToastView_textColor, 0);
        textSize = (int) style.getDimension(R.styleable.ToastView_textSize, 0);
        style.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        mToastProgress = new ImageView(context);

        params = new LayoutParams(textSize, textSize);
        params.setMargins(0, 0, drawablePadding, 0);
        mToastProgress.setLayoutParams(params);
        if (drawable != null) {
            mToastProgress.setImageDrawable(drawable);
        }
        if (!showProgress) {
            mToastProgress.setVisibility(GONE);
        }
        addView(mToastProgress);

        mToastText = new TextView(context);
        mToastText.setLayoutParams(new LayoutParams(-2, -2));
        mToastText.setText(text);
        mToastText.setTextColor(textColor);
        mToastText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mToastText);

        mFadeIn = new AlphaAnimation(0f, 1f);
        mFadeIn.setDuration(400);
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }
        });
        mFadeOut = new AlphaAnimation(1f, 0f);
        mFadeOut.setDuration(400);
        mFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        mHandler = new Handler();

        setVisibility(GONE);
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
        mShowing = false;
        mHandler.removeCallbacks(mFadeOutRunnable);
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
        return mShowing;
    }

    /**
     * 显示提示视图
     *
     * 可以选择临时性或持续性两种显示方式
     * 临时性提示会在显示4秒后自动隐藏；持续性提示会一直显示，直到手动隐藏
     *
     * @param text
     *         提示文字
     * @param progress
     *         是否为持续性提示
     */
    public final void show(String text, boolean progress) {
        mHandler.removeCallbacks(mFadeOutRunnable);
        mShowing = true;
        mToastText.setText(text);
        if (progress) {
            mToastProgress.setVisibility(VISIBLE);
        } else {
            mToastProgress.setVisibility(GONE);
            mHandler.postDelayed(mFadeOutRunnable, 3400);
        }
        clearAnimation();
        startAnimation(mFadeIn);
    }

    /**
     * 显示提示视图
     *
     * 可以选择临时性或持续性两种显示方式
     * 临时性提示会在显示4秒后自动隐藏；持续性提示会一直显示，直到手动隐藏
     *
     * @param textId
     *         提示文字Id
     * @param progress
     *         是否为持续性提示
     */
    public final void show(int textId, boolean progress) {
        show(getResources().getString(textId), progress);
    }
}