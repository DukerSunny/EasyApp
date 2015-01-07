package com.harreke.easyapp.parsers;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public abstract class ObjectParser<ITEM> {
    private ITEM mObject = null;

    public ITEM getObject() {
        return mObject;
    }

    public abstract void parse(String json);

    protected void setObject(ITEM object) {
        mObject = object;
    }
}
