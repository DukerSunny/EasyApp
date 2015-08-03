package com.harreke.easyapp.frameworks.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * 框架接口
 */
public interface IFramework extends IToast, IIntent {
    /**
     * 内容层新增视图
     *
     * @param view   视图
     * @param params 布局参数
     */
    void addContentView(View view, ViewGroup.LayoutParams params);

    /**
     * 分派事件
     * <p/>
     * 将创建好的事件赋予对应的视图或控件
     */
    void attachCallbacks();

    /**
     * 查询布局
     * <p/>
     * 在布局中查询视图与控件
     */
    void enquiryViews();

    /**
     * 初始化事件
     * <p/>
     * 创建回调与监听器
     */
    void establishCallbacks();

    View findViewById(int viewId);

    /**
     * 获得当前Activity框架
     *
     * @return 当前Activity框架
     */
    ActivityFramework getActivityFramework();

    /**
     * 获得Application框架
     *
     * @return Application框架
     */
    ApplicationFramework getApplicationFramework();

    /**
     * 获得当前Activity的Context
     *
     * @return 当前Activity的Context
     */
    Context getContext();

    /**
     * 获得框架接口
     *
     * @return 框架接口
     */
    IFramework getFramework();

    /**
     * 设置内容层布局
     * <p/>
     * 为内容层填充布局
     */
    int getLayoutId();

    /**
     * 判断该框架是否为Activity
     *
     * @return 该框架是否为Activity
     */
    boolean isActivity();

    /**
     * 开始运作
     * <p/>
     * 框架开始执行用户命令
     */
    void startAction();
}