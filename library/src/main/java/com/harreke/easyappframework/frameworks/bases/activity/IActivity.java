package com.harreke.easyappframework.frameworks.bases.activity;

import android.content.Intent;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架的接口
 */
public interface IActivity {
    /**
     * 初始化Activity配置信息
     *
     * 如设置屏幕样式，屏幕亮度，是否全屏等
     */
    public void configActivity();

    /**
     * 初始化Activity传参数据
     */
    public void initData(Intent intent);

    /**
     * 当Activity的ActionBar菜单被创建时触发
     */
    public void onActionBarMenuCreate();
}