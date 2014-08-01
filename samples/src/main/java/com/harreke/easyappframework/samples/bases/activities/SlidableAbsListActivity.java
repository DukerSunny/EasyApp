package com.harreke.easyappframework.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyappframework.beans.ActionBarItem;
import com.harreke.easyappframework.frameworks.bases.IFramework;
import com.harreke.easyappframework.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyappframework.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.NumberItem;
import com.harreke.easyappframework.samples.entities.loaders.NumberItemLoader;
import com.harreke.easyappframework.samples.holders.NumberItemHolder;
import com.harreke.easyappframework.widgets.InfoView;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 *
 * SlidableView的示例Activity
 */
public class SlidableAbsListActivity extends ActivityFramework {
    private AbsList mAbsList;

    public static Intent create(Context context) {
        return new Intent(context, SlidableAbsListActivity.class);
    }

    @Override
    public void assignEvents() {
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
        mAbsList = new AbsList(this, R.id.absListView, R.id.slidableView);
        mAbsList.setInfoView((InfoView) findViewById(R.id.infoView));
        mAbsList.setRootView(findViewById(R.id.absListView));
        mAbsList.setLoadEnabled(true);
        mAbsList.setTotalPage(10);
    }

    @Override
    public void setLayout() {
        setContent(R.layout.activity_slidable);
    }

    @Override
    public void startAction() {
        ArrayList<NumberItem> list = new ArrayList<NumberItem>(20);
        NumberItem item;
        int i;
        int start = (mAbsList.getCurrentPage() - 1) * 20 + 1;
        int end = mAbsList.getCurrentPage() * 20 + 1;

        for (i = start; i < end; i++) {
            item = new NumberItem();
            item.setNumber(i);
            item.setNumberDesc("这是第" + i + "条数据");
            list.add(item);
        }
        mAbsList.from(list);
    }

    private class AbsList extends AbsListFramework<NumberItem, NumberItemHolder, NumberItemLoader> {
        public AbsList(IFramework framework, int listId, int slidableViewId) {
            super(framework, listId, slidableViewId);
        }

        @Override
        public NumberItemHolder createHolder(int position, View convertView) {
            return new NumberItemHolder(convertView);
        }

        @Override
        public View createView(int position, NumberItem numberItem) {
            return View.inflate(getActivity(), R.layout.item_number, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, NumberItem numberItem) {
            showToast("点击了条目" + position, false);
        }

        @Override
        public void onParseItem(NumberItem item) {
            addItem(getItemCount(), item);
        }
    }
}