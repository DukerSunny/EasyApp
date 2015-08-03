package com.harreke.easyapp.frameworks.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.widgets.transitions.TransitionOptions;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/10
 * <p/>
 * Intent接口
 * <p/>
 * Intent接口处理所有从Activity和Fragment发起的Intent请求
 * <p/>
 * Intent接口封装了对{@link TransitionOptions}Activity切换选项的调用
 * Activity切换选项兼容实现了安卓Lollipop中部分Activity切换动画效果
 */
public interface IIntent {
    /**
     * 启动Intent
     *
     * @param intent 目标Intent
     */
    void start(@NonNull Intent intent);

    /**
     * 启动Intent
     *
     * @param intent  目标Intent
     *                请求代码
     * @param options Activity切换选项
     */
    void start(@NonNull Intent intent, @Nullable TransitionOptions options);

    /**
     * 启动Intent
     *
     * @param intent      目标Intent
     * @param requestCode 请求代码
     */
    void start(@NonNull Intent intent, int requestCode);

    /**
     * 启动Intent
     *
     * @param intent      目标Intent
     * @param requestCode 请求代码
     * @param options     Activity切换选项
     */
    void start(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options);

    void startDelay(@NonNull Intent intent, @Nullable TransitionOptions options, long delay);

    void startDelay(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options, long delay);

    void startDelay(@NonNull Intent intent, long delay);
}