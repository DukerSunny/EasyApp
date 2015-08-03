package com.harreke.easyapp.parsers;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class ObjectResult<ITEM> {
    private int flag = -1;
    private ITEM mObject = null;
    private String message = "";

    public int getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public ITEM getObject() {
        return mObject;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(ITEM object) {
        mObject = object;
    }
}
