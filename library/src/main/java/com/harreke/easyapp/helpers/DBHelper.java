package com.harreke.easyapp.helpers;

import android.content.Context;
import android.database.Cursor;

import com.harreke.easyapp.databases.IDB;
import com.harreke.easyapp.configs.DBConfig;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * SqliteOpenHelper的封装
 */
public class DBHelper {
    private IDB mDB;

    public DBHelper(Context context, String dbName, int version) {
        mDB = DBConfig.create(context, dbName, version);
    }

    public <ITEM> boolean createTable(Class<ITEM> classOfBean, String table, String primaryKey) {
        return mDB.createTable(classOfBean, table, primaryKey);
    }

    public boolean delete(String table, String key, String value) {
        return mDB.delete(table, key, value);
    }

    public boolean dropTable(String table) {
        return mDB.dropTable(table);
    }

    public boolean hasTable(String table) {
        return mDB.hasTable(table);
    }

    public <ITEM> boolean insert(ITEM item, String table, String primaryKey) {
        return mDB.insert(item, table, primaryKey);
    }

    public <ITEM> ArrayList<ITEM> query(Class<ITEM> classOfBean, String table, String key, String value, String filter) {
        return mDB.query(classOfBean, table, key, value, filter);
    }

    public Cursor rawQuery(String sql) {
        return mDB.rawQuery(sql);
    }

    public boolean rawReadExecute(String sql) {
        return mDB.rawReadExecute(sql);
    }

    public boolean rawWriteExecute(String sql) {
        return mDB.rawWriteExecute(sql);
    }

    public <ITEM> boolean update(ITEM item, String table, String key, String value) {
        return mDB.update(item, table, key, value);
    }
}