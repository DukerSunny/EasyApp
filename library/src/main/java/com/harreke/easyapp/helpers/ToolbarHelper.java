package com.harreke.easyapp.helpers;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.frameworks.base.IToolbar;
import com.harreke.easyapp.utils.ViewUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/09
 */
public class ToolbarHelper implements IToolbar {
    private Menu mMenu;
    private Toolbar mToolbar;

    public ToolbarHelper(ActivityFramework activity, int toolbarSolidId) {
        mToolbar = (Toolbar) activity.findViewById(toolbarSolidId);
        if (mToolbar != null) {
            activity.setSupportActionBar(mToolbar);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void enableDefaultToolbarNavigation() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void hideToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void hideToolbarItem(int id) {
        MenuItem item;

        if (mMenu != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(false);
            }
        }
    }

    public void inflate(int menuId) {
        if (mToolbar != null && menuId > 0) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(menuId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isToolbarShowing() {
        return mToolbar != null && mToolbar.getVisibility() == View.VISIBLE;
    }

    @Override
    public void patchToolbarTopPadding() {
        if (mToolbar != null) {
            ViewUtil.patchTopPadding(mToolbar);
        }
    }

    public final void setMenu(Menu menu) {
        mMenu = menu;
    }

    public final void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        if (mToolbar != null) {
            mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

    public final void setOnNavigationClickListener(View.OnClickListener onNavigationClickListener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(onNavigationClickListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setToolbarNavigation(int imageId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(imageId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setToolbarSubTitle(int titleId) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(titleId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setToolbarSubTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setSubtitle(title);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setToolbarTitle(int textId) {
        if (mToolbar != null) {
            mToolbar.setTitle(textId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setToolbarTitle(String text) {
        if (mToolbar != null) {
            mToolbar.setTitle(text);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void showToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void showToolbarItem(int id) {
        MenuItem item;

        if (mMenu != null) {
            item = mMenu.findItem(id);
            if (item != null) {
                item.setVisible(true);
            }
        }
    }
}