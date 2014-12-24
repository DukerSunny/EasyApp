package com.harreke.easyapp.frameworks.bases.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.IToolbar;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.receivers.ExitReceiver;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.tools.NetUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架
 */
public abstract class ActivityFramework extends ActionBarActivity
        implements IFramework, IToolbar, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "ActivityFramework";
    private boolean mCreated = false;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private Menu mMenu;
    private View.OnClickListener mOnNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onToolbarNavigationClick();
        }
    };
    private RequestHelper mRequest = new RequestHelper();
    private SuperActivityToast mToast;
    private Toolbar mToolbar;
    private View mToolbarShadow;

    /**
     * 初始化Activity传参数据
     */
    protected abstract void acquireArguments(Intent intent);

    @TargetApi(11)
    @Override
    public void addToolbarItem(int id, int titleId, int imageId) {
        MenuItem item = mMenu.add(0, id, id, titleId);

        item.setIcon(imageId);

        if (Build.VERSION.SDK_INT > 11) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @TargetApi(11)
    @Override
    public void addToolbarViewItem(int id, int titleId, View view) {
        MenuItem item;

        if (Build.VERSION.SDK_INT >= 11) {
            item = mMenu.add(0, id, id, titleId);
            item.setActionView(view);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            Log.e(TAG, "Sdk version too low to call this method!");
        }
    }

    protected void attachToolbar(int toolbarSolidId, int toolbarShadowId) {
        mToolbar = (Toolbar) findViewById(toolbarSolidId);
        mToolbarShadow = findViewById(toolbarShadowId);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(mOnNavigationClickListener);
            mToolbar.setOnMenuItemClickListener(this);
        }
    }

    /**
     * 取消正在执行的Http请求
     */
    @Override
    public void cancelRequest() {
        mRequest.cancel();
    }

    /**
     * 初始化Activity配置信息
     *
     * 如设置屏幕样式，屏幕亮度，是否全屏等
     */
    protected void configActivity() {
    }

    protected void createMenu() {
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
    public void executeRequest(RequestBuilder builder, IRequestCallback<String> callback) {
        builder.print();
        mRequest.execute(this, builder, callback);
    }

    public final void exit() {
        exit(-1, -1);
    }

    /**
     * 退出Activity
     *
     * @param animIn
     *         进入动画Id
     *
     *         设置为-1则使用默认进入动画
     * @param animOut
     *         退出动画Id
     *
     *         设置为-1则使用默认退出动画
     */
    public final void exit(int animIn, int animOut) {
        super.onBackPressed();
        if (animIn <= 0) {
            animIn = R.anim.zoom_in_enter;
        }
        if (animOut <= 0) {
            animOut = R.anim.slide_out_right;
        }
        overridePendingTransition(animIn, animOut);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 获得当前Activity
     *
     * @return 当前Activity
     */
    @Override
    public Context getContext() {
        return this;
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

    public Toolbar getToolBar() {
        return mToolbar;
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        SuperActivityToast.clearSuperActivityToastsForActivity(this);
    }

    @Override
    public void hideToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
        if (mToolbarShadow != null) {
            mToolbarShadow.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideToolbarItem(int id) {
        MenuItem item;

        if (mToolbar != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(false);
            }
        }
    }

    private void initToast() {
        mToast = new SuperActivityToast(this);
        mToast.setAnimations(SuperToast.Animations.POPUP);
        mToast.setDuration(SuperToast.Duration.SHORT);
        mToast.setBackground(SuperToast.Background.GRAY);
        mToast.setTextSize(SuperToast.TextSize.MEDIUM);
        mToast.setTextColor(Color.WHITE);
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    @Override
    public boolean isToolbarShowing() {
        return mToolbar != null && mToolbar.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mExitReceiver, new IntentFilter(getPackageName() + ".EXIT"));
        NetUtil.checkConnection(this);

        configActivity();
        initToast();
        setLayout();
        attachToolbar(R.id.toolbar_solid, R.id.toolbar_shadow);
        acquireArguments(getIntent());
        establishCallbacks();
        enquiryViews();
        attachCallbacks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;

        createMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mExitReceiver);
        hideToast();
        cancelRequest();
        mCreated = false;
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mCreated) {
            mCreated = true;
            startAction();
        }
    }

    protected void onToolbarNavigationClick() {
        onBackPressed();
    }

    public void setToolbarSubTitle(int titleId) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(titleId);
        }
    }

    public void setToolbarSubTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(title);
        }
    }

    @Override
    public void setToolbarNavigation() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.image_toolbar_back);
        }
    }

    @Override
    public void setToolbarNavigation(int imageId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(imageId);
        }
    }

    @Override
    public void setToolbarTitle(int textId) {
        if (mToolbar != null) {
            mToolbar.setTitle(textId);
        }
    }

    @Override
    public void setToolbarTitle(String text) {
        if (mToolbar != null) {
            mToolbar.setTitle(text);
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
    public void showToast(String text, boolean progress) {
        mToast.dismiss();
        mToast.setText(text);
        //        if (progress) {
        //            mToast.setIcon(R.drawable.anim_progress_radiant, SuperToast.IconPosition.LEFT);
        //        } else {
        mToast.setIcon(R.drawable.shape_transparent, SuperToast.IconPosition.LEFT);
        //        }
        mToast.show();
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
        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
        if (mToolbarShadow != null) {
            mToolbarShadow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showToolbarItem(int id) {
        MenuItem item;

        if (mToolbar != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(true);
            }
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
     *         设置为-1则使用默认进入动画
     * @param animOut
     *         退出动画Id
     *
     *         设置为-1则使用默认退出动画
     */
    @Override
    public final void start(Intent intent, int requestCode, int animIn, int animOut) {
        if (intent != null) {
            hideToast();
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (requestCode < 0) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, requestCode);
            }
            if (animIn <= 0) {
                animIn = R.anim.slide_in_right;
            }
            if (animOut <= 0) {
                animOut = R.anim.zoom_in_exit;
            }
            overridePendingTransition(animIn, animOut);
        }
    }

    public final void start(Intent intent, int animIn, int animOut) {
        start(intent, -1, animIn, animOut);
    }

    public final void start(Intent intent, int requestCode) {
        start(intent, requestCode, -1, -1);
    }

    public final void start(Intent intent) {
        start(intent, -1, -1, -1);
    }
}