package com.harreke.easyappframework.databases;

import android.database.Cursor;

import com.harreke.easyappframework.tools.DevUtil;
import com.harreke.easyappframework.tools.GsonUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/25
 */
public class DBUtil {
    private static String convertValue(Field field, Object item, String primaryKey) throws IllegalAccessException {
        String convert;

        convert = GsonUtil.toString(field.get(item));
        if (primaryKey.equals(field.getName())) {
            return convert;
        } else {
            return "'" + convert + "'";
        }
    }

    public static <ITEM> ITEM getBean(Cursor cursor, Class<ITEM> classOfBean) {
        String json;
        String[] keys;
        int i;

        keys = cursor.getColumnNames();
        json = "{";
        for (i = 0; i < keys.length - 1; i++) {
            json += "\"" + keys[i] + "\":" + cursor.getString(i) + ",";
        }
        json += "\"" + keys[keys.length - 1] + "\":" + cursor.getString(keys.length - 1) + "}";
        DevUtil.e("json=" + json);

        return GsonUtil.toBean(json, classOfBean);
    }

    public static <ITEM> String getCreateTableSql(Class<ITEM> classOfBean, String table, String primaryKey) {
        ArrayList<Field> fieldList;
        String sql = null;
        String name;
        int i;

        if (classOfBean != null && table != null && primaryKey != null) {
            fieldList = getFields(classOfBean);
            if (fieldList.size() > 0) {
                sql = "create table if not exists " + table + " ( ";
                for (i = 0; i < fieldList.size() - 1; i++) {
                    name = fieldList.get(i).getName();
                    if (primaryKey.equals(name)) {
                        name += " int primary key , ";
                    } else {
                        name += " varchar , ";
                    }
                    sql += name;
                }
                name = fieldList.get(fieldList.size() - 1).getName();
                if (primaryKey.equals(name)) {
                    name += " int primary key )";
                } else {
                    name += " varchar )";
                }
                sql += name;
            }
        }

        return sql;
    }

    public static String getDeleteSql(String table, String key, String value) {
        if (table != null && key != null && value != null) {
            return "delete from " + table + " where " + key + " = " + value;
        } else {
            return null;
        }
    }

    public static String getDropTableSql(String table) {
        if (table != null) {
            return "drop table if exists " + table;
        } else {
            return null;
        }
    }

    private static ArrayList<Field> getFields(Class<?> classOfBean) {
        Class<?> clazz;
        Field[] fields;
        ArrayList<Field> fieldList;
        int i;

        if (classOfBean != null) {
            clazz = classOfBean;
            fieldList = new ArrayList<Field>();
            while (true) {
                fields = clazz.getDeclaredFields();
                for (i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fieldList.add(fields[i]);
                }
                if (clazz.getSuperclass() != null) {
                    clazz = clazz.getSuperclass();
                } else {
                    break;
                }
            }

            return fieldList;
        } else {
            return null;
        }
    }

    public static String getHasTableSql(String table) {
        if (table != null) {
            return "select * from " + table;
        } else {
            return null;
        }
    }

    public static <ITEM> String getInsertSql(ITEM item, String table, String primaryKey) {
        ArrayList<Field> fieldList;

        if (item != null && table != null) {
            fieldList = getFields(item.getClass());

            return "insert into " + table + " ( " + jointNames(fieldList) + " ) values (" + jointValues(fieldList, item, primaryKey) + " )";
        } else {
            return null;
        }
    }

    public static String getQueryOrderBySql(String key, boolean reverse) {
        String sql;

        if (key != null) {
            sql = "order by " + key;
            if (reverse) {
                sql += " desc";
            } else {
                sql += " asc";
            }

            return sql;
        } else {
            return null;
        }
    }

    public static String getQuerySql(String table, String key, String value, String filter) {
        if (table != null) {
            if (key == null || value == null) {
                key = "";
                value = "";
            } else {
                key = " where " + key;
                value = " = " + value;
            }
            if (filter == null) {
                filter = "";
            } else {
                filter = " " + filter;
            }

            return "select * from " + table + key + value + filter;
        } else {
            return null;
        }
    }

    public static <ITEM> String getUpdateSql(ITEM item, String table, String key, String value) {
        String result;

        if (item != null && table != null && key != null && value != null) {
            result = jointNamesAndValues(getFields(item.getClass()), item);
            if (result != null) {
                return "update " + table + " set " + result + " where " + key + " = " + value;
            }
        }

        return null;
    }

    private static String jointNames(ArrayList<Field> fieldList) {
        String names = null;
        int i;

        if (fieldList != null && fieldList.size() > 0) {
            names = "";
            for (i = 0; i < fieldList.size() - 1; i++) {
                names += fieldList.get(i).getName() + " , ";
            }
            names += fieldList.get(fieldList.size() - 1).getName();
        }

        return names;
    }

    private static String jointNamesAndValues(ArrayList<Field> fieldList, Object item) {
        String result = null;
        int i;

        if (fieldList != null && fieldList.size() > 0 && item != null) {
            try {
                result = "";
                for (i = 0; i < fieldList.size() - 1; i++) {
                    result += fieldList.get(i).getName() + " = '" + fieldList.get(i).get(item) + "' , ";
                }
                result += fieldList.get(fieldList.size() - 1).getName() + " = '" + fieldList.get(fieldList.size() - 1).get(item) + "'";
            } catch (IllegalAccessException e) {
                result = null;
            }
        }

        return result;
    }

    private static String jointValues(ArrayList<Field> fieldList, Object item, String primaryKey) {
        String values = null;
        int size;
        int i;

        if (fieldList != null && fieldList.size() > 0 && item != null && primaryKey != null) {
            try {
                values = "";
                size = fieldList.size();
                for (i = 0; i < size - 1; i++) {
                    values += convertValue(fieldList.get(i), item, primaryKey) + " , ";
                }
                values += convertValue(fieldList.get(size - 1), item, primaryKey);
            } catch (IllegalAccessException e) {
                values = null;
            }
        }

        return values;
    }
}