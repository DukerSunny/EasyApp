package com.harreke.easyapp.requests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harreke.easyapp.frameworks.base.IFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/30
 */
public interface IExecutor<RESULT> {
    /**
     * 异步方式获得网络数据
     *
     * 使用{@link IRequestCallback}回调
     *
     * @param framework
     *         要执行Http请求的框架
     * @param requestCallback
     *         请求结果回调
     */
    void execute(@NonNull IFramework framework, @Nullable IRequestCallback<RESULT> requestCallback);
}