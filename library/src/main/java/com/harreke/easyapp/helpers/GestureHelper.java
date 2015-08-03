package com.harreke.easyapp.helpers;

import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.harreke.easyapp.frameworks.base.ApplicationFramework;
import com.harreke.easyapp.utils.MathUtil;

public class GestureHelper {
    private PointF mLastPoint = new PointF();
    private float mLastPointerDistance = 0f;
    private long mLastPointerTime = 0l;
    private long mLastTime = 0l;
    private PointF mNowPoint = new PointF();
    private PointF mNowPointerPoint = new PointF();
    private OnGestureListener mOnGestureListener = null;
    private boolean mPointerEnabled = false;
    private float mScaleX = 0f;
    private float mScaleY = 0f;
    private boolean mSingleTap = false;
    private PointF mStartPoint = new PointF();
    private float mStartPointerDistance = 0f;
    private long mStartPointerTime = 0l;
    private long mStartTime = 0l;
    private int mTapCount = 0;
    private Handler mTapHandler = new Handler();
    private Runnable mTapRunnable = new Runnable() {
        @Override
        public final void run() {
            mOnGestureListener.onTap(mNowPoint.x, mNowPoint.y, mTapCount);
            mTapCount = 0;
        }
    };

    public GestureHelper(@NonNull OnGestureListener gestureListener) {
        mOnGestureListener = gestureListener;
    }

    /**
     * 手势检测入口
     *
     * @param event 动作事件
     * @return 是否需要消费该动作事件
     */
    public final boolean onTouch(@NonNull MotionEvent event) {
        long duration;
        float distance;
        float scale;
        float scrollX;
        float scrollY;
        long nowTime;
        int pointerCount;

        pointerCount = event.getPointerCount();
        recordPoint(mNowPoint, event, 0);
        nowTime = event.getEventTime();
        if (pointerCount == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mSingleTap = true;
                    mStartPoint.set(mNowPoint);
                    mStartTime = nowTime;
                    mLastPoint.set(mStartPoint);
                    mLastTime = nowTime;

                    return mOnGestureListener.onDown(mStartPoint.x, mStartPoint.y);
                case MotionEvent.ACTION_MOVE:
                    scrollX = mNowPoint.x - mLastPoint.x;
                    scrollY = mNowPoint.y - mLastPoint.y;
                    distance = MathUtil.getDistance(scrollX, scrollY);
                    duration = nowTime - mLastTime;
                    mLastPoint.set(mNowPoint);
                    mLastTime = nowTime;
                    if (distance > ApplicationFramework.TouchThreshold) {
                        mSingleTap = false;
                    }

                    return mOnGestureListener.onScroll(scrollX, scrollY, duration);
                case MotionEvent.ACTION_UP:
                    if (mSingleTap) {
                        mSingleTap = false;
                        mTapCount++;
                        mTapHandler.removeCallbacks(mTapRunnable);
                        mTapHandler.postDelayed(mTapRunnable, 200);

                        return true;
                    } else {
                        return mOnGestureListener.onUp();
                    }
            }
        } else if (pointerCount == 2 && mPointerEnabled) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    mSingleTap = false;
                    recordPoint(mNowPointerPoint, event, 1);
                    distance = MathUtil.getDistance(mNowPoint, mNowPointerPoint);
                    if (distance > ApplicationFramework.Density) {
                        mScaleX = (mNowPoint.x + mNowPointerPoint.x) / 2;
                        mScaleY = (mNowPoint.y + mNowPointerPoint.y) / 2;
                        mStartPointerDistance = distance;
                        mStartPointerTime = nowTime;
                        mLastPointerDistance = distance;
                        mLastPointerTime = nowTime;

                        return mOnGestureListener.onPointerDown();
                    }
                case MotionEvent.ACTION_MOVE:
                    recordPoint(mNowPointerPoint, event, 1);
                    distance = MathUtil.getDistance(mNowPoint, mNowPointerPoint);
                    duration = nowTime - mLastPointerTime;
                    scale = distance / mLastPointerDistance;
                    mLastPointerDistance = distance;
                    mLastPointerTime = nowTime;

                    return mOnGestureListener.onScale(scale, mScaleX, mScaleY, duration);
                case MotionEvent.ACTION_POINTER_UP:
                    if (event.getActionIndex() == 0) {
                        mNowPoint.set(mNowPointerPoint);
                    }
                    mStartPoint.set(mNowPoint);
                    mStartTime = nowTime;
                    mLastPoint.set(mStartPoint);
                    mLastTime = nowTime;

                    return mOnGestureListener.onPointerUp();
            }
        }

        return false;
    }

    private void recordPoint(PointF point, MotionEvent event, int index) {
        point.x = event.getX(index);
        point.y = event.getY(index);
    }

    /**
     * 设置是否开启多点触摸功能
     *
     * @param pointerEnabled 是否开启多点触摸功能
     */
    public final void setPointerEnabled(boolean pointerEnabled) {
        mPointerEnabled = pointerEnabled;
    }

    /**
     * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
     * <p/>
     * 手势监听器
     * 注：
     * 支持最多2个点的手势
     *
     * @see com.harreke.easyapp.helpers.GestureHelper
     */
    public interface OnGestureListener {
        /**
         * 当单点按下时时触发
         *
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onDown(float downX, float downY);

        /**
         * 当多点按下时时触发
         * <p/>
         * 注：
         * 仅在已开启多点触摸功能时可用
         *
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onPointerDown();

        /**
         * 当多点抬起时触发
         * <p/>
         * 注：
         * 仅在已开启多点触摸功能时可用
         *
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onPointerUp();

        /**
         * 当缩放时触发
         * <p/>
         * 注：
         * 仅在已开启多点触摸功能时可用
         *
         * @param scale    缩放的倍率
         * @param scaleX   缩放中心x坐标
         * @param scaleY   缩放中心y坐标
         * @param duration 缩放时长
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onScale(float scale, float scaleX, float scaleY, long duration);

        /**
         * 当滑动时触发
         *
         * @param scrollX  滑动的横向距离
         * @param scrollY  滑动的纵向距离
         * @param duration 滑动的时长
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onScroll(float scrollX, float scrollY, long duration);

        /**
         * 当点击屏幕时触发
         *
         * @param x        点击的x坐标
         * @param y        点击的y坐标
         * @param tapCount 连续点击的次数
         */
        void onTap(float x, float y, int tapCount);

        /**
         * 当单点抬起时触发
         *
         * @return 如果需要消费该事件，返回true；否则返回false
         */
        boolean onUp();
    }
}