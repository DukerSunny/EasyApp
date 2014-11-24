package com.harreke.easyapp.requests;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Http请求执行器
 */
public interface IRequestExecutor {
    public void cancel();

    public boolean isExecuting();
}