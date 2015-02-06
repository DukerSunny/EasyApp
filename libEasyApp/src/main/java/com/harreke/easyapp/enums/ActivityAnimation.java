package com.harreke.easyapp.enums;

import com.harreke.easyapp.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum ActivityAnimation {
    /**
     * 系统默认动画
     */
    Default(0, 0),
    /**
     * 无动画
     */
    None(0, 0),
    /**
     * 启动动画
     */
    Slide_In_Left(R.anim.slide_in_left, R.anim.none),
    Slide_In_Right(R.anim.slide_in_right, R.anim.none),
    Slide_In_Top(R.anim.abc_slide_in_top, R.anim.none),
    Slide_In_Bottom(R.anim.slide_in_bottom, R.anim.none),
    Fade_In(R.anim.fade_in, R.anim.none),
    /**
     * 退出动画
     */
    Slide_Out_Left(R.anim.none, R.anim.slide_out_left),
    Slide_Out_Right(R.anim.none, R.anim.slide_out_right),
    Slide_Out_Top(R.anim.none, R.anim.slide_out_top),
    Slide_Out_Bottom(R.anim.none, R.anim.slide_out_bottom),
    Fade_Out(R.anim.none, R.anim.fade_out);

    private int mEnterAnim;
    private int mExitAnim;

    private ActivityAnimation(int enterAnim, int exitAnim) {
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
    }

    public int getEnterAnim() {
        return mEnterAnim;
    }

    public int getExitAnim() {
        return mExitAnim;
    }
}