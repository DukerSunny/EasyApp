package com.harreke.easyapp.frameworks.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.helpers.IntentHelper;
import com.harreke.easyapp.helpers.LoaderHelper;
import com.harreke.easyapp.widgets.transitions.TransitionOptions;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * Fragment框架
 */
public abstract class FragmentFramework extends Fragment implements IFramework, IFragment {
    private static String TAG;
    private IActivityData mActivityData;
    private WeakReference<View> mContentViewRef = null;
    private boolean mFirstEnter = true;
    private IntentHelper mIntentHelper;
    private long mPauseTime = 0;
    private long mRefreshTime = -1l;
    private IToast mToast;
    private boolean mUseContainerLayout = false;

    public FragmentFramework() {
        TAG = getClass().getSimpleName();
    }

    public FragmentFramework(boolean useContainerLayout) {
        this();
        mUseContainerLayout = useContainerLayout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addContentView(View view, ViewGroup.LayoutParams params) {
        View contentView = getContentView();

        if (contentView != null && contentView instanceof ViewGroup) {
            ((ViewGroup) contentView).addView(view, params);
        }
    }

    private void clearContentView() {
        if (mContentViewRef != null) {
            mContentViewRef.clear();
            mContentViewRef = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final View findViewById(int viewId) {
        View contentView = getContentView();

        return contentView == null ? null : contentView.findViewById(viewId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityFramework getActivityFramework() {
        return (ActivityFramework) getActivity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ApplicationFramework getApplicationFramework() {
        return (ApplicationFramework) getActivity().getApplication();
    }

    protected View getContentView() {
        return mContentViewRef == null ? null : mContentViewRef.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Context getContext() {
        return getActivity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final IFramework getFramework() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideToast() {
        mToast.hideToast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActivity() {
        return false;
    }

    public final boolean isFirstEnter() {
        return mFirstEnter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mToast = (IToast) activity;
        mActivityData = (IActivityData) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = null;

        clearContentView();
        if (mUseContainerLayout) {
            contentView = container.findViewById(getLayoutId());
            mContentViewRef = new WeakReference<>(contentView);
        } else if (getLayoutId() > 0) {
            contentView = inflater.inflate(getLayoutId(), container, false);
            mContentViewRef = new WeakReference<>(contentView);
        }
        mFirstEnter = true;
        mIntentHelper = new IntentHelper(this);
        acquireArguments(getArguments());
        establishCallbacks();

        return contentView;
    }

    @Override
    public void onPause() {
        LoaderHelper.cancelAll(this);
        mPauseTime = System.currentTimeMillis();
        super.onPause();
    }

    protected void onRefresh() {
    }

    @Override
    public void onResume() {
        super.onResume();
        View contentView;
        long pausedTime;

        if (mRefreshTime >= 0 && mPauseTime > 0) {
            pausedTime = System.currentTimeMillis() - mPauseTime;
            mPauseTime = 0;
            if (pausedTime > mRefreshTime) {
                onRefresh();
            }
        }
        if (mFirstEnter) {
            mFirstEnter = false;
            contentView = getContentView();
            if (contentView != null) {
                ButterKnife.bind(this, contentView);
                enquiryViews();
            }
            attachCallbacks();
            startAction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendFragmentData(Bundle bundle) {
        mActivityData.receiveFragmentData(bundle);
    }

    /**
     * 设置刷新间隔
     * <p/>
     * 当该Fragment被暂停（{@link #onPause()}）后，重新运作（{@link #onResume()}）时，会检测暂停总时长。若大于刷新间隔时间，则会触发{@link #onRefresh()}函数要求刷新Fragment的内容。
     *
     * @param refreshTime 刷新间隔，单位为毫秒
     *                    <p/>
     *                    设置-1则禁止刷新检测
     */
    public void setRefreshTime(long refreshTime) {
        mRefreshTime = refreshTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToastDuration(long duration) {
        mToast.setToastDuration(duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId) {
        mToast.showToast(textId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text) {
        mToast.showToast(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId, boolean indeterminate) {
        mToast.showToast(textId, indeterminate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text, boolean indeterminate) {
        mToast.showToast(text, indeterminate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(@NonNull Intent intent) {
        mIntentHelper.start(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(@NonNull Intent intent, @Nullable TransitionOptions options) {
        mIntentHelper.start(intent, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(@NonNull Intent intent, int requestCode) {
        mIntentHelper.start(intent, requestCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options) {
        mIntentHelper.start(intent, requestCode, options);
    }

    @Override
    public void startDelay(@NonNull Intent intent, @Nullable TransitionOptions options, long delay) {
        mIntentHelper.startDelay(intent, options, delay);
    }

    @Override
    public void startDelay(@NonNull Intent intent, int requestCode, @Nullable TransitionOptions options, long delay) {
        mIntentHelper.startDelay(intent, requestCode, options, delay);
    }

    @Override
    public void startDelay(@NonNull Intent intent, long delay) {
        mIntentHelper.startDelay(intent, delay);
    }
}