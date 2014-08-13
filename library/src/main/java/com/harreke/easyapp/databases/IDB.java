package com.harreke.easyapp.databases;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Sql接口
 *
 * Sql接口使用反射获取对象的Field和Value，通过传入表、键名和键值定位表中的条目
 */
public interface IDB {
    /**
     * 关闭数据库
     */
    public void close();

    /**
     * 创建一张表
     *
     * @param classOfItem
     *         数据模块类
     * @param table
     *         表名
     * @param primaryKey
     *         主键
     * @param <ITEM>
     *         数据模块类型
     *
     * @return 是否执行成功
     */
    public <ITEM> boolean createTable(Class<ITEM> classOfItem, String table, String primaryKey);

    /**
     * 从数据库删除一条数据
     *
     * 查询数据模块所属的表，删除目标键名对应的条目
     *
     * @param table
     *         表名
     * @param key
     *         目标键名
     * @param value
     *         目标键值
     *
     * @return 是否执行成功
     */
    public boolean delete(String table, String key, String value);

    /**
     * 删除一张表
     *
     * @param table
     *         表名
     *
     * @return 是否执行成功
     */
    public boolean dropTable(String table);

    /**
     * 判断一张表是否存在
     *
     * @param table
     *         表名
     *
     * @return 表是否存在
     */
    public boolean hasTable(String table);

    /**
     * 向数据库插入一条数据
     *
     * 查询数据模块所属的表，插入一条数据
     *
     * @param item
     *         数据模块
     * @param table
     *         表名
     * @param primaryKey
     *         主键名
     * @param <ITEM>
     *         数据模块类型
     *
     * @return 是否执行成功
     */
    public <ITEM> boolean insert(ITEM item, String table, String primaryKey);

    /**
     * 从数据库读取一个数据列表
     *
     * @param classOfItem
     *         数据模块类
     * @param table
     *         表名
     * @param key
     *         目标键名
     * @param value
     *         目标键值
     * @param filter
     *         筛选语句
     * @param <ITEM>
     *         数据模块类型
     *
     * @return 是否执行成功
     */
    public <ITEM> ArrayList<ITEM> query(Class<ITEM> classOfItem, String table, String key, String value, String filter);

    /**
     * 原生查询
     *
     * @param sql
     *         Sql语句
     *
     * @return 游标
     */
    public Cursor rawQuery(String sql);

    /**
     * 原生执行读操作
     *
     * @param sql
     *         Sql语句
     *
     * @return 是否执行成功
     */
    public boolean rawReadExecute(String sql);

    /**
     * 原生执行写操作
     *
     * @param sql
     *         Sql语句
     *
     * @return 是否执行成功
     */
    public boolean rawWriteExecute(String sql);

    /**
     * 更新数据库的条目
     *
     * 查询数据模块所属的表，更新目标键名的条目
     *
     * @param item
     *         数据模块
     * @param table
     *         表名
     * @param key
     *         目标键名
     * @param value
     *         目标键值
     * @param <ITEM>
     *         数据模块类型
     *
     * @return 是否执行成功
     */
    public <ITEM> boolean update(ITEM item, String table, String key, String value);
}