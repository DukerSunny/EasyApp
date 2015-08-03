package com.harreke.easyapp.frameworks.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.harreke.easyapp.R;
import com.harreke.easyapp.helpers.ConnectionHelper;
import com.harreke.easyapp.helpers.IntentHelper;
import com.harreke.easyapp.helpers.LoaderHelper;
import com.harreke.easyapp.helpers.ToastHelper;
import com.harreke.easyapp.helpers.ToolbarHelper;
import com.harreke.easyapp.utils.LogUtil;
import com.harreke.easyapp.widgets.animators.ViewAnimator;
import com.harreke.easyapp.widgets.animators.ViewAnimatorSet;
import com.harreke.easyapp.widgets.transitions.ActivityTransition;
import com.harreke.easyapp.widgets.transitions.OnTransitionListener;
import com.harreke.easyapp.widgets.transitions.SharedViewInfo;
import com.harreke.easyapp.widgets.transitions.TransitionLayout;
import com.harreke.easyapp.widgets.transitions.TransitionOptions;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 * <p/>
 * Activity框架
 */
public abstract class ActivityFramework extends AppCompatActivity
        implements IFramework, IToolbar, IActivity, Toolbar.OnMenuItemClickListener {
    private static String TAG;
    private IntentFilter mConnectionIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectionHelper.checkConnection(context);

            onConnectionChange(ConnectionHelper.isConnected());
        }
    };
    private boolean mFirstEnter = true;
    private Handler mHandler = new Handler();
    private IntentHelper mIntentHelper;
    private long mPauseTime = 0;
    private long mRefreshTime = -1l;
    private ToastHelper mToastHelper = null;
    private ToolbarHelper mToolbarHelper;
    private long mTouchTime = 0;
    private TransitionLayout mTransitionLayout = null;
    private TransitionOptions mTransitionOptions = null;
    private OnTransitionListener mOnActivityTransitionListener = new OnTransitionListener() {
        @Override
        public void onContentEnter(View contentView, ViewAnimatorSet animatorSet) {
            onTransitionContentEnter(contentView, animatorSet);
        }

        @Override
        public void onContentExit(View contentView, ViewAnimatorSet animatorSet) {
            onTransitionContentExit(contentView, animatorSet);
        }

        @Override
        public void onPostEnter() {
            onTransitionPostEnter();
        }

        @Override
        public void onPostExit() {
            onTransitionPostExit();
        }

        @Override
        public void onShared(int sharedViewId, View sharedView, View endView, SharedViewInfo sharedViewInfo) {
            onTransitionShared(sharedViewId, sharedView, endView, sharedViewInfo);
        }
    };
    private Runnable mBackPressedRunnable = new Runnable() {
        @Override
        public void run() {
            onBackPressed();
        }
    };
    private View.OnClickListener mOnNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onToolbarNavigationClick();
        }
    };

    private void attachToolbar(int toolbarSolidId) {
        mToolbarHelper = new ToolbarHelper(this, toolbarSolidId);
        mToolbarHelper.setOnNavigationClickListener(mOnNavigationClickListener);
        mToolbarHelper.setOnMenuItemClickListener(this);
    }

    private void attachTransitionLayout(TransitionLayout transitionLayout) {
        mTransitionLayout = transitionLayout;
        if (mTransitionLayout != null) {
            super.setContentView(mTransitionLayout);
            mTransitionLayout.setOnTransitionListener(mOnActivityTransitionListener);
            if (mTransitionOptions != null && mTransitionOptions.transition != ActivityTransition.None &&
                    mTransitionOptions.transition != ActivityTransition.Animation) {
                mTransitionLayout.setMeasureViewId(getLayoutId());
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        long touchTime;

        if (mFirstEnter) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchTime = System.currentTimeMillis();
            if (touchTime - mTouchTime <= 300) {
                mTouchTime = touchTime;

                return true;
            } else {
                mTouchTime = touchTime;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableDefaultToolbarNavigation() {
        mToolbarHelper.enableDefaultToolbarNavigation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void exit() {
        finish();
        if (mTransitionOptions != null) {
            if (mTransitionOptions.transition == ActivityTransition.Animation) {
                overridePendingTransition(0, mTransitionOptions.animation.getExitAnimationId());
            } else {
                overridePendingTransition(R.anim.none, R.anim.none);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ActivityFramework getActivityFramework() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ApplicationFramework getApplicationFramework() {
        return (ApplicationFramework) getApplication();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Context getContext() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public final FragmentFramework getFragment(String tag) {
        return (FragmentFramework) getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * {@inheritDoc}
     */
    public final FragmentFramework getFragment(int index) {
        List<Fragment> fragmentList;

        fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && index >= 0 && index < fragmentList.size()) {
            return (FragmentFramework) fragmentList.get(index);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final IFramework getFramework() {
        return this;
    }

    protected Toolbar getToolbar() {
        return mToolbarHelper.getToolbar();
    }

    protected int getToolbarSolidId() {
        return R.id.toolbar_solid;
    }

    public final TransitionLayout getTransitionLayout() {
        return mTransitionLayout;
    }

    public TransitionOptions getTransitionOptions() {
        return mTransitionOptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideToast() {
        if (mToastHelper != null) {
            mToastHelper.hideToast();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideToolbar() {
        if (mToolbarHelper != null) {
            mToolbarHelper.hideToolbar();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideToolbarItem(int id) {
        if (mToolbarHelper != null) {
            mToolbarHelper.hideToolbarItem(id);
        }
        //        invalidateOptionsMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActivity() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isToolbarShowing() {
        return mToolbarHelper != null && mToolbarHelper.isToolbarShowing();
    }

    protected TransitionLayout makeTransitionLayout() {
        return new TransitionLayout(this);
    }

    public final void onBackPressed(long delay) {
        mHandler.removeCallbacks(mBackPressedRunnable);
        mHandler.postDelayed(mBackPressedRunnable, delay);
    }

    @Override
    public void onBackPressed() {
        if (mTransitionLayout != null) {
            mTransitionLayout.startExitTransition(mTransitionOptions);
        } else {
            exit();
        }
    }

    protected void onConnectionChange(boolean connected) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        configActivity();
        super.onCreate(savedInstanceState);
        mToastHelper = new ToastHelper(this);
        mIntentHelper = new IntentHelper(this);
        mTransitionOptions = TransitionOptions.fromBundle(getIntent().getBundleExtra("transitionOptions"));
        attachTransitionLayout(makeTransitionLayout());
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        attachToolbar(getToolbarSolidId());
        acquireArguments(getIntent());
        establishCallbacks();
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        mToolbarHelper.setMenu(menu);
        mToolbarHelper.inflate(getToolbarMenuId());
        createMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        if (mToastHelper != null) {
            mToastHelper.release();
        }
        mHandler.removeCallbacks(mBackPressedRunnable);
        mBackPressedRunnable = null;
        if (mTransitionLayout != null) {
            mTransitionLayout.release();
            mTransitionLayout = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LoaderHelper.cancelAll(this);
        unregisterReceiver(mConnectionReceiver);
        mPauseTime = System.currentTimeMillis();
        MobclickAgent.onPause(this);
        super.onPause();
    }

    protected void onRefresh() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        registerReceiver(mConnectionReceiver, mConnectionIntentFilter);
    }

    protected void onToolbarNavigationClick() {
        onBackPressed();
    }

    protected void onTransitionContentEnter(View contentView, ViewAnimatorSet animatorSet) {
        if (mTransitionOptions != null && mTransitionOptions.transition == ActivityTransition.Shared) {
            animatorSet.add(ViewAnimator.animate(contentView).alphaStart(0f).alphaEnd(1f).visibilityStart(View.VISIBLE)
                    .visibilityReverseEnd(View.INVISIBLE));
        }
    }

    protected void onTransitionContentExit(View contentView, ViewAnimatorSet animatorSet) {
    }

    protected void onTransitionPostEnter() {
        startAction();
    }

    protected void onTransitionPostExit() {
        exit();
    }

    protected void onTransitionShared(int sharedViewId, View sharedView, View endView, SharedViewInfo sharedViewInfo) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        long pausedTime;
        long time = System.currentTimeMillis();

        if (hasFocus) {
            mTouchTime = time;
            if (mFirstEnter) {
                mFirstEnter = false;
                enquiryViews();
                attachCallbacks();
                if (mTransitionLayout != null) {
                    mTransitionLayout.startEnterTransition(mTransitionOptions);
                } else {
                    startAction();
                }
            } else if (mRefreshTime >= 0 && mPauseTime > 0) {
                pausedTime = time - mPauseTime;
                mPauseTime = 0;
                if (pausedTime > mRefreshTime) {
                    onRefresh();
                }
            }
        }
    }

    @Override
    public void patchToolbarTopPadding() {
        if (mToolbarHelper != null) {
            mToolbarHelper.patchToolbarTopPadding();
        }
    }

    @Override
    public void receiveFragmentData(Bundle bundle) {
    }

    @Override
    public void setContentView(int layoutId) {
        if (mTransitionLayout != null) {
            mTransitionLayout.setContentView(layoutId);
        } else {
            super.setContentView(layoutId);
        }
    }

    public void setRefreshTime(long refreshTime) {
        mRefreshTime = refreshTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToastDuration(long duration) {
        mToastHelper.setToastDuration(duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolbarNavigation(int imageId) {
        mToolbarHelper.setToolbarNavigation(imageId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolbarSubTitle(int titleId) {
        mToolbarHelper.setToolbarSubTitle(titleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolbarSubTitle(String title) {
        mToolbarHelper.setToolbarSubTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolbarTitle(int textId) {
        mToolbarHelper.setToolbarTitle(textId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolbarTitle(String text) {
        mToolbarHelper.setToolbarTitle(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId) {
        mToastHelper.showToast(textId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text) {
        mToastHelper.showToast(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(int textId, boolean indeterminate) {
        mToastHelper.showToast(textId, indeterminate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToast(String text, boolean indeterminate) {
        mToastHelper.showToast(text, indeterminate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToolbar() {
        mToolbarHelper.showToolbar();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showToolbarItem(int id) {
        mToolbarHelper.showToolbarItem(id);
        //        invalidateOptionsMenu();
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