package com.harreke.easyapp.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.recyclerview.RecyclerFramework;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.bases.application.Samples;
import com.harreke.easyapp.samples.entities.beans.RecyclerItem;
import com.harreke.easyapp.samples.holders.ImageRecyclerHolder;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
@Deprecated
public class ImageRecyclerActivity extends ActivityFramework {
    private Recycler mRecycler;

    public static Intent create(Context context) {
        return new Intent(context, ImageRecyclerActivity.class);
    }

    @Override
    public void assignEvents() {
    }

    private ArrayList<RecyclerItem> generateRecyclerItems(int size) {
        ArrayList<RecyclerItem> list = new ArrayList<RecyclerItem>(size);
        RecyclerItem item;
        int i;
        int start = (mRecycler.getCurrentPage() - 1) * size + 1;
        int end = mRecycler.getCurrentPage() * size + 1;

        for (i = start; i < end; i++) {
            item = new RecyclerItem();
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
        mRecycler = new Recycler(this, R.id.recyclerView);
        mRecycler.setLoadEnabled(true);
        mRecycler.setTotalPage(5);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_image_recycler);
    }

    @Override
    public void startAction() {
        mRecycler.from(generateRecyclerItems(20));
    }

    private class Recycler extends RecyclerFramework<RecyclerItem, ImageRecyclerHolder> {
        public Recycler(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public ImageRecyclerHolder createHolder(int position, View convertView) {
            return new ImageRecyclerHolder(convertView);
        }

        @Override
        public View createView(int position, RecyclerItem absListItem) {
            return View.inflate(getActivity(), R.layout.item_image_recycler, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public ArrayList<RecyclerItem> onParse(String json) {
            return null;
        }

        @Override
        public void onItemClick(int position) {
            showToast("点击了第" + (position + 1) + "个条目", false);
        }

        @Override
        public int parseItemId(RecyclerItem absListItem) {
            return absListItem.getId();
        }
    }
}