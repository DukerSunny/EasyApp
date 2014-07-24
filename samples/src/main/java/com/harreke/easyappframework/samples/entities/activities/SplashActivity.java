package com.harreke.easyappframework.samples.entities.activities;

import android.content.Intent;
import android.os.Handler;

import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.beans.ActionBarItem;
import com.harreke.easyappframework.frameworks.activity.ActivityFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/23
 *
 * Splash界面
 *
 * 用于在启动真正的主界面MainActivity之前显示SplashScreen
 */
public class SplashActivity extends ActivityFramework {
    private Handler splashHandler = new Handler();
    /**
     * 计时Runnable
     */
    private Runnable splashRunnable = new Runnable() {
        @Override
        public void run() {
            /**
             * 计时结束，启动主界面
             */
            start(MainActivity.create(getActivity()), false);
            exit(false);
        }
    };

    /**
     * 分派事件
     *
     * 将创建好的事件赋予对应的视图或控件
     */
    @Override
    public void assignEvents() {
    }

    /**
     * 初始化事件
     *
     * 创建回调与监听器
     */
    @Override
    public void newEvents() {
    }

    /**
     * 查询布局
     *
     * 在布局中查询视图与控件
     */
    @Override
    public void queryLayout() {
    }

    /**
     * 设置内容层布局
     *
     * 为内容层填充布局
     */
    @Override
    public void setLayout() {
        setContent(R.layout.activity_splash);
    }

    /**
     * 开始运作
     *
     * 框架开始执行用户命令
     */
    @Override
    public void startAction() {
        /**
         * 延时3秒后启动主界面
         */
        splashHandler.postDelayed(splashRunnable, 3000);
    }

    /**
     * 初始化Activity配置信息
     *
     * 如设置屏幕样式，屏幕亮度，是否全屏等
     */
    @Override
    public void configActivity() {
        super.configActivity();
    }

    @Override
    public void onBackPressed() {
        /**
         * 如果在计时结束前按下返回键，则直接退出，不再继续启动MainActivity
         */
        splashHandler.removeCallbacks(splashRunnable);
        exit(false);
    }

    /**
     * 初始化Activity传参数据
     */
    @Override
    public void initData(Intent intent) {
    }

    /**
     * 当Activity的ActionBar菜单被创建时触发
     */
    @Override
    public void onActionBarMenuCreate() {
    }

    /**
     * 当ActionBar的菜单选项被点击时触发
     *
     * @param position
     *         菜单选项的位置
     */
    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }
}