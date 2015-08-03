package com.harreke.easyapp.frameworks.base;

import android.content.Intent;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/10
 * <p/>
 * Activity框架接口
 */
public interface IActivity extends IActivityData {
    /**
     * 初始化Activity传参数据
     */
    void acquireArguments(Intent intent);

    /**
     * 初始化Activity配置信息
     * <p/>
     * 如设置屏幕样式、屏幕亮度、是否全屏、过渡动画等
     */
    void configActivity();

    /**
     * 创建Activity菜单
     */
    void createMenu();

    /**
     * 退出Activity
     */
    void exit();

    /**
     * 获得Activity框架中加载的Fragment框架
     *
     * @param index Fragment框架序号
     * @return Fragment框架
     */
    FragmentFramework getFragment(int index);

    FragmentFramework getFragment(String tag);

    int getToolbarMenuId();
}
