package com.harreke.easyapp.databases.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.harreke.easyapp.databases.DBUtil;
import com.harreke.easyapp.databases.IDB;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * SqliteOpenHelper的封装
 */
public class SQLiteDB extends SQLiteOpenHelper implements IDB {
    private final static String TAG = "SQLiteDB";

    public SQLiteDB(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }

    @Override
    public <ITEM> boolean createTable(Class<ITEM> classOfBean, String table, String primaryKey) {
        String sql = DBUtil.getCreateTableSql(classOfBean, table, primaryKey);

        return sql != null && rawWriteExecute(sql);
    }

    @Override
    public boolean delete(String table, String key, String value) {
        String sql = DBUtil.getDeleteSql(table, key, value);

        return sql != null && rawWriteExecute(sql);
    }

    @Override
    public boolean dropTable(String table) {
        String sql = DBUtil.getDropTableSql(table);

        return sql != null && rawWriteExecute(sql);
    }

    //    @Override
    //    public <ITEM> boolean hasTable(Class<ITEM> classOfBean, String table) {
    //        return false;
    //    }

    @Override
    public boolean hasTable(String table) {
        Cursor cursor;
        String sql = DBUtil.getHasTableSql(table);

        if (sql != null) {
            cursor = rawQuery(sql);
            if (cursor != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <ITEM> boolean insert(ITEM item, String table, String primaryKey) {
        String sql = DBUtil.getInsertSql(item, table, primaryKey);

        return sql != null && rawWriteExecute(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public <ITEM> ArrayList<ITEM> query(Class<ITEM> classOfBean, String table, String key, String value, String filter) {
        ArrayList<ITEM> itemList;
        ITEM queryItem;
        Cursor cursor;
        String sql;
        int i;

        sql = DBUtil.getQuerySql(table, key, value, filter);
        if (sql != null) {
            cursor = rawQuery(sql);
            if (cursor != null && cursor.moveToFirst()) {
                itemList = new ArrayList<ITEM>(cursor.getCount());
                for (i = 0; i < cursor.getCount(); i++) {
                    queryItem = DBUtil.getBean(cursor, classOfBean);
                    if (queryItem != null) {
                        itemList.add(queryItem);
                    }
                    cursor.move(1);
                }
            } else {
                itemList = new ArrayList<ITEM>();
            }
        } else {
            itemList = new ArrayList<ITEM>();
        }

        return itemList;
    }

    @Override
    public Cursor rawQuery(String sql) {
        Cursor cursor;

        try {
            Log.e(TAG, "raw query sql=" + sql);
            cursor = getReadableDatabase().rawQuery(sql, null);
        } catch (SQLException e) {
            Log.e(TAG, "Raw query execute error!");
            cursor = null;
        }

        return cursor;
    }

    public boolean rawReadExecute(String sql) {
        boolean success;

        try {
            Log.e(TAG, "raw read sql=" + sql);
            getReadableDatabase().execSQL(sql);
            success = true;
        } catch (SQLException e) {
            Log.e(TAG, "Raw read execute error!");
            success = false;
        }

        return success;
    }

    @Override
    public boolean rawWriteExecute(String sql) {
        boolean success;

        try {
            Log.e(TAG, "raw write sql=" + sql);
            getWritableDatabase().execSQL(sql);
            success = true;
        } catch (SQLException e) {
            Log.e(TAG, "Raw write execute error!");
            success = false;
        }

        return success;
    }

    @Override
    public <ITEM> boolean update(ITEM item, String table, String key, String value) {
        String sql = DBUtil.getUpdateSql(item, table, key, value);

        return sql != null && rawWriteExecute(sql);
    }
}