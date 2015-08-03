package com.harreke.easyapp.widgets.transitions;

import android.view.View;

import com.harreke.easyapp.widgets.animators.ViewAnimatorSet;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/02/03
 * <p/>
 * Activity切换监听器
 */
public interface OnTransitionListener {
    /**
     * Activity内容开始进入切换效果时触发
     */
    void onContentEnter(View contentView, ViewAnimatorSet animatorSet);

    /**
     * Activity内容开始退出切换效果时触发
     */
    void onContentExit(View contentView, ViewAnimatorSet animatorSet);

    /**
     * 整个Activity完成进入切换效果时触发
     */
    void onPostEnter();

    /**
     * 整个Activity完成退出切换效果时触发
     */
    void onPostExit();

    void onShared(int sharedViewId, View sharedView, View endView, SharedViewInfo sharedViewInfo);

    //    /**
    //     * 某个共享视图开始进入时触发
    //     * <p>
    //     * 进入时，会自动生成一个临时的共享视图，该共享视图复制自Activity的目标视图，两者拥有同一个视图Id
    //     * <p>
    //     * 共享视图处于显示状态，目标视图处于隐藏状态。
    //     * 共享视图通过位移、缩放等动画变形为目标视图的形状，但共享视图并没有内容，是个空白视图，可通过该函数填充临时内容（文字、图像等）
    //     *
    //     * @param sharedViewId 共享视图Id
    //     * @param sharedView   共享视图
    //     */
    //    void onSharedEnter(int sharedViewId, View sharedView, View endView);
    //
    //    /**
    //     * 某个共享视图开始退出时触发
    //     * <p>
    //     * 退出时，会自动生成一个临时的共享视图，该共享视图复制自Activity的目标视图，两者拥有同一个视图Id
    //     * <p>
    //     * 共享视图处于显示状态，目标视图处于隐藏状态。
    //     * 共享视图通过位移、缩放等动画变形为目标视图的形状，但共享视图并没有内容，是个空白视图，可通过该函数填充临时内容（文字、图像等）
    //     *
    //     * @param sharedViewId 共享视图Id
    //     * @param sharedView   共享视图
    //     */
    //    void onSharedExit(int sharedViewId, View sharedView, View endView);
}