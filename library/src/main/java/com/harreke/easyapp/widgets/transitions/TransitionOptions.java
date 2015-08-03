package com.harreke.easyapp.widgets.transitions;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.HashSet;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/02/06
 * <p/>
 * 切换视图选项
 */
public class TransitionOptions {
    public ActivityAnimation animation = ActivityAnimation.None;
    public ActivityTransition transition = ActivityTransition.None;
    public HashSet<SharedViewInfo> viewInfoSet = null;

    @SuppressWarnings("unchecked")
    public static TransitionOptions fromBundle(Bundle bundle) {
        TransitionOptions options = null;

        if (bundle != null) {
            options = new TransitionOptions();
            options.transition = (ActivityTransition) bundle.getSerializable("transition");
            options.animation = (ActivityAnimation) bundle.getSerializable("animation");
            options.viewInfoSet = (HashSet<SharedViewInfo>) bundle.getSerializable("viewInfoSet");
        }

        return options;
    }

    /**
     * 生成自定义视图切换选项
     *
     * @param animation 新Activity进入/退出动画
     * @return 视图切换选项
     */
    public static TransitionOptions makeAnimationTransition(ActivityAnimation animation) {
        TransitionOptions options = new TransitionOptions();

        options.transition = ActivityTransition.Animation;
        options.animation = animation;

        return options;
    }

    //    public static TransitionOptions makeScaleThumbnailTransition(View scaleThumbnail) {
    //        TransitionOptions options = new TransitionOptions();
    //
    //        options.transition = ActivityTransition.Scale;
    //        options.viewInfo = ViewUtil.getRect(scaleThumbnail);
    //
    //        return options;
    //    }

    /**
     * 生成共享视图切换选项
     * <p/>
     * 当A Activity启动B Activity时，如果执行共享视图切换动画，则A上的指定视图会通过位移、缩放等动画效果进入B
     *
     * @param sharedViewInfos 共享视图信息数组
     * @return 共享视图切换选项
     */
    public static TransitionOptions makeSharedViewTransition(@NonNull SharedViewInfo... sharedViewInfos) {
        TransitionOptions options = new TransitionOptions();
        int i;

        options.transition = ActivityTransition.Shared;
        options.viewInfoSet = new HashSet<>(sharedViewInfos.length);
        for (i = 0; i < sharedViewInfos.length; i++) {
            options.viewInfoSet.add(sharedViewInfos[i]);
        }

        return options;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putSerializable("transition", transition);
        bundle.putSerializable("animation", animation);
        bundle.putSerializable("viewInfoSet", viewInfoSet);

        return bundle;
    }
}