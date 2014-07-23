package com.harreke.utils.frameworks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.harreke.utils.R;
import com.harreke.utils.frameworks.IFramework;
import com.harreke.utils.frameworks.activity.ActivityFramework;
import com.harreke.utils.helpers.RequestHelper;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.RequestBuilder;
import com.harreke.utils.tools.Debug;
import com.harreke.utils.widgets.InfoView;
import com.harreke.utils.widgets.ToastView;

public abstract class FragmentFramework extends Fragment implements IFramework, IFragment {
    private static final String TAG = "Framework_Fragment";

    private InfoView framework_info;
    private FrameLayout framework_root;
    private ToastView framework_toast;
    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (framework_info.isShowingRetry()) {
                onInfoClick();
            }
        }
    };
    private RequestHelper mRequest;

    /**
     {@inheritDoc}
     */
    @Override
    public final void addContentView(View view, FrameLayout.LayoutParams params) {
        framework_root.addView(view, params);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void cancelRequest() {
        mRequest.cancel();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void debug(String message) {
        Debug.e(TAG, message);
    }

    /**
     {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <RESULT> void executeRequest(RequestBuilder builder, IRequestCallback<RESULT> callback) {
        mRequest.execute(getActivity(), builder, callback);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final View findViewById(int textId) {
        return framework_root.findViewById(textId);
    }

    @Override
    public final IFramework getFramework() {
        return this;
    }

    @Override
    public final InfoView getInfoView() {
        return framework_info;
    }

    @Override
    public final View getRootView() {
        return framework_root;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void hideInfo() {
        framework_info.hide();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void hideRoot() {
        framework_root.setVisibility(View.GONE);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void hideToast(boolean animate) {
        framework_toast.hide(animate);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void hideToast() {
        framework_toast.hide();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void onInfoClick() {
        if (!isRequestExecuting()) {
            startAction();
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setContentView(View view) {
        framework_root.removeAllViews();
        framework_root.addView(view);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setContentView(int layoutId) {
        framework_root.removeAllViews();
        View.inflate(getActivity(), layoutId, framework_root);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showInfoEmpty() {
        framework_info.showEmpty();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showInfoError() {
        framework_info.showError();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showInfoLoading() {
        framework_info.showLoading();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showRoot() {
        framework_root.setVisibility(View.VISIBLE);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showToast(String text) {
        framework_toast.show(text, false);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void showToast(String text, boolean withProgress) {
        framework_toast.show(text, withProgress);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void start(Intent intent) {
        start(intent, true);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void start(Intent intent, boolean animate) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            hideToast(false);
            activity.start(intent, animate);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void start(Intent intent, int requestCode) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            hideToast(false);
            startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public ActivityFramework getActivityFramework() {
        return (ActivityFramework) getActivity();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout framework = (FrameLayout) inflater.inflate(R.layout.widget_framework, null);

        if (framework != null) {
            framework_root = (FrameLayout) framework.findViewById(R.id.framework_root);
            framework_info = (InfoView) framework.findViewById(R.id.framework_info);
            framework_toast = (ToastView) framework.findViewById(R.id.framework_toast);
            mRequest = new RequestHelper();
            framework_info.setOnClickListener(mInfoClickListener);
        }

        return framework;
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLayout();
        initData(getArguments());
        initCallback();
        initView();
        initEvent();
        startAction();
    }

    @Override
    public void onDestroyView() {
        hideToast(false);
        cancelRequest();
        super.onDestroyView();
    }

    public final void setActionBarTitle(int textId) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setActionBarTitle(textId);
        }
    }

    public final void setActionBarTitle(String title) {
        ActivityFramework activity = getActivityFramework();

        if (activity != null) {
            activity.setActionBarTitle(title);
        }
    }
}