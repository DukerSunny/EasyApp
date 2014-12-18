package com.harreke.easyapp.frameworks.bases.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.IToolbar;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import java.lang.ref.WeakReference;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Fragment框架
 */
public abstract class FragmentFramework extends Fragment implements IFramework, IToolbar {
    private static final String TAG = "FragmentFramework";
    private WeakReference<ActivityFramework> mActivityReference;
    private int mContentLayoutId;
    private View mContentView;
    private boolean mCreated;
    private RequestHelper mRequest;

    public FragmentFramework() {
        mActivityReference = null;
    }

    /**
     * 初始化Bundle传参数据
     */
    protected abstract void acquireArguments(Bundle bundle);

    /**
     * 布局新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    @Override
    public final void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mContentView instanceof ViewGroup) {
            ((ViewGroup) mContentView).addView(view, params);
        }
    }

    @Override
    public void addToolbarItem(int id, int titleId, int imageId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.addToolbarItem(id, titleId, imageId);
        }
    }

    @Override
    public void addToolbarViewItem(int id, int titleId, View view) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.addToolbarViewItem(id, titleId, view);
        }
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

    @Override
    public View findViewById(int viewId) {
        return mContentView.findViewById(viewId);
    }

    private ActivityFramework getActivityFramework() {
        if (mActivityReference != null) {
            return mActivityReference.get();
        } else {
            return null;
        }
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
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.hideToast();
        }
    }

    @Override
    public void hideToolbar() {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.hideToolbar();
        }
    }

    @Override
    public void hideToolbarItem(int id) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.hideToolbarItem(id);
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
    public boolean isToolbarShowing() {
        ActivityFramework activity = getActivityFramework();

        return activity != null && activity.isToolbarShowing();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityReference = new WeakReference<ActivityFramework>((ActivityFramework) activity);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRequest = new RequestHelper();

        setLayout();
        mContentView = inflater.inflate(mContentLayoutId, container, false);
        acquireArguments(getArguments());
        establishCallbacks();
        enquiryViews();
        attachCallbacks();

        return mContentView;
    }

    @Override
    public void onDestroyView() {
        cancelRequest();
        mCreated = false;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mActivityReference.clear();
        mActivityReference = null;
        super.onDetach();
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
    public void setContentView(int contentLayoutId) {
        mContentLayoutId = contentLayoutId;
    }

    @Override
    public void setToolbarNavigation() {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setToolbarNavigation();
        }
    }

    @Override
    public void setToolbarNavigation(int imageId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setToolbarNavigation(imageId);
        }
    }

    @Override
    public void setToolbarTitle(String text) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setToolbarTitle(text);
        }
    }

    @Override
    public void setToolbarTitle(int textId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setToolbarTitle(textId);
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

    @Override
    public void showToolbar() {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showToolbar();
        }
    }

    @Override
    public void showToolbarItem(int id) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.showToolbarItem(id);
        }
    }

    /**
     * 启动Intent
     *
     * @param intent
     *         目标Intent
     * @param requestCode
     *         请求代码
     *
     *         如果需要回调，则设置requestCode为正整数；否则设为-1；
     * @param animIn
     *         进入动画Id
     *
     *         如果不需要进入动画，则设置为0
     * @param animOut
     *         退出动画Id
     *
     *         如果不需要退出动画，则设置为0
     */
    @Override
    public void start(Intent intent, int requestCode, int animIn, int animOut) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.start(intent, requestCode, animIn, animOut);
        }
    }

    @Override
    public void start(Intent intent, int animIn, int animOut) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.start(intent, animIn, animOut);
        }
    }

    @Override
    public final void start(Intent intent) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.start(intent);
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
            activity.start(intent, requestCode);
        }
    }
}