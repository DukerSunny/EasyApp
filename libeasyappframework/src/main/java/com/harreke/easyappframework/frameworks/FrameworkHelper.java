package com.harreke.easyappframework.frameworks;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.harreke.easyappframework.R;
import com.harreke.easyappframework.helpers.RequestHelper;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.RequestBuilder;
import com.harreke.easyappframework.tools.DevUtil;
import com.harreke.easyappframework.widgets.InfoView;
import com.harreke.easyappframework.widgets.ToastView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 框架助手
 */
public abstract class FrameworkHelper {
    private static final String TAG = "Framework";
    private FrameLayout mContent;
    private InfoView mInfo;
    private ToastView mToast;
    private IFramework mFramework;

    public FrameworkHelper(IFramework framework) {
        mFramework = framework;
    }

    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mInfo.isShowingRetry()) {
                if (!isRequestExecuting()) {
                    mFramework.startAction();
                }
            }
        }
    };
    private RequestHelper mRequest = new RequestHelper();

    /**
     * 布局新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    public void addContentView(View view, FrameLayout.LayoutParams params) {
        mContent.addView(view, params);
    }

    /**
     * 取消正在执行的Http请求
     */
    public void cancelRequest() {
        mRequest.cancel();
    }

    /**
     * 输出调试信息
     *
     * @param message
     *         调试信息
     */
    public void debug(String message) {
        DevUtil.e(TAG, message);
    }

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
    public <RESULT> void executeRequest(RequestBuilder builder, IRequestCallback<RESULT> callback) {
        mRequest.execute(mFramework.getActivity(), builder, callback);
    }

    /**
     * 获得内容层视图
     *
     * 框架拥有两层视图，内容层和消息层
     * 内容层为xml文件中编写的实际布局内容
     *
     * @return 内容层视图
     */
    public final FrameLayout getContent() {
        return mContent;
    }

    /**
     * 获得消息层视图
     *
     * 框架拥有两层视图，内容层和消息层
     * 消息层为一个InfoView（消息视图），盖在内容层上，用来提示相关信息（如加载中）
     * 框架因执行启动、刷新数据等异步操作，而导致内容层里的内容不可用时，会显示出消息层
     * 当异步操作完成后，消息层会隐藏，重新显示出内容层
     *
     * @return 消息层视图
     */
    public final InfoView getInfo() {
        return mInfo;
    }

    /**
     * 隐藏Toast
     *
     * @param animate
     *         是否显示动画
     */
    public final void hideToast(boolean animate) {
        mToast.hide(animate);
    }

    /**
     * 隐藏Toast
     */
    public final void hideToast() {
        mToast.hide();
    }

    /**
     * 隐藏Toast
     */
    public final boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    /**
     * 查找视图
     *
     * @param viewId
     *         视图id
     *
     * @return 视图
     */
    public final View queryContent(int viewId) {
        return mContent.findViewById(viewId);
    }

    /**
     * 设置内容层布局
     *
     * @param view
     *         布局视图
     */
    public final void setContent(View view) {
        mContent.removeAllViews();
        mContent.addView(view);
    }

    /**
     * 设置内容层布局
     *
     * @param layoutId
     *         布局id
     */
    public final void setContent(int layoutId) {
        mContent.removeAllViews();
        View.inflate(mFramework.getActivity(), layoutId, mContent);
    }

    /**
     * 设置内容层是否可见
     *
     * @param visible
     *         是否可见
     */
    public void setContentVisible(boolean visible) {
        if (visible) {
            mContent.setVisibility(View.VISIBLE);
        } else {
            mContent.setVisibility(View.GONE);
        }
    }

    /**
     * 设置消息层可见方式
     *
     * @param infoVisibility
     *         可见方式
     *         {@link com.harreke.easyappframework.widgets.InfoView#INFO_HIDE}
     *         {@link com.harreke.easyappframework.widgets.InfoView#INFO_LOADING}
     *         {@link com.harreke.easyappframework.widgets.InfoView#INFO_EMPTY}
     *         {@link com.harreke.easyappframework.widgets.InfoView#INFO_ERROR}
     */
    public void setInfoVisibility(int infoVisibility) {
        mInfo.setInfoVisibility(infoVisibility);
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     */
    public final void showToast(String text) {
        mToast.show(text, false);
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     * @param progress
     *         是否显示进度条
     */
    public final void showToast(String text, boolean progress) {
        mToast.show(text, progress);
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     */
    public final void start(Intent intent) {
        start(intent, true);
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param animate
     *         是否显示切换动画
     */
    public final void start(Intent intent, boolean animate) {
        Activity activity = mFramework.getActivity();

        if (activity != null && intent != null) {
            hideToast(false);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            if (animate) {
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    /**
     * 启动带回调的Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     */
    public final void start(Intent intent, int requestCode) {
        Activity activity = mFramework.getActivity();

        if (intent != null) {
            hideToast(false);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    /**
     * 退出Activity
     */
    public final void exit() {
        exit(true);
    }

    /**
     * 退出Activity
     *
     * @param animate
     *         是否显示动画
     */
    public final void exit(boolean animate) {
        Activity activity = mFramework.getActivity();

        if (activity != null) {
            activity.finish();
            if (animate) {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }
    }

    public final FrameLayout create(LayoutInflater inflater) {
        FrameLayout framework = (FrameLayout) inflater.inflate(R.layout.widget_framework, null);
        mContent = (FrameLayout) framework.findViewById(R.id.framework_content);
        mInfo = (InfoView) framework.findViewById(R.id.framework_info);
        mToast = (ToastView) framework.findViewById(R.id.framework_toast);
        mInfo.setOnClickListener(mInfoClickListener);

        return framework;
    }

    public final void destroy() {
        hideToast(false);
        cancelRequest();
    }
}