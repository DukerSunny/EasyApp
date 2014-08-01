package com.harreke.easyappframework.sqls.models;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 数据模块接口
 *
 * Java Bean通过实现此接口，可以与数据库数据互相转化
 */
public interface IModel {
    public String[] getKeys();

    public String getPrimaryKey();

    public String getTable();

    public String[] getValues();

    public String getCreateSql();
}