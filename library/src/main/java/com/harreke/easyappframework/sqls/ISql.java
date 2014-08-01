package com.harreke.easyappframework.sqls;

import android.database.Cursor;

import com.harreke.easyappframework.sqls.models.IModel;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * Sql接口
 *
 * Sql接口使用IModel接口获取对应的表、主键和Sql语句，通过传入目标键值定位表中的条目
 *
 * @see com.harreke.easyappframework.sqls.models.IModel
 */
public interface ISql {
    /**
     * 关闭数据库
     */
    public void close();

    /**
     * 创建一张表
     *
     * @param item
     *         数据模块
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> void createTable(ITEM item);

    /**
     * 从数据库删除一条数据
     *
     * 查询数据模块所属的表，删除目标主键值对应的条目
     *
     * @param item
     *         数据模块
     * @param target
     *         目标主键值
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> void delete(ITEM item, String target);

    /**
     * 删除一张表
     *
     * @param item
     *         数据模块
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> void dropTable(ITEM item);

    /**
     * 向数据库插入一条数据
     *
     * 查询数据模块所属的表，插入一条数据
     *
     * @param item
     *         数据模块
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> void insert(ITEM item);

    /**
     * 从数据库读取一个数据列表
     *
     * @param item
     *         数据模块
     * @param key
     *         目标键
     * @param target
     *         目标键值
     * @param filter
     *         筛选语句
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> ArrayList<ITEM> query(ITEM item, String key, String target, String filter);

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
     */
    public void rawReadExecute(String sql);

    /**
     * 原生执行写操作
     *
     * @param sql
     *         Sql语句
     */
    public void rawWriteExecute(String sql);

    /**
     * 更新数据库的条目
     *
     * 查询数据模块所属的表，更新目标主键值的条目
     *
     * @param item
     *         数据模块
     * @param target
     *         目标主键值
     * @param <ITEM>
     *         数据模块类型
     */
    public <ITEM extends IModel> void update(ITEM item, String target);
}