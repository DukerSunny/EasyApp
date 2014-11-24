package com.harreke.easyapp.configs;

import android.content.Context;

import com.harreke.easyapp.databases.IDB;
import com.harreke.easyapp.databases.sqlite.SQLiteDB;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/04
 */
public class DBConfig {
    /**
     * 创建一个数据库助手
     *
     * 默认使用SQLite数据库
     * 若需要使用其他数据库，请重写该函数
     *
     * @param context
     *         Context
     * @param databaseName
     *         数据库名称
     * @param version
     *         数据库版本
     *
     * @return 数据库助手
     */
    public static IDB create(Context context, String databaseName, int version) {
        return new SQLiteDB(context, databaseName, version);
    }
}