package com.harreke.easyapp.frameworks.bases.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;

import com.harreke.easyapp.frameworks.bases.IActionBarClickListener;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.R;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.IActionBar;
import com.harreke.easyapp.helpers.RequestHelper;
import com.harreke.easyapp.receivers.ExitReceiver;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.tools.DevUtil;
import com.harreke.easyapp.tools.NetUtil;
import com.harreke.easyapp.widgets.InfoView;
import com.harreke.easyapp.widgets.ToastView;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Activity框架
 */
public abstract class ActivityFramework extends FragmentActivity implements IFramework, IActivity, IActionBar, IActionBarClickListener {
    private static final String TAG = "ActivityFramework";
    private FrameLayout framework_content;
    private InfoView framework_info;
    private ToastView framework_toast;
    private ActionBar mActionBar;
    private ArrayList<ActionBarItem> mActionBarItemList = new ArrayList<ActionBarItem>();
    private boolean mCreated = false;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (framework_info.isShowingRetry()) {
                onInfoClick();
            }
        }
    };
    private RequestHelper mRequest = new RequestHelper();

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项图片Id
     */
    @Override
    public void addActionBarItem(String title, int imageId) {
        addActionBarItem(title, imageId, null, null, 0);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项图片Id
     * @param flag
     *         选项的Flag
     */
    @Override
    public void addActionBarItem(String title, int imageId, int flag) {
        addActionBarItem(title, imageId, null, null, flag);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     */
    @Override
    public void addActionBarItem(String title, View view) {
        addActionBarItem(title, 0, view, null, 0);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     * @param flag
     *         选项的Flag
     */
    @Override
    public void addActionBarItem(String title, View view, int flag) {
        addActionBarItem(title, 0, view, null, flag);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     * @param items
     *         选项的内容数组
     */
    @Override
    public void addActionBarItem(String title, View view, String[] items) {
        addActionBarItem(title, 0, view, items, 0);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param view
     *         选项的视图
     * @param items
     *         选项的内容数组
     * @param flag
     *         选项的Flag
     */
    @Override
    public void addActionBarItem(String title, View view, String[] items, int flag) {
        addActionBarItem(title, 0, view, items, flag);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     * @param items
     *         选项的内容数组
     */
    @Override
    public void addActionBarItem(String title, int imageId, String[] items) {
        addActionBarItem(title, imageId, null, items, 0);
    }

    /**
     * 为ActionBar添加一个菜单选项
     *
     * @param title
     *         选项标题
     * @param imageId
     *         选项的图标Id
     * @param items
     *         选项的内容数组
     * @param flag
     *         选项的Flag
     */
    @Override
    public void addActionBarItem(String title, int imageId, String[] items, int flag) {
        addActionBarItem(title, imageId, null, items, flag);
    }

    private void addActionBarItem(String title, int imageId, View view, String[] items, int flag) {
        ActionBarItem actionBarItem;
        ActionBarItem subItem;
        int index;
        int i;

        if (mActionBar != null) {
            index = mActionBarItemList.size();
            actionBarItem = new ActionBarItem(title, index);
            actionBarItem.setImageId(imageId);
            actionBarItem.setView(view);
            actionBarItem.setFlag(flag);
            if (items != null) {
                index++;
                for (i = 0; i < items.length; i++) {
                    subItem = new ActionBarItem(title, index);
                    mActionBarItemList.add(subItem);
                }
                actionBarItem.setSubItemCount(items.length);
            }
            mActionBarItemList.add(actionBarItem);
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
    public void addContentView(View view, FrameLayout.LayoutParams params) {
        framework_content.addView(view, params);
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

    /**
     * 输出调试信息
     *
     * @param message
     *         调试信息
     */
    @Override
    public void debug(String message) {
        DevUtil.e(TAG, message);
    }

    /**
     * 禁用ActionBar的Home键上的图标
     */
    @Override
    public void disableActionBarHomeIcon() {
        if (mActionBar != null) {
            mActionBar.setIcon(R.drawable.shape_transparent);
        }
    }

    /**
     * 启用ActionBar的Home键左边的返回箭头
     */
    @Override
    public void enableActionBarHomeBack() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
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
        finish();
        if (animate) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    /**
     * 隐藏ActoinBar上的指定菜单选项
     */
    @Override
    public final void hideActionBarItem(int position) {
        if (mActionBar != null && position >= 0 && position < mActionBarItemList.size()) {
            mActionBarItemList.get(position).setVisible(false);
        }
    }

    /**
     * 隐藏Toast
     *
     * @param animate
     *         是否显示动画
     */
    @Override
    public final void hideToast(boolean animate) {
        framework_toast.hide(animate);
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final void hideToast() {
        framework_toast.hide();
    }

    /**
     * 判断ActionBar上的某个菜单选项是否正在显示
     *
     * @param position
     *         菜单选项位置
     */
    @Override
    public final boolean isActionBarItemShowing(int position) {
        return mActionBar != null && position >= 0 && position < mActionBarItemList.size() && mActionBarItemList.get(position).isVisible();
    }

    @Override
    public final boolean isActionBarShowing() {
        return mActionBar != null && mActionBar.isShowing();
    }

    /**
     * 隐藏Toast
     */
    @Override
    public final boolean isRequestExecuting() {
        return mRequest.isExecuting();
    }

    /**
     * 当ActionBar的Home键被点击时触发
     */
    @Override
    public void onActionBarHomeClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout framework;

        registerReceiver(mExitReceiver, new IntentFilter(getPackageName() + ".EXIT"));
        NetUtil.checkConnection(this);

        configActivity();

        framework = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.widget_framework, null);
        framework_content = (FrameLayout) framework.findViewById(R.id.framework_content);
        framework_info = (InfoView) framework.findViewById(R.id.framework_info);
        framework_toast = (ToastView) framework.findViewById(R.id.framework_toast);
        framework_info.setOnClickListener(mInfoClickListener);

        super.setContentView(framework);

        mActionBar = getActionBar();

        setLayout();
        initData(getIntent());
        onActionBarMenuCreate();
        queryLayout();
        newEvents();
        assignEvents();
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        SubMenu subMenu;
        ActionBarItem actionBarItem;
        ActionBarItem subItem;
        int i;
        int index = 0;

        if (mActionBar != null) {
            while (index < mActionBarItemList.size()) {
                actionBarItem = mActionBarItemList.get(index);
                if (actionBarItem.getSubItemCount() == 0) {
                    menuItem = menu.add(0, index, index, actionBarItem.getTitle());
                    index++;
                } else {
                    subMenu = menu.addSubMenu(0, index, index, actionBarItem.getTitle());
                    for (i = 0; i < actionBarItem.getSubItemCount(); i++) {
                        subItem = mActionBarItemList.get(index + i);
                        subMenu.add(0, index + i, i, subItem.getTitle());
                    }
                    menuItem = subMenu.getItem();
                    index += actionBarItem.getSubItemCount() + 1;
                }
                if (actionBarItem.getImageId() > 0) {
                    menuItem.setIcon(actionBarItem.getImageId());
                } else if (actionBarItem.getView() != null) {
                    menuItem.setActionView(actionBarItem.getView());
                }
                if (actionBarItem.getFlag() > -1) {
                    menuItem.setShowAsAction(actionBarItem.getFlag());
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mExitReceiver);
        hideToast(false);
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
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onActionBarHomeClick();
        } else {
            onActionBarItemClick(item.getItemId(), mActionBarItemList.get(item.getItemId()));
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mCreated) {
            mCreated = true;
            startAction();
        }
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
    public final View queryContent(int viewId) {
        return framework_content.findViewById(viewId);
    }

    /**
     * 刷新ActionBar上的菜单选项
     */
    @Override
    public final void refreshActionBarItems() {
        if (mActionBar != null) {
            invalidateOptionsMenu();
        }
    }

    /**
     * 设置ActionBar的Home键是否可点击
     *
     * @param clickable
     *         是否可点击
     */
    @Override
    public void setActionBarHomeClickable(boolean clickable) {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(clickable);
        }
    }

    /**
     * 设置ActionBar的Home键上的图标
     *
     * @param imageId
     *         图标Id
     */
    @Override
    public final void setActionBarHomeIcon(int imageId) {
        if (mActionBar != null) {
            mActionBar.setIcon(imageId);
        }
    }

    /**
     * 设置ActionBar的Home键上的标题
     *
     * @param titleId
     *         标题
     */
    @Override
    public final void setActionBarHomeTitle(int titleId) {
        if (mActionBar != null) {
            mActionBar.setTitle(titleId);
        }
    }

    /**
     * 设置ActionBar的Home键上的标题
     *
     * @param title
     *         标题
     */
    @Override
    public final void setActionBarHomeTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    /**
     * 设置ActionBar的Home键上的标题是否可见
     *
     * @param visible
     *         是否可见
     */
    @Override
    public void setActionBarHomeTitleVisible(boolean visible) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(visible);
        }
    }

    /**
     * 设置ActionBar的Home键是否可见
     *
     * @param visible
     *         是否可见
     */
    @Override
    public void setActionBarHomeVisible(boolean visible) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(visible);
        }
    }

    /**
     * 设置ActionBar上的所有菜单选项是否可见
     *
     * @param visible
     *         是否可见
     */
    @Override
    public void setActionBarItemsVisible(boolean visible) {
        int i;

        if (mActionBar != null) {
            for (i = 0; i < mActionBarItemList.size(); i++) {
                mActionBarItemList.get(i).setVisible(visible);
            }
        }
    }

    /**
     * 设置ActionBar的视图
     *
     * @param view
     *         ActionBar的视图
     */
    @Override
    public final void setActionBarView(View view) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(view);
        }
    }

    /**
     * 设置ActionBar的视图
     *
     * @param viewId
     *         ActionBar的视图Id
     */
    @Override
    public final void setActionBarView(int viewId) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(viewId);
        }
    }

    /**
     * 设置ActionBar是否可见
     *
     * @param visible
     *         是否可见
     */
    @Override
    public void setActionBarVisible(boolean visible) {
        if (mActionBar != null) {
            if (visible) {
                mActionBar.show();
            } else {
                mActionBar.hide();
            }
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

    /**
     * 显示ActionBar上的指定菜单选项
     *
     * @param position
     *         菜单选项位置
     */
    @Override
    public final void showActionBarItem(int position) {
        if (mActionBar != null && position >= 0 && position < mActionBarItemList.size()) {
            mActionBarItemList.get(position).setVisible(true);
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
        framework_toast.show(text, false);
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
        framework_toast.show(text, progress);
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
            hideToast(false);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            if (animate) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
            hideToast(false);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, requestCode);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}