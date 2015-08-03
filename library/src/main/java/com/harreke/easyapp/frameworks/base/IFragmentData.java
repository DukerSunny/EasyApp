package com.harreke.easyapp.frameworks.base;

import android.os.Bundle;

/**
 * Created by 启圣 on 2015/6/5.
 */
public interface IFragmentData {
    /**
     * 对Activity发送数据
     *
     * @param bundle 消息
     */
    void sendFragmentData(Bundle bundle);
}
