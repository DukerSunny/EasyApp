package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.beans.ExItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.ExListFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.entities.beans.ExListChildItem;
import com.harreke.easyapp.samples.entities.beans.ExListGroupItem;
import com.harreke.easyapp.samples.holders.ExListChildHolder;
import com.harreke.easyapp.samples.holders.ExListGroupHolder;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/05
 */
public class ExListActivity extends ActivityFramework {
    private ExList mExList;

    public static Intent create(Context context) {
        return new Intent(context, ExListActivity.class);
    }

    @Override
    public void assignEvents() {

    }

    private ArrayList<ExItem<ExListGroupItem, ExListChildItem>> generateExListItems(int groupSize, int childSize) {
        ArrayList<ExItem<ExListGroupItem, ExListChildItem>> list = new ArrayList<ExItem<ExListGroupItem, ExListChildItem>>(groupSize);
        ExItem<ExListGroupItem, ExListChildItem> item;
        ExListGroupItem groupItem;
        ArrayList<ExListChildItem> childList;
        ExListChildItem childItem;
        int start = (mExList.getCurrentPage() - 1) * 20 + 1;
        int end = mExList.getCurrentPage() * 20 + 1;
        int i;
        int j;

        for (i = start; i < end; i++) {
            groupItem = new ExListGroupItem();
            groupItem.setTag("第" + i + "组");
            groupItem.setChecked(false);
            childList = new ArrayList<ExListChildItem>(childSize);
            for (j = 0; j < 3; j++) {
                childItem = new ExListChildItem();
                childItem.setTitle("第" + i + "组的第" + j + "条数据");
                childItem.setChecked(false);
                childList.add(childItem);
            }
            item = new ExItem<ExListGroupItem, ExListChildItem>();
            item.setGroup(groupItem);
            item.setChildList(childList);
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
        mExList = new ExList(this, R.id.exListView);
        mExList.setLoadEnabled(true);
        mExList.setTotalPage(5);
    }

    private void setGroupChecked(int groupPosition, boolean checked) {
        ExListGroupItem groupItem;
        ArrayList<ExListChildItem> childList;
        ExListChildItem child;
        int i;

        groupItem = mExList.getGroup(groupPosition);
        groupItem.setChecked(checked);
        childList = mExList.getChildList(groupPosition);
        for (i = 0; i < childList.size(); i++) {
            child = childList.get(i);
            child.setChecked(checked);
        }
        mExList.refresh();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_exlist);
    }

    @Override
    public void startAction() {
        mExList.from(generateExListItems(20, 3));
        if (mExList.getItemCount() == 20) {
            setGroupChecked(0, true);
        }
    }

    private class ExList extends ExListFramework<ExListGroupItem, ExListGroupHolder, ExListChildItem, ExListChildHolder> {
        public ExList(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public ExListChildHolder createChildHolder(int groupPosition, int childPosition, View convertView) {
            return new ExListChildHolder(convertView);
        }

        @Override
        public View createChildView(int groupPosition, int childPosition, ExListChildItem childItem) {
            return View.inflate(getActivity(), R.layout.item_exlist_child, null);
        }

        @Override
        public ExListGroupHolder createGroupHolder(int groupPosition, View convertView) {
            return new ExListGroupHolder(convertView);
        }

        @Override
        public View createGroupView(int groupPosition, ExListGroupItem groupItem) {
            return View.inflate(getActivity(), R.layout.item_exlist_group, null);
        }

        @Override
        public boolean isGroupClickable(int groupPosition) {
            return true;
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onChildItemClick(int groupPosition, int childPosition, ExListChildItem childItem) {
            showToast("点击了第" + (groupPosition + 1) + "组第" + (childPosition + 1) + "个条目", false);
        }

        @Override
        public void onGroupItemClick(int groupPosition, ExListGroupItem groupItem) {
            int i;

            showToast("点击了第" + (groupPosition + 1) + "组", false);
            for (i = 0; i < getItemCount(); i++) {
                setGroupChecked(i, false);
            }
            setGroupChecked(groupPosition, true);
        }

        @Override
        public ArrayList<ExItem<ExListGroupItem, ExListChildItem>> onParse(String json) {
            return null;
        }

        @Override
        public void onPostAction() {
            super.onPostAction();

            expandAllGroups();
        }

        @Override
        public int parseItemId(ExItem<ExListGroupItem, ExListChildItem> exItem) {
            return getItemCount();
        }
    }
}