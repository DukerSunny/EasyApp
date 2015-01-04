package com.harreke.easyapp.frameworks.bases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 框架接口
 */
public interface IFramework {
    /**
     * 内容层新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    public void addContentView(View view, ViewGroup.LayoutParams params);

    /**
     * 分派事件
     *
     * 将创建好的事件赋予对应的视图或控件
     */
    public void attachCallbacks();

    /**
     * 取消正在执行的Http请求
     */
    public void cancelRequest();

    /**
     * 查询布局
     *
     * 在布局中查询视图与控件
     */
    public void enquiryViews();

    /**
     * 初始化事件
     *
     * 创建回调与监听器
     */
    public void establishCallbacks();

    /**
     * 执行一个Http请求
     *
     * 注：同一时间只能执行一个请求，新增请求前会先取消正在执行的请求
     *
     * @param builder
     *         Http请求
     * @param callback
     *         Http请求回调
     */
    public void executeRequest(RequestBuilder builder, IRequestCallback<String> callback);

    public View findViewById(int viewId);

    public Activity getActivity();

    /**
     * 获得当前Activity的Context
     *
     * @return 当前Activity的Context
     */
    public Context getContext();

    /**
     * 获得框架
     *
     * @return 框架
     */
    public IFramework getFramework();

    /**
     * 设置内容层布局
     *
     * 为内容层填充布局
     */
    public int getLayoutId();

    /**
     * 隐藏Toast
     */
    public void hideToast();

    /**
     * 是否正在执行一个Http请求
     *
     * @return boolean
     */
    public boolean isRequestExecuting();

    /**
     * 显示Toast
     *
     * @param text
     *         Toast文本
     */
    public void showToast(String text);

    /**
     * 显示Toast
     *
     * @param textId
     *         Toast文本
     */
    public void showToast(int textId);

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     * @param progress
     *         是否显示进度条
     */
    public void showToast(String text, boolean progress);

    /**
     * 显示Toast
     *
     * @param textId
     *         文本
     * @param progress
     *         是否显示进度条
     */
    public void showToast(int textId, boolean progress);

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     */
    public void start(Intent intent);

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param anim
     *         Intent切换动画
     *
     *         {@link com.harreke.easyapp.frameworks.bases.activity.ActivityFramework.Anim}
     */
    public void start(Intent intent, ActivityFramework.Anim anim);

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     *
     *         如果需要回调，则设置requestCode为正整数；否则设为-1；
     */
    public void start(Intent intent, int requestCode);

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     *
     *         如果需要回调，则设置requestCode为正整数；否则设为-1；
     * @param anim
     *         Intent切换动画
     *
     *         {@link com.harreke.easyapp.frameworks.bases.activity.ActivityFramework.Anim}
     */
    public void start(Intent intent, int requestCode, ActivityFramework.Anim anim);

    /**
     * 开始运作
     *
     * 框架开始执行用户命令
     */
    public void startAction();
}