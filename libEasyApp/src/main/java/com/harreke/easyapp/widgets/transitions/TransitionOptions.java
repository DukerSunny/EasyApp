package com.harreke.easyapp.widgets.transitions;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.enums.ActivityTransition;
import com.harreke.easyapp.utils.ViewUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/02/06
 */
public class TransitionOptions {
    public int enterAnimation = R.anim.none;
    public int exitAnimation = R.anim.none;
    public String imageViewUrl = null;
    public int targetViewId = 0;
    public ActivityTransition transition = ActivityTransition.None;
    public RectF viewRect = null;

    public static TransitionOptions fromBundle(Bundle bundle) {
        TransitionOptions options = new TransitionOptions();

        if (bundle != null) {
            options.transition = (ActivityTransition) bundle.getSerializable("transition");
            options.enterAnimation = bundle.getInt("enterAnimation");
            options.exitAnimation = bundle.getInt("exitAnimation");
            options.viewRect = bundle.getParcelable("viewRect");
            options.targetViewId = bundle.getInt("targetViewId");
            options.imageViewUrl = bundle.getString("imageViewUrl");
        }

        return options;
    }

    /**
     * 生成自定义视图切换选项
     *
     * @param enterAnimation
     *         新Activity进入动画
     * @param exitAnimation
     *         新Activity退出动画
     *
     * @return 视图切换选项
     */
    public static TransitionOptions makeCustomTransition(int enterAnimation, int exitAnimation) {
        TransitionOptions options = new TransitionOptions();

        options.enterAnimation = enterAnimation;
        options.exitAnimation = exitAnimation;

        return options;
    }

    public static TransitionOptions makeScaleImageThumbnailTransition(ImageView thumbnail, String scaleImageThumbnailUrl) {
        TransitionOptions options = new TransitionOptions();

        options.transition = ActivityTransition.Scale;
        options.viewRect = ViewUtil.getRect(thumbnail);
        options.imageViewUrl = scaleImageThumbnailUrl;

        return options;
    }

    public static TransitionOptions makeScaleThumbnailTransition(View scaleThumbnail) {
        TransitionOptions options = new TransitionOptions();

        options.transition = ActivityTransition.Scale;
        options.viewRect = ViewUtil.getRect(scaleThumbnail);

        return options;
    }

    public static TransitionOptions makeSharedImageViewTransition(ImageView sharedImageView, String sharedImageViewUrl,
            int targetViewId) {
        TransitionOptions options = new TransitionOptions();

        options.transition = ActivityTransition.SharedImage;
        options.viewRect = ViewUtil.getRect(sharedImageView);
        options.targetViewId = targetViewId;
        options.imageViewUrl = sharedImageViewUrl;

        return options;
    }

    public static TransitionOptions makeSharedViewTransition(View sharedView, int targetViewId) {
        TransitionOptions options = new TransitionOptions();

        options.transition = ActivityTransition.Shared;
        options.viewRect = ViewUtil.getRect(sharedView);
        options.targetViewId = targetViewId;

        return options;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putSerializable("transition", transition);
        bundle.putInt("enterAnimation", enterAnimation);
        bundle.putInt("exitAnimation", exitAnimation);
        bundle.putParcelable("viewRect", viewRect);
        bundle.putInt("targetViewId", targetViewId);
        bundle.putString("imageViewUrl", imageViewUrl);

        return bundle;
    }
}