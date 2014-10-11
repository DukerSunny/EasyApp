package com.harreke.easyapp.frameworks.bases;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.InfoView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 框架接口
 */
public interface IFramework extends IInfoClickListener {
    /**
     * 内容层新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    public void addContentView(View view, FrameLayout.LayoutParams params);

    /**
     * 分派事件
     *
     * 将创建好的事件赋予对应的视图或控件
     */
    public void assignEvents();

    /**
     * 取消正在执行的Http请求
     */
    public void cancelRequest();

    /**
     * 输出调试信息
     *
     * @param message
     *         调试信息
     */
    public void debug(String message);

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

    /**
     * 查找内容层指定视图
     *
     * @param viewId
     *         视图id
     *
     * @return 视图
     */
    public View findViewById(int viewId);

    /**
     * 获得当前Activity
     *
     * @return 当前Activity
     */
    public Activity getActivity();

    /**
     * 获得内容层视图
     *
     * 框架拥有两层视图，内容层和消息层
     * 内容层为xml文件中编写的实际布局内容
     *
     * @return 内容层视图
     */
    public View getContent();

    /**
     * 获得框架
     *
     * @return 框架
     */
    public IFramework getFramework();

    /**
     * 获得消息层视图
     *
     * 框架拥有两层视图，内容层和消息层
     * 消息层为一个InfoView（消息视图），盖在内容层上，用来提示相关信息（如加载中）
     * 框架因执行启动、刷新数据等异步操作，而导致内容层里的内容不可用时，会显示出消息层，屏蔽内容层一切触摸点击事件
     * 当异步操作完成后，消息层会隐藏，重新显示出内容层，并解除对内容层事件的屏蔽
     *
     * @return 消息层视图
     *
     * @see com.harreke.easyapp.widgets.InfoView
     */
    public InfoView getInfo();

    /**
     * 隐藏Toast
     *
     * @param animate
     *         是否显示动画
     */
    public void hideToast(boolean animate);

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
     * 初始化事件
     *
     * 创建回调与监听器
     */
    public void newEvents();

    /**
     * 查询布局
     *
     * 在布局中查询视图与控件
     */
    public void queryLayout();

    /**
     * 设置内容层布局
     *
     * @param view
     *         布局视图
     */
    public void setContentView(View view);

    /**
     * 设置内容层布局
     *
     * @param layoutId
     *         布局Id
     */
    public void setContentView(int layoutId);

    /**
     * 设置内容层是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setContentVisible(boolean visible);

    /**
     * 设置消息层可见方式
     *
     * @param infoVisibility
     *         可见方式
     *         {@link com.harreke.easyapp.widgets.InfoView#INFO_HIDE}
     *         {@link com.harreke.easyapp.widgets.InfoView#INFO_LOADING}
     *         {@link com.harreke.easyapp.widgets.InfoView#INFO_EMPTY}
     *         {@link com.harreke.easyapp.widgets.InfoView#INFO_ERROR}
     */
    public void setInfoVisibility(int infoVisibility);

    /**
     * 设置内容层布局
     *
     * 为内容层填充布局
     */
    public void setLayout();

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
     * @param animate
     *         是否显示切换动画
     */
    public void start(Intent intent, boolean animate);

    /**
     * 启动带回调的Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     */
    public void start(Intent intent, int requestCode);

    /**
     * 开始运作
     *
     * 框架开始执行用户命令
     */
    public void startAction();
}