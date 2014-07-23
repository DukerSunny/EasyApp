package com.harreke.utils.frameworks.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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

import com.harreke.utils.R;
import com.harreke.utils.beans.ActionBarItem;
import com.harreke.utils.frameworks.IFramework;
import com.harreke.utils.helpers.RequestHelper;
import com.harreke.utils.receivers.ExitReceiver;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.RequestBuilder;
import com.harreke.utils.tools.Debug;
import com.harreke.utils.tools.NetUtil;
import com.harreke.utils.widgets.InfoView;
import com.harreke.utils.widgets.ToastView;

import java.util.ArrayList;

public abstract class ActivityFramework extends FragmentActivity implements IFramework, IActivity, IActionBar {
    private static final String TAG = "Framework_Activity";

    private InfoView framework_info;
    private FrameLayout framework_root;
    private ToastView framework_toast;
    private ActionBar mActionBar;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (framework_info.isShowingRetry()) {
                onInfoClick();
            }
        }
    };
    private ArrayList<ActionBarItem> mItemList = new ArrayList<ActionBarItem>();
    private RequestHelper mRequest = new RequestHelper();

    public final void addActionBarItem(String title, int imageId) {
        addActionBarItem(title, imageId, null, null, -1);
    }

    private void addActionBarItem(String title, int imageId, View view, String[] items, int flag) {
        ActionBarItem item;
        int i;

        if (mActionBar != null) {
            item = new ActionBarItem();
            item.setTitle(title);
            if (imageId > 0) {
                item.setImageId(imageId);
            }
            if (view != null) {
                item.setView(view);
            }
            if (items != null) {
                for (i = 0; i < items.length; i++) {
                    item.addSubItem(items[i]);
                }
            }
            if (flag >= 0) {
                item.setFlag(flag);
            }
            mItemList.add(item);
        }
    }

    public final void addActionBarItem(String title, int imageId, int flag) {
        addActionBarItem(title, imageId, null, null, flag);
    }

    public final void addActionBarItem(String title, View view) {
        addActionBarItem(title, 0, view, null, -1);
    }

    public final void addActionBarItem(String title, View view, int flag) {
        addActionBarItem(title, 0, view, null, flag);
    }

    public final void addActionBarItem(String title, int imageId, String[] itemArray) {
        addActionBarItem(title, imageId, null, itemArray, -1);
    }

    public final void addActionBarItem(String title, int imageId, String[] itemArray, int flag) {
        addActionBarItem(title, imageId, null, itemArray, flag);
    }

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
        mRequest.execute(this, builder, callback);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final FragmentActivity getActivity() {
        return this;
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
    public final FrameLayout getRootView() {
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

    @Override
    public void initConfig() {
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final boolean isRequestExecuting() {
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
     {@inheritDoc}
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

    public final void disableActionBarHome() {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(false);
        }
    }

    public final void disableActionBarHomeUp() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public final void enableActionBarHome() {
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
        }
    }

    public final void enableActionBarHomeUp() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final View findViewById(int viewId) {
        return framework_root.findViewById(viewId);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setContentView(int layoutId) {
        framework_root.removeAllViews();
        View.inflate(this, layoutId, framework_root);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final void setContentView(View view) {
        framework_root.removeAllViews();
        framework_root.addView(view);
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        SubMenu subMenu;
        ActionBarItem actionBarItem;
        int i;
        int j;
        int index = 0;

        if (mActionBar != null) {
            for (i = 0; i < mItemList.size(); i++) {
                actionBarItem = mItemList.get(i);
                if (actionBarItem.getSubItemList() == null) {
                    menuItem = menu.add(0, index, i, actionBarItem.getTitle());
                    index++;
                    if (actionBarItem.getImageId() > -1) {
                        menuItem.setIcon(actionBarItem.getImageId());
                    } else if (actionBarItem.getView() != null) {
                        menuItem.setActionView(actionBarItem.getView());
                    }
                    menuItem.setShowAsAction(actionBarItem.getFlag());
                    menuItem.setVisible(actionBarItem.isVisible());
                } else {
                    subMenu = menu.addSubMenu(0, index, i, actionBarItem.getTitle());
                    index++;
                    for (j = 0; j < actionBarItem.getSubItemList().size(); j++) {
                        subMenu.add(0, index, j, actionBarItem.getSubItemList().get(j));
                        index++;
                    }
                    menuItem = subMenu.getItem();
                    if (actionBarItem.getImageId() > -1) {
                        menuItem.setIcon(actionBarItem.getImageId());
                    } else if (actionBarItem.getView() != null) {
                        menuItem.setActionView(actionBarItem.getView());
                    }
                    menuItem.setShowAsAction(actionBarItem.getFlag());
                    menuItem.setVisible(actionBarItem.isVisible());
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onActionBarHomeClicked();

            return true;
        } else {
            onActionBarItemClicked(item.getItemId(), item);

            return true;
        }
    }

    @Override
    public void onActionBarHomeClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public final void exit() {
        exit(true);
    }

    public final void exit(boolean animate) {
        finish();
        if (animate) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("AppCompatMethod")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout framework;

        registerReceiver(mExitReceiver, new IntentFilter(getPackageName() + ".EXIT"));
        NetUtil.checkConnection(this);

        initConfig();

        framework = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.widget_framework, null);
        framework_root = (FrameLayout) framework.findViewById(R.id.framework_root);
        framework_info = (InfoView) framework.findViewById(R.id.framework_info);
        framework_toast = (ToastView) framework.findViewById(R.id.framework_toast);
        framework_info.setOnClickListener(mInfoClickListener);

        super.setContentView(framework);

        mActionBar = getActionBar();

        initLayout();
        initData(getIntent());
        onMenuCreate();
        initCallback();
        initView();
        initEvent();
        startAction();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mExitReceiver);
        hideToast(false);
        cancelRequest();
        super.onDestroy();
    }

    public final void hideActionBar() {
        if (mActionBar != null) {
            mActionBar.hide();
        }
    }

    public final void hideActionBarAllItem() {
        int i;

        if (mActionBar != null) {
            for (i = 0; i < mItemList.size(); i++) {
                mItemList.get(i).setVisible(false);
            }
        }
    }

    public final void hideActionBarIcon() {
        if (mActionBar != null) {
            mActionBar.setIcon(R.drawable.shape_transparent);
        }
    }

    public final void hideActionBarItem(int position) {
        if (mActionBar != null && position >= 0 && position < mItemList.size()) {
            mItemList.get(position).setVisible(false);
        }
    }

    public final void hideActionBarTitle() {
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public final boolean isActionBarItemShowing(int position) {
        return mActionBar != null && position >= 0 && position < mItemList.size() && mItemList.get(position).isVisible();
    }

    public final boolean isActionBarShowing() {
        return mActionBar != null && mActionBar.isShowing();
    }

    public final void refreshMenu() {
        if (mActionBar != null) {
            invalidateOptionsMenu();
        }
    }

    public final void setActionBarIcon(int imageId) {
        if (mActionBar != null) {
            mActionBar.setIcon(imageId);
        }
    }

    public final void setActionBarTitle(int titleId) {
        if (mActionBar != null) {
            mActionBar.setTitle(titleId);
        }
    }

    public final void setActionBarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    public final void setActionView(int viewId) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(viewId);
        }
    }

    public final void setActionView(View view) {
        if (mActionBar != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setCustomView(view);
        }
    }

    public final void showActionBar() {
        if (mActionBar != null) {
            mActionBar.show();
        }
    }

    public final void showActionBarAllItem() {
        int i;

        if (mActionBar != null) {
            for (i = 0; i < mItemList.size(); i++) {
                mItemList.get(i).setVisible(true);
            }
        }
    }

    public final void showActionBarHome() {
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public final void showActionBarItem(int position) {
        if (mActionBar != null && position >= 0 && position < mItemList.size()) {
            mItemList.get(position).setVisible(true);
        }
    }

    public final void showActionBarTitle() {
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }
}