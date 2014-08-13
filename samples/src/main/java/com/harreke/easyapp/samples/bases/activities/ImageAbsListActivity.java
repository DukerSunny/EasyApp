package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.bases.application.Samples;
import com.harreke.easyapp.samples.entities.beans.AbsListItem;
import com.harreke.easyapp.samples.holders.ImageAbsListHolder;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
public class ImageAbsListActivity extends ActivityFramework {
    private AbsList mAbsList;

    public static Intent create(Context context) {
        return new Intent(context, ImageAbsListActivity.class);
    }

    @Override
    public void assignEvents() {
    }

    private ArrayList<AbsListItem> generateAbsListItems(int size) {
        ArrayList<AbsListItem> list = new ArrayList<AbsListItem>(size);
        AbsListItem item;
        int i;
        int start = (mAbsList.getCurrentPage() - 1) * size + 1;
        int end = mAbsList.getCurrentPage() * size + 1;

        for (i = start; i < end; i++) {
            item = new AbsListItem();
            item.setId(i);
            item.setTitle("标题" + i);
            item.setDesc("这是第" + i + "条数据的描述");
            item.setImage("/" + Samples.DIR_ASSETS + "/placeholder_4x3.png");
            list.add(item);
        }

        return list;
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {
    }

    @Override
    public void queryLayout() {
        mAbsList = new AbsList(this, R.id.absListView, 0);
        mAbsList.setLoadEnabled(true);
        mAbsList.setTotalPage(5);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_image_abslist);
    }

    @Override
    public void startAction() {
        mAbsList.from(generateAbsListItems(20));
    }

    private class AbsList extends AbsListFramework<AbsListItem, ImageAbsListHolder> {
        public AbsList(IFramework framework, int listId, int slidableViewId) {
            super(framework, listId, slidableViewId);
        }

        @Override
        public ImageAbsListHolder createHolder(int position, View convertView) {
            return new ImageAbsListHolder(convertView);
        }

        @Override
        public View createView(int position, AbsListItem absListItem) {
            return View.inflate(getActivity(), R.layout.item_image_abslist, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, AbsListItem absListItem) {
            showToast("点击了第" + (position + 1) + "个条目", false);
        }

        @Override
        public int parseItemId(AbsListItem absListItem) {
            return absListItem.getId();
        }
    }
}