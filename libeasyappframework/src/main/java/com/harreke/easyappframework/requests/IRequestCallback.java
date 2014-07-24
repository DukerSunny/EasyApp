package com.harreke.easyappframework.requests;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Http请求回调接口
 *
 * @param <RESULT>
 *         目标类型
 */
public interface IRequestCallback<RESULT> {
    public void onFailure();

    public void onSuccess(RESULT result);
}