package com.harreke.easyapp.widgets.transitions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.utils.ViewUtil;

import java.io.Serializable;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/18
 * <p/>
 * 共享视图信息
 */
public class SharedViewInfo implements Serializable {
    public byte[] bitmap;
    public int endViewId;
    public boolean endViewWithStatusBarHeight;
    public ViewInfo startViewInfo;
    public CharSequence text;

    private void SharedViewInfo() {
    }

    public static class Builder {
        private int mEndViewId;
        private boolean mEndViewWithStatusBarHeight = true;
        private View mStartView;
        private boolean mStartViewWithStatusBarHeight = true;
        private boolean mUseDrawingCache = false;

        public Builder(View startView, int endViewId) {
            mStartView = startView;
            mEndViewId = endViewId;
        }

        public SharedViewInfo build() {
            SharedViewInfo sharedViewInfo = new SharedViewInfo();

            sharedViewInfo.startViewInfo = ViewUtil.getViewInfo(mStartView, mStartViewWithStatusBarHeight);
            sharedViewInfo.endViewId = mEndViewId;
            sharedViewInfo.endViewWithStatusBarHeight = mEndViewWithStatusBarHeight;
            if (mUseDrawingCache) {
                sharedViewInfo.bitmap = ViewUtil.getDrawingCacheBytes(mStartView);
            } else if (mStartView instanceof ImageView) {
                sharedViewInfo.bitmap = ViewUtil.getBitmapBytes((ImageView) mStartView);
            } else if (mStartView instanceof TextView) {
                sharedViewInfo.text = ((TextView) mStartView).getText();
            }

            return sharedViewInfo;
        }

        public Builder endViewWithStatusBarHeight(boolean endViewWithStatusBarHeight) {
            mEndViewWithStatusBarHeight = endViewWithStatusBarHeight;

            return this;
        }

        public Builder startViewWithStatusBarHeight(boolean startViewWithStatusBarHeight) {
            mStartViewWithStatusBarHeight = startViewWithStatusBarHeight;

            return this;
        }

        public Builder useDrawingCache(boolean useDrawingCache) {
            mUseDrawingCache = useDrawingCache;

            return this;
        }
    }

    //    /**
    //     * 创建一个共享视图信息
    //     * <p/>
    //     * A Activity上的指定起始视图会通过位移、缩放等动画效果进入B Activity，成为B上的终止视图
    //     * 如果A或B的视图设置了沉浸模式，则必须通过指定startViewWithStatusBarHeight或endViewWithStatusBarHeight参数，通知函数是否需要计算状态栏的高度，否则会出现A或B的视图动画位置不正确的问题
    //     *
    //     * @param startView                    起始视图
    //     * @param startViewWithStatusBarHeight 起始视图是否需要包括状态栏高度
    //     * @param endViewId                    终止视图Id
    //     * @param endViewWithStatusBarHeight   终止视图是否需要包括状态栏高度
    //     */
    //    public SharedViewInfo(View startView, boolean useDrawingCache, boolean startViewWithStatusBarHeight, int endViewId, boolean endViewWithStatusBarHeight) {
    //        this.startViewInfo = ViewUtil.getViewInfo(startView, startViewWithStatusBarHeight);
    //        this.endViewId = endViewId;
    //        this.endViewWithStatusBarHeight = endViewWithStatusBarHeight;
    //        if (useDrawingCache) {
    //            bitmap = ViewUtil.getDrawingCacheBytes(startView);
    //        } else if (startView instanceof ImageView) {
    //            bitmap = ViewUtil.getBitmapBytes((ImageView) startView);
    //        } else if (startView instanceof TextView) {
    //            text = ((TextView) startView).getText();
    //        }
    //    }
    //
    //    /**
    //     * 创建一个共享视图信息
    //     * <p/>
    //     * A Activity上的指定起始视图会通过位移、缩放等动画效果进入B Activity，成为B上的终止视图
    //     *
    //     * @param startView 起始视图
    //     * @param endViewId 终止视图Id
    //     */
    //    public SharedViewInfo(View startView, int endViewId) {
    //        this(startView, false, true, endViewId, true);
    //    }
    //
    //    public SharedViewInfo(View startView, boolean useDrawingCache, int endViewId) {
    //        this(startView, useDrawingCache, true, endViewId, true);
    //    }
    //
    //    public SharedViewInfo(ViewInfo startViewInfo, int endViewId, boolean endViewWithStatusBarHeight) {
    //        this.startViewInfo = startViewInfo;
    //        this.endViewId = endViewId;
    //        this.endViewWithStatusBarHeight = endViewWithStatusBarHeight;
    //    }
}