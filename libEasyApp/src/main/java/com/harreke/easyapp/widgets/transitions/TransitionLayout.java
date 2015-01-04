package com.harreke.easyapp.widgets.transitions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.harreke.easyapp.utils.ResourceUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public abstract class TransitionLayout extends FrameLayout {
    private int[] mColors;
    private int mContentHeight = 0;
    private float mContentOffsetX = 0f;
    private float mContentOffsetY = 0f;
    private View mContentView;
    private int mContentWidth = 0;
    private Animator.AnimatorListener mEnterCompleteListener = null;
    private EnterTransition mEnterTransition = EnterTransition.None;
    private Animator.AnimatorListener mExitCompleteListener = null;
    private ExitTransition mExitTransition = ExitTransition.None;
    private ValueAnimator.AnimatorUpdateListener mHeroUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setHeroPosition((PointF) animation.getAnimatedValue("heroPosition"));
            setHeroSize((PointF) animation.getAnimatedValue("heroSize"));
        }
    };
    private ImageView mHeroView;
    private Animator.AnimatorListener mHeroViewListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            removeView(mHeroView);
        }
    };
    private Animator.AnimatorListener mHeroInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            ViewPropertyAnimator heroAnimator = ViewPropertyAnimator.animate(mHeroView);

            heroAnimator.alpha(0f).setDuration(300).setListener(mHeroViewListener).start();
        }
    };
    private Animator.AnimatorListener mHeroOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            ViewPropertyAnimator heroAnimator = ViewPropertyAnimator.animate(mHeroView);

            heroAnimator.alpha(0f).setDuration(300).setListener(mHeroViewListener).start();
        }
    };
    private ValueAnimator.AnimatorUpdateListener mOffsetUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setContentOffsetX((Float) animation.getAnimatedValue());
        }
    };
    private TypeEvaluator<PointF> mPointFEvaluator = new TypeEvaluator<PointF>() {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                    startValue.y + fraction * (endValue.y - startValue.y));
        }
    };

    protected TransitionLayout(Context context) {
        super(context);

        mColors = ResourceUtil.obtainThemeColor(context);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    }

    private ValueAnimator animateHero(ImageViewInfo oldImageViewInfo, ImageViewInfo newImageViewInfo) {
        PropertyValuesHolder heroSizeHolder;
        PropertyValuesHolder heroPositionHolder;
        PointF heroOldPosition;
        PointF heroOldSize;
        PointF heroNewPosition;
        PointF heroNewSize;
        ValueAnimator heroAnimator;

        heroOldPosition = oldImageViewInfo.getPosition();
        heroOldSize = oldImageViewInfo.getSize();
        heroNewPosition = newImageViewInfo.getPosition();
        heroNewSize = newImageViewInfo.getSize();
        mHeroView = new ImageView(getContext());
        mHeroView.setLayoutParams(new ViewGroup.LayoutParams((int) heroOldSize.x, (int) heroOldSize.y));
        //        ImageLoaderHelper.loadImage(mHeroView, oldImageViewInfo.getImageUrl(), 0, 0);
        addView(mHeroView);
        ViewHelper.setX(mHeroView, heroOldPosition.x);
        ViewHelper.setY(mHeroView, heroOldPosition.y);
        heroPositionHolder = PropertyValuesHolder.ofObject("heroPosition", mPointFEvaluator, heroOldPosition, heroNewPosition);
        heroSizeHolder = PropertyValuesHolder.ofObject("heroSize", mPointFEvaluator, heroOldSize, heroNewSize);
        heroAnimator = ValueAnimator.ofPropertyValuesHolder(heroPositionHolder, heroSizeHolder);
        heroAnimator.setDuration(300);
        heroAnimator.addUpdateListener(mHeroUpdateListener);

        return heroAnimator;
    }

    protected void animateToX(float targetX, Animator.AnimatorListener listener) {
        ValueAnimator contentAnimator = ValueAnimator.ofFloat(mContentOffsetX, targetX);

        contentAnimator.setDuration(300);
        contentAnimator.addUpdateListener(mOffsetUpdateListener);
        if (listener != null) {
            contentAnimator.addListener(listener);
        }
        contentAnimator.start();
    }

    public void destroy() {
        mContentView = null;
    }

    protected int getContentHeight() {
        return mContentHeight;
    }

    protected int getContentWidth() {
        return mContentWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mContentWidth = getMeasuredWidth();
        mContentHeight = getMeasuredHeight();
    }

    private void performHeroIn(ImageViewInfo oldImageViewInfo, ImageViewInfo newImageViewInfo) {
        ViewPropertyAnimator contentAnimator = ViewPropertyAnimator.animate(mContentView);
        ValueAnimator heroAnimator;

        ViewHelper.setAlpha(mContentView, 0f);
        contentAnimator.alpha(1f).setDuration(600).setListener(mEnterCompleteListener).start();
        heroAnimator = animateHero(oldImageViewInfo, newImageViewInfo);
        heroAnimator.addListener(mHeroInListener);
        heroAnimator.start();
    }

    private void performHeroOut(ImageViewInfo oldImageViewInfo, ImageViewInfo newImageViewInfo) {
        ViewPropertyAnimator contentAnimator = ViewPropertyAnimator.animate(mContentView);
        ValueAnimator heroAnimator;

        contentAnimator.alpha(0f).setDuration(600).setListener(mExitCompleteListener).start();
        heroAnimator = animateHero(oldImageViewInfo, newImageViewInfo);
        heroAnimator.addListener(mHeroOutListener);
        heroAnimator.start();
    }

    public void setActivity(Activity activity) {
        ViewGroup decorView;
        View contentView;
        ViewGroup.LayoutParams contentParams;

        if (mContentView == null) {
            decorView = (ViewGroup) activity.getWindow().getDecorView();
            contentView = decorView.getChildAt(0);
            contentParams = contentView.getLayoutParams();
            decorView.removeView(contentView);
            addView(contentView);
            decorView.addView(this, contentParams);

            mContentView = contentView;
        } else {
            throw new IllegalArgumentException("Cannot set activity twice!");
        }
    }

    protected void setContentOffsetX(float contentOffsetX) {
        mContentOffsetX = contentOffsetX;
        ViewHelper.setX(mContentView, mContentOffsetX);
        setBackgroundColor(Color.argb((int) (192 - 192 * contentOffsetX / mContentWidth), 0, 0, 0));
    }

    protected void setContentOffsetY(float contentOffsetY) {
        mContentOffsetY = contentOffsetY;
        ViewHelper.setY(mContentView, mContentOffsetY);
        setBackgroundColor(Color.argb((int) (192 - 192 * contentOffsetY / mContentHeight), 0, 0, 0));
    }

    public void setEnterCompleteListener(Animator.AnimatorListener enterCompleteListener) {
        mEnterCompleteListener = enterCompleteListener;
    }

    public void setExitCompleteListener(Animator.AnimatorListener exitCompleteListener) {
        mExitCompleteListener = exitCompleteListener;
    }

    private void setHeroPosition(PointF value) {
        ViewHelper.setX(mHeroView, value.x);
        ViewHelper.setY(mHeroView, value.y);
    }

    private void setHeroSize(PointF value) {
        ViewGroup.LayoutParams params = mHeroView.getLayoutParams();

        params.width = (int) value.x;
        params.height = (int) value.y;
        mHeroView.setLayoutParams(params);
    }

    /**
     * 播放“进入”过渡动画
     *
     * @param enterTransition
     *         过渡动画
     * @param params
     *         部分过渡动画需要指定参数
     *
     *         Hero_In
     *         参数1：ImageViewInfo，取自旧视图中的ImageView，该旧ImageView将成为焦点，大小和位置将变换成新ImageView
     *         参数2，ImageViewInfo，取自新视图中的目标ImageView
     *
     *         {@link com.harreke.easyapp.widgets.transitions.ImageViewInfo}
     */
    public void startEnterTransition(final EnterTransition enterTransition, Object... params) {
        mEnterTransition = enterTransition;
        switch (enterTransition) {
            case Slide_In_Left:
                setContentOffsetX(-mContentWidth);
                animateToX(0f, mEnterCompleteListener);
                break;
            case Slide_In_Right:
                setContentOffsetX(mContentWidth);
                animateToX(0f, mEnterCompleteListener);
                break;
            //            case Hero_In:
            //                try {
            //                    performHeroIn((ImageViewInfo) params[0], (ImageViewInfo) params[1]);
            //                } catch (Exception e) {
            //                    throw new IllegalArgumentException("Invalid hero-in params");
            //                }
            //                break;
        }
    }

    public void startExitTransition(ExitTransition exitTransition, Object... params) {
        mExitTransition = exitTransition;
        switch (exitTransition) {
            case Slide_Out_Left:
                animateToX(-mContentWidth, mExitCompleteListener);
                break;
            case Slide_Out_Right:
                animateToX(mContentWidth, mExitCompleteListener);
                break;
            //            case Hero_Out:
            //                try {
            //                    performHeroOut((ImageViewInfo) params[0], (ImageViewInfo) params[1]);
            //                } catch (Exception e) {
            //                    throw new IllegalArgumentException("Invalid hero-out params");
            //                }
            //                break;
        }
    }

    public enum EnterTransition {
        None,
        /**
         * 从左滑入
         */
        Slide_In_Left,
        /**
         * 从右滑入
         */
        Slide_In_Right,
        //        Slide_In_Top,
        //        Slide_In_Bottom,
        //        /**
        //         * 将一个ImageView作为焦点，从旧视图变换至新视图
        //         */
        //        Hero_In,
        //        /**
        //         * 将旧视图以波纹扩散效果变为新视图
        //         */
        //        Ripple_In
    }

    public enum ExitTransition {
        None,
        /**
         * 从左滑出
         */
        Slide_Out_Left,
        /**
         * 从右滑出
         */
        Slide_Out_Right,
        //        Slide_Out_Top,
        //        Slide_Out_Bottom,
        //        /**
        //         * 将一个ImageView作为焦点，从新视图变换至旧视图
        //         */
        //        Hero_Out,
        //        /**
        //         * 将新视图以波纹收缩效果变为旧视图
        //         */
        //        Ripple_Out
    }
}