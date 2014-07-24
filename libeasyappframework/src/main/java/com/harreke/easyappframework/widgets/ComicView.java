package com.harreke.easyappframework.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.harreke.easyappframework.helpers.GestureHelper;
import com.harreke.easyappframework.listeners.OnGestureListener;
import com.harreke.easyappframework.listeners.OnImageTapListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 漫画视图
 *
 * 支持手势缩放、拖拽功能，并可以设置旋转角度
 */
public class ComicView extends ImageView implements OnGestureListener {
    private AnimateRunnable mAnimateRunnable = new AnimateRunnable();
    private boolean mBounceEnabled = false;
    private Matrix mDrawableMatrix = new Matrix();
    private RectF mDrawableRect = new RectF();
    private boolean mFirstMeasure = false;
    private GestureHelper mGesture;
    private OnImageTapListener mImageTapListener = null;
    private Matrix mLimitMatrix = new Matrix();
    private RectF mLimitRect = new RectF();
    private float mMaxScale;
    private float mMiddleScale;
    private float mMinScale;
    private RectF mNowRect = new RectF();
    private float mOffsetX;
    private float mOffsetY;
    private Matrix mOriginalMatrix = new Matrix();
    private float mRotate = 0f;
    private float mScale = 1f;
    private float mScaleX;
    private float mScaleY;
    private Matrix mStartMatrix = new Matrix();
    private RectF mStartRect = new RectF();
    private float mStartScale;
    private float mStartTranslateX;
    private float mStartTranslateY;
    private float mTranslateX = 0f;
    private float mTranslateY = 0f;

    public ComicView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScaleType(ScaleType.MATRIX);

