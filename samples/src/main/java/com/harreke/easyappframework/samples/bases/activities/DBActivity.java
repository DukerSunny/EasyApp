package com.harreke.easyappframework.samples.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.harreke.easyappframework.beans.ActionBarItem;
import com.harreke.easyappframework.databases.DBUtil;
import com.harreke.easyappframework.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyappframework.helpers.DBHelper;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.DBItem;
import com.harreke.easyappframework.tools.GsonUtil;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/04
 *
 * 数据库示例程序
 */
public class DBActivity extends ActivityFramework {
    private DBHelper mDatabase;
    private TextView mTextView;

    public static Intent create(Context context) {
        return new Intent(context, DBActivity.class);
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
        mTextView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void setLayout() {
        setContent(R.layout.activity_db);
    }

    @Override
    public void startAction() {
        ArrayList<DBItem> list;
        ArrayList<DBItem.SubItem> subList;
        DBItem dbItem;
        DBItem.SubItem subItem;
        String table = "DBItems";
        String primaryKey = "mId";
        String text = "";
        int i;
        int j;

        list = new ArrayList<DBItem>(20);
        for (i = 1; i < 21; i++) {
            dbItem = new DBItem();
            dbItem.setId(i);
            dbItem.setTitle("标题" + i);
            dbItem.setDesc("这是第" + i + "条数据的描述");
            subList = new ArrayList<DBItem.SubItem>();
            for (j = 1; j < 3; j++) {
                subItem = new DBItem.SubItem();
                subItem.setSubTitle("标题" + i + " - " + j);
                subItem.setSubDesc("这是第" + i + "条数据的第" + j + "个子数据的描述");
                subList.add(subItem);
            }
            dbItem.setSubList(subList);
            list.add(dbItem);
        }
        mDatabase = new DBHelper(this, "Samples", 1);
        text += "检查表" + table + "是否存在\n\n";
        mTextView.setText(text);
        if (mDatabase.hasTable(table)) {
            /**
             * 如果存在表，就丢弃重建
             */
            text += "表" + table + "存在，丢弃\n\n";
            mTextView.setText(text);
            mDatabase.dropTable(table);
        } else {
            text += "表" + table + "不存在\n\n";
            mTextView.setText(text);
        }
        /**
         * 新建表
         */
        text += "新建表" + table + "\n\n";
        mTextView.setText(text);
        mDatabase.createTable(DBItem.class, table, primaryKey);
        /**
         * 插入20条数据
         */
        text += "插入20条数据\n\n";
        mTextView.setText(text);
        for (i = 0; i < list.size(); i++) {
            text += (i + 1) + "：" + GsonUtil.toString(list.get(i)) + "\n";
            mTextView.setText(text);
            mDatabase.insert(list.get(i), table, primaryKey);
        }
        /**
         * 删除第一条数据
         */
        text += "\n删除第一条数据\n\n";
        mTextView.setText(text);
        mDatabase.delete(table, "mId", String.valueOf(list.get(0).getId()));
        /**
         * 查询表数据
         */
        text += "查询表数据\n\n";
        mTextView.setText(text);
        list = mDatabase.query(DBItem.class, table, null, null, DBUtil.getQueryOrderBySql(primaryKey, false));
        mTextView.setText(text);
        for (i = 0; i < list.size(); i++) {
            dbItem = list.get(i);
            text += (i + 1) + "：" + GsonUtil.toString(dbItem) + "\n";
            mTextView.setText(text);
        }
    }
}