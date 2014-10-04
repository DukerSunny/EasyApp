package com.harreke.easyapp.widgets.stateview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 *
 * 状态视图接口
 */
public interface IState {
    public void setShowRetryWhenEmpty(boolean showRetryWhenEmpty);

    public void setState(int state);

    public int getState(int state);
}
