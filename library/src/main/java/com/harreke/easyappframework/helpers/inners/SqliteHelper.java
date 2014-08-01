package com.harreke.easyappframework.helpers.inners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harreke.easyappframework.sqls.ISql;
import com.harreke.easyappframework.sqls.models.IModel;
import com.harreke.easyappframework.sqls.models.ModelUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * SqliteOpenHelper的封装
 */
public class SqliteHelper extends SQLiteOpenHelper implements ISql {
    public SqliteHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }

    @Override
    public <ITEM extends IModel> void createTable(ITEM item) {
        rawWriteExecute("create table " + item.getTable() + " ( " + item.getCreateSql() + " ) ");
    }

    @Override
    public <ITEM extends IModel> void delete(ITEM item, String target) {
        rawWriteExecute("delete from " + item.getTable() + " where " + item.getPrimaryKey() + " = " + target);
    }

    @Override
    public <ITEM extends IModel> void dropTable(ITEM item) {
        rawWriteExecute("drop table if exist " + item.getTable());
    }

    @Override
    public <ITEM extends IModel> void insert(ITEM item) {
        rawWriteExecute("insert into " + item.getTable() + " ( " + ModelUtil.joint(item.getKeys()) + " ) values ( " + ModelUtil.joint(item.getValues()) + " )");
    }

    @Override
    public <ITEM extends IModel> ArrayList<ITEM> query(ITEM item, String key, String target, String filter) {
        ArrayList<ITEM> itemList;
        Type type = new TypeToken<ITEM>() {
        }.getType();
        ITEM queryItem;
        Cursor cursor = rawQuery("select * from " + item.getTable() + " where " + key + " = " + target + " " + filter);
        Gson gson;
        String[] keys;
        String json;
        int i;
        int j;

        if (cursor.moveToFirst()) {
            gson = new Gson();
            itemList = new ArrayList<ITEM>(cursor.getCount());
            for (i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);
                keys = cursor.getColumnNames();
                json = "{";
                for (j = 0; j < keys.length - 1; i++) {
                    json += keys[i] + ":\"" + cursor.getString(cursor.getColumnIndex(keys[j])) + "\",";
                }
                json += keys[keys.length - 1] + ":\"" + cursor.getString(cursor.getColumnIndex(keys[keys.length - 1])) + "\"}";
                queryItem = gson.fromJson(json, type);
                if (queryItem != null) {
                    itemList.add(queryItem);
                }
            }
        } else {
            itemList = new ArrayList<ITEM>();
        }

        return itemList;
    }

    @Override
    public Cursor rawQuery(String sql) {
        return getReadableDatabase().rawQuery(sql, null);
    }

    public void rawReadExecute(String sql) {
        getReadableDatabase().execSQL(sql);
    }

    @Override
    public void rawWriteExecute(String sql) {
        getWritableDatabase().execSQL(sql);
    }

    @Override
    public <ITEM extends IModel> void update(ITEM item, String target) {
        rawWriteExecute(
                "update " + item.getTable() + " set " + ModelUtil.joint(item.getKeys(), item.getValues()) + " where " + item.getPrimaryKey() + " = " + target);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}