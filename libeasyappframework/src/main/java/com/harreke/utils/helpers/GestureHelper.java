package com.harreke.utils.helpers;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;

import com.harreke.utils.listeners.OnGestureListener;

public class GestureHelper {
    private OnGestureListener mGestureListener = null;
    private PointF[] mNowPoint = new PointF[]{new PointF(), new PointF()};
    private boolean mPointerEnabled = false;
    private float mScaleX = 0;
    private float mScaleY = 0;
    private boolean mSingleTap = false;
    private float mStartDistance = 0;
    private PointF mStartPoint = new PointF();
    private long mStartTime = 0;
    private int mTapCount = 0;
    private Runnable mTapRunnable = new Runnable() {
        @Override
        public final void run() {
            mGestureListener.onTaps(mNowPoint[0].x, mNowPoint[0].y, mTapCount);
            mTapCount = 0;
        }
    };
    private Handler mTapHandler = new Handler();
    private float mThreshold;

    public GestureHelper(Context context) {
        mThreshold = context.getResources().getDisplayMetrics().density * 8;
    }

    public final float getNowX(int index) {
        if (index < 2) {
            return mNowPoint[index].x;
        } else {
            return 0;
        }
    }

    public final float getNowY(int index) {
        if (index < 2) {
            return mNowPoint[index].y;
        } else {
            return 0;
        }
    }

    /**
     手势检测入口

     @param event
     动作事件

     @return 是否应该处理该动作事件
     */
    public final boolean onTouch(MotionEvent event) {
        int pointerCount;
        long duration;
        long nowTime;
        float distance;
        float scale;
        float translateX;
        float translateY;

        if (mGestureListener == null) {
            throw new IllegalStateException("Must set OnGestureListener first!");
        } else {
            if (event != null) {
                /**
                 记录当前动作点的位置和时间
                 */
                pointerCount = event.getPointerCount();
                mNowPoint[0].x = event.getX(0);
                mNowPoint[0].y = event.getY(0);
                nowTime = event.getEventTime();
                if (pointerCount > 1) {
                    mNowPoint[1].x = event.getX(1);
                    mNowPoint[1].y = event.getY(1);
                } else {
                    mNowPoint[1].set(mNowPoint[0]);
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        /**
                         单点按下事件
                         */
                        mSingleTap = true;
                        mStartPoint.set(mNowPoint[0]);
                        mStartTime = nowTime;
                        mGestureListener.onDown();
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        /**
                         多点按下事件
                         */
                        if (mPointerEnabled && pointerCount > 1) {
                            /**
                             事件由一个点变成二个点时，记录这两点间距和中心坐标作为缩放依据
                             */
                            mSingleTap = false;
                            distance = distanceF(mNowPoint[0], mNowPoint[1]);
                            /**
                             防止因为误按（两点间距过小）导致的错误计算
                             */
                            if (distance > mThreshold) {
                                mScaleX = (mNowPoint[0].x + mNowPoint[1].x) / 2;
                                mScaleY = (mNowPoint[0].y + mNowPoint[1].y) / 2;
                                mStartDistance = distance;
                                mStartTime = nowTime;
                                mGestureListener.onPointerDown();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        /**
                         单点抬起事件
                         */
                        if (mSingleTap) {
                            mSingleTap = false;
                            /**
                             在点击后进行超时判断，如果200毫秒内再无更多点击事件，则触发回调，否则累加点击数并重新进行超时判断
                             */
                            mTapCount++;
                            mScaleX = mNowPoint[0].x;
                            mScaleY = mNowPoint[0].y;
                            mTapHandler.removeCallbacks(mTapRunnable);
                            mTapHandler.postDelayed(mTapRunnable, 200);
                        } else {
                            mGestureListener.onUp();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        /**
                         多点抬起事件
                         */
                        if (mPointerEnabled && pointerCount > 1) {
                            /**
                             事件由两个点变为一个点时，重设基准
                             */
                            if (event.getActionIndex() == 0) {
                                /**
                                 如果抬起的是第一个点，则以第二个点为基准
                                 */
                                mNowPoint[0].set(mNowPoint[1]);
                            }
                            mStartPoint.set(mNowPoint[0]);
                            mStartTime = nowTime;
                            mGestureListener.onPointerUp();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        /**
                         位移事件
                         */
                        duration = nowTime - mStartTime;
                        if (mPointerEnabled && pointerCount > 1) {
                            /**
                             如果是多点事件，为缩放模式
                             */
                            distance = distanceF(mNowPoint[0], mNowPoint[1]);
                            scale = distance / mStartDistance;

                            return mGestureListener.onScale(scale, mScaleX, mScaleY, duration);
                        } else if (pointerCount == 1) {
                            /**
                             如果是单点事件
                             */
                            translateX = mNowPoint[0].x - mStartPoint.x;
                            translateY = mNowPoint[0].y - mStartPoint.y;
                            if (mSingleTap && Math.abs(translateX) > mThreshold || Math.abs(translateY) > mThreshold || duration > 200) {
                                mSingleTap = false;
                            }
                            if (mPointerEnabled) {
                                /**
                                 拖拽模式
                                 */
                                return mGestureListener.onTranslate(translateX, translateY, duration);
                            } else {
                                /**
                                 滑动模式
                                 */
                                return mGestureListener.onScroll(translateX, translateY, duration);
                            }
                        }
                }
            }
        }

        return true;
    }

    private float distanceF(PointF p1, PointF p2) {
        return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     设置手势监听器

     @param gestureListener
     手势监听器
     */
    public final void setOnGestureListener(OnGestureListener gestureListener) {
        mGestureListener = gestureListener;
    }

    /**
     设置是否开启多点触摸功能

     @param pointerEnabled
     是否开启多点触摸功能
     */
    public final void setPointerEnabled(boolean pointerEnabled) {
        mPointerEnabled = pointerEnabled;
    }
}