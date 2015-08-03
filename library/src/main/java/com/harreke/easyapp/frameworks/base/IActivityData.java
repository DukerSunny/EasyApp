package com.harreke.easyapp.frameworks.base;

import android.os.Bundle;

/**
 * Created by 启圣 on 2015/6/5.
 */
public interface IActivityData {
    /**
     * 接受并处理来自Fragment的消息
     *
     * @param bundle 消息
     */
    void receiveFragmentData(Bundle bundle);
}
