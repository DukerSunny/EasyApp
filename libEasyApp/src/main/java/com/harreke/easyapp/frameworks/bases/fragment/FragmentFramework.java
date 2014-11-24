package com.harreke.easyapp.frameworks.bases.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IActionBar;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.InfoView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Fragment框架
 */
public abstract class FragmentFramework extends Fragment implements IFramework, IFragment, IActionBar {
    private static final String TAG = "FragmentFramework";
    private FrameLayout framework_content;
    private InfoView framework_info;
    private boolean mCreated = false;
    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (framework_info.isShowingRetry()) {
                onInfoClick();
            }
        }
    };
    private RequestHelper mRequest;

    public FragmentFramework() {
    }

    @Override
    public void addActionBarImageItem(int id, int imageId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.addActionBarImageItem(id, imageId);
        }
    }

    @Override
    public void addActionBarViewItem(int id, int layoutId, boolean clickable) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.addActionBarViewItem(id, layoutId, clickable);
        }
    }

    @Override
    public void addActionBarViewItem(int id, View item, boolean clickable) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.addActionBarViewItem(id, item, clickable);
        }
    }

    /**
     * 布局新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    @Override
    public final void addContentView(View view, FrameLayout.LayoutParams params) {
        framework_content.addView(view, params);
    }

    /**
     * 取消正在执行的Http请求
     */
    @Override
    public final void cancelRequest() {
        mRequest.cancel();
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
    @Override
    public final void executeRequest(RequestBuilder builder, IRequestCallback<String> callback) {
        builder.print();
        mRequest.execute(getActivity(), builder, callback);
    }

    /**
     * 查找视图
     *
     * @param viewId
     *         视图id
     *
     * @return 视图
     */
    @Override
    public final View findViewById(int viewId) {
        return framework_content.findViewById(viewId);
    }

    private ActivityFramework getActivityFramework() {
        ActivityFramework activity;

        try {
            activity = (ActivityFramework) getActivity();
        } catch (ClassCastException e) {
            activity = null;
        }

        return activity;
    }

    /**
     * 获得内容层视图
     *
     * 框架拥有两层视图，内容层和消息层
     *
     * 内容层为xml文件中编写的实际布局内容
     *
     * @return 内容层视图
     */
    @Override
    public final View getContent() {
        return framework_content;
    }

    /**
     * 获得框架
     *
     * @return 框架
     */
    @Override
    public final IFramework getFramework() {
        return this;
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
     *
     * @see com.harreke.easyapp.widgets.InfoView
     */
    @Override
    public final InfoView getInfo() {
        return framework_info;
    }

    @Override
    public void hideActionbarItem(int id) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.hideActionbarItem(id);
        }
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.hideToast();
        }
    }

    /**
     * 是否正在执行一个Http请求
     *
     * @return boolean
     */
    @Override
    public boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_framework, null);

        if (root != null) {
            framework_content = (FrameLayout) root.findViewById(R.id.framework_content);
            framework_info = (InfoView) root.findViewById(R.id.framework_info);
            mRequest = new RequestHelper();
            framework_info.setOnClickListener(mInfoClickListener);

            setLayout();
            acquireArguments(getArguments());
            establishCallbacks();
            enquiryViews();
            attachCallbacks();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        cancelRequest();
        mCreated = false;
        super.onDestroyView();
    }

    /**
     * 当消息层被点击时触发
     */
    @Override
    public void onInfoClick() {
        if (!isRequestExecuting()) {
            startAction();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mCreated) {
            mCreated = true;
            startAction();
        }
    }

    @Override
    public void setActionBarTitle(String text) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setActionBarTitle(text);
        }
    }

    @Override
    public void setActionBarTitle(int textId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setActionBarTitle(textId);
        }
    }

    /**
     * 设置内容层布局
     *
     * @param view
     *         布局视图
     */
    @Override
    public final void setContentView(View view) {
        framework_content.removeAllViews();
        framework_content.addView(view);
    }

    /**
     * 设置内容层布局
     *
     * @param layoutId
     *         布局Id
     */
    @Override
    public final void setContentView(int layoutId) {
        framework_content.removeAllViews();
        View.inflate(getActivity(), layoutId, framework_content);
    }

    /**
     * 设置内容层是否可见
     *
     * @param visible
     *         是否可见
     */
    @Override
    public void setContentVisible(boolean visible) {
        if (visible) {
            framework_content.setVisibility(View.VISIBLE);
        } else {
            framework_content.setVisibility(View.GONE);
        }
    }

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
    @Override
    public void setInfoVisibility(int infoVisibility) {
        framework_info.setInfoVisibility(infoVisibility);
    }

    @Override
    public void showActionBarHome(boolean show) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showActionBarHome(show);
        }
    }

    @Override
    public void showActionBarItem(int id) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showActionBarItem(id);
        }
    }

    @Override
    public void showActionBarTitle(boolean show) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showActionBarTitle(show);
        }
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     */
    @Override
    public final void showToast(String text) {
        showToast(text, false);
    }

    /**
     * 显示Toast
     *
     * @param textId
     *         文本
     */
    @Override
    public final void showToast(int textId) {
        showToast(getString(textId));
    }

    /**
     * 显示Toast
     *
     * @param text
     *         文本
     * @param progress
     *         是否显示进度条
     */
    @Override
    public final void showToast(String text, boolean progress) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showToast(text, progress);
        }
    }

    /**
     * 显示Toast
     *
     * @param textId
     *         文本Id
     * @param progress
     *         是否显示进度条
     */
    @Override
    public final void showToast(int textId, boolean progress) {
        showToast(getString(textId), progress);
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     */
    @Override
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
    @Override
    public final void start(Intent intent, boolean animate) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.start(intent, animate);
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
    @Override
    public final void start(Intent intent, int requestCode) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            hideToast();
            startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}