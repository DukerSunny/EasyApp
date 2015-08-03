package com.harreke.easyapp.widgets.transitions;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum ActivityAnimation {
    None(R.anim.none, R.anim.none),
    /**
     * 从左滑入/滑出
     */
    Slide_Left(R.anim.slide_in_left, R.anim.slide_out_left),
    /**
     * 从右滑入/滑出
     */
    Slide_Right(R.anim.slide_in_right, R.anim.slide_out_right),
    /**
     * 从上滑入/滑出
     */
    Slide_Top(R.anim.slide_in_top, R.anim.slide_out_top),
    /**
     * 从下滑入/滑出
     */
    Slide_Bottom(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

    private int mEnterAnimationId;
    private int mExitAnimationId;

    ActivityAnimation(int enterAnimationId, int exitAnimationId) {
        mEnterAnimationId = enterAnimationId;
        mExitAnimationId = exitAnimationId;
    }

    public int getEnterAnimationId() {
        return mEnterAnimationId;
    }

    public int getExitAnimationId() {
        return mExitAnimationId;
    }
}
