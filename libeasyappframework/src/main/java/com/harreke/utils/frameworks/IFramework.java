package com.harreke.utils.frameworks;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.RequestBuilder;
import com.harreke.utils.widgets.InfoView;

/**
 框架接口
 */
public interface IFramework {
    /**
     布局新增视图

     @param view
     视图
     @param params
     布局参数
     */
    public void addContentView(View view, FrameLayout.LayoutParams params);

    /**
     取消正在执行的Http请求
     */
    public void cancelRequest();

    /**
     输出调试信息

     @param message
     调试信息
     */
    public void debug(String message);

    /**
     执行一个Http请求

     注：同一时间只能执行一个请求，新增请求前会先取消正在执行的请求

     @param builder
     Http请求
     @param callback
     Http请求回调
     */
    public <RESULT> void executeRequest(RequestBuilder builder, IRequestCallback<RESULT> callback);

    /**
     查找视图

     @param id
     视图id

     @return 视图
     */
    public View findViewById(int id);

    /**
     获得框架Activity

     @return Activity
     */
    public FragmentActivity getActivity();

    /**
     获得框架

     @return 框架
     */
    public IFramework getFramework();

    /**
     获得覆盖层视图

     框架拥有两层视图，内容层和覆盖层

     覆盖层为一个InfoView（消息视图），盖在内容层上，用来提示相关信息（如加载中）

     框架因执行启动、刷新数据等异步操作，而导致内容层里的内容不可用时，会显示出覆盖层

     当异步操作完成后，覆盖层会隐藏，重新显示出内容层

     @return 覆盖层视图
     */
    public InfoView getInfoView();

    /**
     获得内容层视图

     框架拥有两层视图，内容层和覆盖层

     内容层为xml文件中编写的实际布局内容

     @return 内容层视图
     */
    public View getRootView();

    /**
     隐藏覆盖层
     */
    public void hideInfo();

    /**
     隐藏内容层
     */
    public void hideRoot();

    /**
     隐藏Toast

     @param animate
     是否显示动画
     */
    public void hideToast(boolean animate);

    /**
     隐藏Toast
     */
    public void hideToast();

    /**
     初始化回调
     */
    public void initCallback();

    /**
     初始化配置信息
     */
    public void initConfig();

    /**
     初始化监听器
     */
    public void initEvent();

    /**
     初始化布局
     */
    public void initLayout();

    /**
     初始化视图
     */
    public void initView();

    /**
     是否正在执行一个Http请求

     @return boolean
     */
    public boolean isRequestExecuting();

    /**
     当覆盖层被点击时触发
     */
    public void onInfoClick();

    /**
     设置内容层内容

     @param view
     布局视图
     */
    public void setContentView(View view);

    /**
     设置内容层内容

     @param layoutId
     布局id
     */
    public void setContentView(int layoutId);

    /**
     显示覆盖层（空内容）
     */
    public void showInfoEmpty();

    /**
     显示覆盖层（加载错误）
     */
    public void showInfoError();

    /**
     显示覆盖层（正在加载）
     */
    public void showInfoLoading();

    /**
     显示内容层
     */
    public void showRoot();

    /**
     显示Toast

     @param text
     Toast文本
     */
    public void showToast(String text);

    /**
     显示Toast

     @param text
     文本
     @param progress
     是否显示进度条
     */
    public void showToast(String text, boolean progress);

    /**
     启动Intent

     @param intent
     目标Intent
     */
    public void start(Intent intent);

    /**
     启动Intent

     @param intent
     目标Intent
     @param animate
     是否显示切换动画
     */
    public void start(Intent intent, boolean animate);

    /**
     启动带回调的Intent

     @param intent
     目标Intent
     @param requestCode
     请求代码
     */
    public void start(Intent intent, int requestCode);

    /**
     开始运作
     */
    public void startAction();
}