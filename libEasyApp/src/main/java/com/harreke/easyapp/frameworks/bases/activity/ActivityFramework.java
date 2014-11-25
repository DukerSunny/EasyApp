package com.harreke.easyapp.frameworks.bases.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.receivers.ExitReceiver;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.tools.NetUtil;
import com.harreke.easyapp.widgets.InfoView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架
 */
public abstract class ActivityFramework extends ActionBarActivity
        implements IFramework, IActivity, IToolbar, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "ActivityFramework";
    private FrameLayout framework_content;
    private InfoView framework_info;
    private boolean mCreated = false;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private Menu mMenu;
    private View.OnClickListener mOnInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (framework_info.isShowingRetry()) {
                onInfoClick();
            }
        }
    };
    private View.OnClickListener mOnNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onToolbarNavigationClick();
        }
    };
    private RequestHelper mRequest = new RequestHelper();
    private SuperActivityToast mToast;
    private Toolbar mToolbar;
    private ToolbarMode mToolbarMode = ToolbarMode.Linear;
    private View mToolbarShadow;

    /**
     * 布局新增视图
     *
     * @param view
     *         视图
     * @param params
     *         布局参数
     */
    @Override
    public void addContentView(View view, FrameLayout.LayoutParams params) {
        framework_content.addView(view, params);
    }

    @Override
    public void addToolbarItem(int id, int titleId, int imageId) {
        MenuItem item = mMenu.add(0, id, id, titleId);

        item.setIcon(imageId);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public void addToolbarViewItem(int id, int titleId, View view) {
        MenuItem item = mMenu.add(0, id, id, titleId);

        item.setActionView(view);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    public void bindToolbar(int toolbarSolidId, int toolbarShadowId) {
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
    @Override
    public void configActivity() {
    }

    @Override
    public void createMenu() {
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
        super.onBackPressed();
        if (animate) {
            overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_in_exit);
        }
    }

    /**
     * 获得当前Activity
     *
     * @return 当前Activity
     */
    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 获得内容层视图
     *
     * 框架拥有两层视图，内容层和消息层
     * 内容层为xml文件中编写的实际布局内容
     *
     * @return 内容层视图
     */
    @Override
    public final FrameLayout getContent() {
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
        mMenu.findItem(id).setVisible(false);
    }

    private void initRoot() {
        View root;

        switch (mToolbarMode) {
            case None:
                root = View.inflate(this, R.layout.widget_framework, null);
                break;
            case Overlay:
                root = View.inflate(this, R.layout.widget_framework_toolbar_overlay, null);
                break;
            default:
                root = View.inflate(this, R.layout.widget_framework_toolbar_linear, null);
        }
        framework_content = (FrameLayout) root.findViewById(R.id.framework_content);
        framework_info = (InfoView) root.findViewById(R.id.framework_info);
        framework_info.setOnClickListener(mOnInfoClickListener);
        super.setContentView(root);
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

    public boolean isToolbarShowing() {
        return mToolbar != null && mToolbar.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(mExitReceiver, new IntentFilter(getPackageName() + ".EXIT"));
        NetUtil.checkConnection(this);

        configActivity();
        initRoot();
        bindToolbar(R.id.toolbar_solid, R.id.toolbar_shadow);
        initToast();
        setLayout();
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
     *         布局id
     */
    @Override
    public final void setContentView(int layoutId) {
        framework_content.removeAllViews();
        View.inflate(this, layoutId, framework_content);
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

    public void setToolbarMode(ToolbarMode toolbarMode) {
        mToolbarMode = toolbarMode;
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
        if (progress) {
            mToast.setIcon(R.drawable.anim_progress_radiant, SuperToast.IconPosition.LEFT);
        } else {
            mToast.setIcon(R.drawable.shape_transparent, SuperToast.IconPosition.LEFT);
        }
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
        mMenu.findItem(id).setVisible(true);
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
        if (intent != null) {
            hideToast();
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            if (animate) {
                overridePendingTransition(R.anim.zoom_in_enter, R.anim.zoom_out_exit);
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
    @Override
    public final void start(Intent intent, int requestCode) {
        if (intent != null) {
            hideToast();
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, requestCode);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public enum ToolbarMode {
        None, Linear, Overlay
    }
}