package com.harreke.utils.beans;

import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class ActionBarItem {
    private int mFlag;
    private int mImageId;
    private ArrayList<String> mSubItemList;
    private String mTitle;
    private View mView;
    private boolean mVisible;

    public ActionBarItem() {
        mFlag = MenuItem.SHOW_AS_ACTION_ALWAYS;
        mImageId = -1;
        mSubItemList = new ArrayList<String>();
        mTitle = "";
        mView = null;
        mVisible = true;
    }

    public final void addSubItem(String subItem) {
        mSubItemList.add(subItem);
    }

    public final int getFlag() {
        return mFlag;
    }

    public final void setFlag(int flag) {
        mFlag = flag;
    }

    public final int getImageId() {
        return mImageId;
    }

    public final void setImageId(int imageId) {
        mImageId = imageId;
    }

    public final ArrayList<String> getSubItemList() {
        return mSubItemList;
    }

    public final String getTitle() {
        return mTitle;
    }

    public final void setTitle(String title) {
        mTitle = title;
    }

    public final View getView() {
        return mView;
    }

    public final void setView(View view) {
        mView = view;
    }

    public final boolean isVisible() {
        return mVisible;
    }

    public final void setVisible(boolean visible) {
        mVisible = visible;
    }
}