        mGesture = new GestureHelper(context);
        mGesture.setPointerEnabled(true);
        mGesture.setOnGestureListener(this);
    }

    /**
     * 进行动画（位移）
     *
     * @param toX
     *         横向位移终点
     * @param toY
     *         纵向位移终点
     */
    public final void animate(float toX, float toY) {
        animate(toX, toY, mScale);
    }

    /**
     * 进行动画（位移和缩放）
     *
     * @param toX
     *         横向位移终点
     * @param toY
     *         纵向位移终点
     * @param toScale
     *         缩放目标比例
     */
    public final void animate(float toX, float toY, float toScale) {
        mAnimateRunnable.animate(toX, toY, toScale);
    }

    /**
     * 进行动画（缩放）
     *
     * @param toScale
     *         缩放目标比例
     */
    public final void animate(float toScale) {
        animate(mTranslateX, mTranslateY, toScale);
    }

    private boolean checkLimit() {
        float limitWidth;
        float limitHeight;
        float deviationX;
        float deviationY;
        float deviationScale;
        float left;
        float top;
        float right;
        float bottom;
        boolean xChanged = true;
        boolean yChanged = true;

        if (mScale >= mMinScale) {
            deviationScale = (mScale - 1f) / mScale;
            limitWidth = mOffsetX * deviationScale;
            limitHeight = mOffsetY * deviationScale;
            deviationX = (mScaleX - mOffsetX) * deviationScale;
            deviationY = (mScaleY - mOffsetY) * deviationScale;
            left = -limitWidth + deviationX;
            top = -limitHeight + deviationY;
            right = limitWidth + deviationX;
            bottom = limitHeight + deviationY;
            mLimitRect.set(left, top, right, bottom);
            mLimitMatrix.reset();
            mLimitMatrix.postRotate(mRotate, mLimitRect.centerX(), mLimitRect.centerY());
            mLimitMatrix.mapRect(mLimitRect);
            if (mTranslateX < mLimitRect.left) {
                mTranslateX = mLimitRect.left;
            } else if (mTranslateX == mLimitRect.left) {
                xChanged = false;
            } else if (mTranslateX > mLimitRect.right) {
                mTranslateX = mLimitRect.right;
            } else if (mTranslateX == mLimitRect.right) {
                xChanged = false;
            }
            if (mTranslateY < mLimitRect.top) {
                mTranslateY = mLimitRect.top;
            } else if (mTranslateY == mLimitRect.top) {
                yChanged = false;
            } else if (mTranslateY > mLimitRect.bottom) {
                mTranslateY = mLimitRect.bottom;
            } else if (mTranslateY == mLimitRect.bottom) {
                yChanged = false;
            }
        } else {
            xChanged = false;
            yChanged = false;
        }

        return xChanged || yChanged;
    }

    public final Bitmap getImageBitmap() {
        Drawable drawable = getDrawable();
        Canvas canvas;
        Bitmap bitmap = null;

        if (drawable != null) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            drawable.draw(canvas);
        }

        return bitmap;
    }

    private Matrix getMatrix(float translateX, float translateY, float scale, float scaleX, float scaleY) {
        Matrix matrix = new Matrix(mStartMatrix);

        matrix.postTranslate(translateX, translateY);
        matrix.postScale(scale, scale, scaleX, scaleY);

        return matrix;
    }

    /**
     * 当单点按下时时触发
     */
    @Override
    public void onDown() {
        recordTranslate();
    }

    /**
     * 当多点按下时时触发
     * 注：
     * 仅在已开启多点触摸功能时可用
     */
    @Override
    public void onPointerDown() {
        mStartScale = mScale;
        recordTranslate();
    }

    /**
     * 当多点抬起时触发
     * 注：
     * 仅在已开启多点触摸功能时可用
     */
    @Override
    public void onPointerUp() {
        if (mBounceEnabled) {
            if (mScale < mMinScale) {
                mScaleX = mOffsetX;
                mScaleY = mOffsetY;
                animate(mMinScale);
            } else if (mScale > mMaxScale) {
                animate(mMaxScale);
            } else {
                recordTranslate();
                recordScale();
            }
        } else {
            recordTranslate();
            recordScale();
        }
    }

    /**
     * 当缩放时触发
     * 注：
     * 仅在已开启多点触摸功能时可用
     *
     * @param scale
     *         缩放的总倍率（从单点按下时开始计算）
     * @param scaleX
     *         缩放中心x坐标
     * @param scaleY
     *         缩放中心y坐标
     * @param duration
     *         缩放总时长（从单点按下时开始计算）
     *
     * @return 如果需要处理该事件，返回true；否则返回false
     */
    @Override
    public boolean onScale(float scale, float scaleX, float scaleY, long duration) {
        float targetScale = mStartScale + (scale - 1f);

        if (!mBounceEnabled) {
            if (targetScale >= mMinScale && targetScale <= mMaxScale) {
                mScale = targetScale;
                mScaleX = scaleX;
                mScaleY = scaleY;
                refresh();
            }
        } else {
            mScale = targetScale;
            if (targetScale < mMinScale) {
                mScaleX = mOffsetX;
                mScaleY = mOffsetY;
            } else {
                mScaleX = scaleX;
                mScaleY = scaleY;
            }
            checkLimit();
            refresh();
        }

        return true;
    }

    /**
     * 当滑动时触发
     * 注：
     * 仅在未开启多点触摸功能时可用
     *
     * @param translateX
     *         滑动的横向总距离（从单点按下时开始计算）
     * @param translateY
     *         滑动的纵向总距离（从单点按下时开始计算）
     * @param duration
     *         滑动的总时长（从单点按下时开始计算）
     *
     * @return 如果需要处理该事件，返回true；否则返回false
     */
    @Override
    public boolean onScroll(float translateX, float translateY, long duration) {
        return false;
    }

    /**
     * 当点击屏幕时触发
     *
     * @param x
     *         点击的x坐标
     * @param y
     *         点击的y坐标
     * @param tapCount
     *         连续点击的次数
     */
    @Override
    public void onTaps(float x, float y, int tapCount) {
        switch (tapCount) {
            case 1:
                if (mImageTapListener != null) {
                    mImageTapListener.onImageTap();
                }
                break;
            case 2:
                mScaleX = x;
                mScaleY = y;
                if (mScale >= mMinScale && mScale < mMiddleScale) {
                    animate(mMiddleScale);
                } else if (mScale >= mMiddleScale && mScale < mMaxScale) {
                    animate(mMaxScale);
                } else {
                    animate(mMinScale);
                }
        }
    }

    /**
     * 当位移时触发
     * 注：
     * 仅在已开启多点触摸功能时，并且缩放倍率大于最小倍率时可用
     *
     * @param translateX
     *         位移的横向总距离（从单点按下时开始计算）
     * @param translateY
     *         位移的纵向总距离（从单点按下时开始计算）
     * @param duration
     *         位移的总时长（从单点按下时开始计算）
     *
     * @return 如果需要处理该事件，返回true；否则返回false
     */
    @Override
    public boolean onTranslate(float translateX, float translateY, long duration) {
        mTranslateX = mStartTranslateX + translateX;
        mTranslateY = mStartTranslateY + translateY;

        if (checkLimit()) {
            refresh();

            return true;
        } else {
            return false;
        }
    }

    /**
     * 当单点抬起时触发
     */
    @Override
    public void onUp() {
        if (mBounceEnabled) {
            if (mScale < mMinScale) {
                mScaleX = mOffsetX;
                mScaleY = mOffsetY;
                animate(mMinScale);
            } else if (mScale > mMaxScale) {
                animate(mMaxScale);
            } else {
                recordTranslate();
                recordScale();
            }
        } else {
            recordTranslate();
            recordScale();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable;
        float imageWidth;
        float imageHeight;
        float targetScale;
        float widthScale;
        float heightScale;
        float drawableWidth;
        float drawableHeight;
        float targetWidth;
        float targetHeight;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        drawable = getDrawable();
        if (drawable != null) {
            if (mFirstMeasure) {
                mFirstMeasure = false;
                mOriginalMatrix.set(getImageMatrix());
            }
            imageWidth = getMeasuredWidth();
            imageHeight = getMeasuredHeight();
            drawableWidth = drawable.getIntrinsicWidth();
            drawableHeight = drawable.getIntrinsicHeight();
            mDrawableRect.set(0f, 0f, drawableWidth, drawableHeight);
            mDrawableMatrix.reset();
            mDrawableMatrix.postRotate(mRotate);
            mDrawableMatrix.mapRect(mDrawableRect);
            drawableWidth = mDrawableRect.width();
            drawableHeight = mDrawableRect.height();
            widthScale = imageWidth / drawableWidth;
            heightScale = imageHeight / drawableHeight;
            if (drawableWidth * heightScale > imageWidth) {
                targetScale = widthScale;
            } else if (drawableHeight * widthScale > imageHeight) {
                targetScale = heightScale;
            } else {
                targetScale = 1f;
            }
            targetWidth = drawableWidth * targetScale;
            targetHeight = drawableHeight * targetScale;
            if (imageWidth > imageHeight) {
                mMiddleScale = Math.abs(imageWidth / targetWidth);
            } else {
                mMiddleScale = Math.abs(imageHeight / targetHeight);
            }
            mMinScale = 1f;
            if (mMiddleScale == 1f) {
                mMiddleScale = 1.5f;
            }
            mMaxScale = mMiddleScale * 2f;
            mOffsetX = imageWidth / 2f;
            mOffsetY = imageHeight / 2f;
            mScaleX = mOffsetX;
            mScaleY = mOffsetY;
            mStartMatrix.set(mOriginalMatrix);
            mStartMatrix.postScale(targetScale, targetScale);
            mStartMatrix.postTranslate((imageWidth - targetWidth) / 2f, (imageHeight - targetHeight) / 2f);
            mStartMatrix.postRotate(mRotate, mOffsetX, mOffsetY);
            setImageMatrix(mStartMatrix);
            mStartRect.set(0f, 0f, targetWidth, targetHeight);
            mStartMatrix.mapRect(mStartRect);
            mNowRect.set(mStartRect);
            refresh();
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !mAnimateRunnable.isAnimating() && mGesture.onTouch(event);
    }

    private void recordScale() {
        mStartScale = mScale;
    }

    private void recordTranslate() {
        mStartTranslateX = mTranslateX;
        mStartTranslateY = mTranslateY;
    }

    private void refresh() {
        refresh(getMatrix(mTranslateX, mTranslateY, mScale, mScaleX, mScaleY));
    }

    private void refresh(Matrix matrix) {
        setImageMatrix(matrix);
        mNowRect.set(mStartRect);
        matrix.mapRect(mNowRect);
    }

    /**
     * 设置图片点击监听器
     *
     * @param imageTapListener
     *         图片点击监听器
     */
    public final void seOnImageTapListener(OnImageTapListener imageTapListener) {
        mImageTapListener = imageTapListener;
    }

    /**
     * 设置是否开启回弹效果
     *
     * @param bounceEnabled
     *         是否开启回弹效果
     */
    public final void setBounceEnabled(boolean bounceEnabled) {
        mBounceEnabled = bounceEnabled;
    }

    /**
     * 设置旋转角度
     *
     * @param rotate
     *         旋转角度
     */
    public final void setComicRotation(float rotate) {
        mRotate = rotate;
        requestLayout();
    }

    /**
     * 设置最大缩放比例
     *
     * @param maxScale
     *         最大缩放比例
     */
    public final void setMaxScale(float maxScale) {
        mMaxScale = maxScale;
        if (mMinScale > mMaxScale) {
            mMinScale = mMaxScale;
        }
        if (mMiddleScale > mMaxScale) {
            mMiddleScale = mMaxScale;
        }
    }

    /**
     * 设置中等缩放比例
     *
     * @param middleScale
     *         中等缩放比例
     */
    public final void setMiddleScale(float middleScale) {
        mMiddleScale = middleScale;
        if (mMinScale > mMiddleScale) {
            mMinScale = mMiddleScale;
        }
        if (mMaxScale < mMiddleScale) {
            mMaxScale = mMiddleScale;
        }
    }

    /**
     * 设置最小缩放比例
     *
     * @param minScale
     *         最小缩放比例
     */
    public final void setMinScale(float minScale) {
        mMinScale = minScale;
        if (mMiddleScale < mMinScale) {
            mMiddleScale = mMinScale;
        }
        if (mMaxScale < mMinScale) {
            mMaxScale = mMinScale;
        }
    }

    /**
     * 动画
     */
    private class AnimateRunnable implements Runnable {
        private final static int DURATION = 100;
        private final static int FRAME = 10;
        private final static int INTERVAL = DURATION / FRAME;
        private Handler mAnimateHandler = new Handler();
        private boolean mAnimating = false;
        private float mCount;
        private float mScaleStep;
        private float mToScale;
        private float mToX;
        private float mToY;
        private float mXStep;
        private float mYStep;

        private void animate(float toX, float toY, float toScale) {
            mCount = 0;
            mAnimating = true;
            /**
             计算横向偏移
             */
            mToX = toX;
            mXStep = (toX - mTranslateX) / FRAME;
            /**
             计算纵向偏移
             */
            mToY = toY;
            mYStep = (toY - mTranslateY) / FRAME;
            /**
             计算缩放偏差
             */
            mToScale = toScale;
            mScaleStep = (toScale - mScale) / FRAME;
            mAnimateHandler.postDelayed(this, INTERVAL);
        }

        private boolean isAnimating() {
            return mAnimating;
        }

        @Override
        public void run() {
            if (mAnimating) {
                if (mCount < FRAME) {
                    mCount++;
                    if (mXStep != 0f || mYStep != 0f) {
                        mTranslateX += mXStep;
                        mTranslateY += mYStep;
                    }
                    if (mScaleStep != 0f) {
                        mScale += mScaleStep;
                    }
                    checkLimit();
                    refresh();
                    mAnimateHandler.postDelayed(this, INTERVAL);
                } else {
                    mAnimating = false;
                    if (mXStep != 0f || mYStep != 0f) {
                        mTranslateX = mToX;
                        mTranslateY = mToY;
                    }
                    if (mScaleStep != 0f) {
                        mScale = mToScale;
                    }
                    mStartScale = mScale;
                    checkLimit();
                    refresh();
                }
            }
        }
    }
}