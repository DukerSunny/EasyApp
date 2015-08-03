package com.harreke.easyapp.frameworks.base;

import android.os.Bundle;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/10
 * <p/>
 * Fragment框架接口
 */
public interface IFragment extends IFragmentData {
    /**
     * 初始化Fragment传参数据
     */
    void acquireArguments(Bundle bundle);
}
