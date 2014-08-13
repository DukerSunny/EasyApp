package com.harreke.easyapp.holders.abslistview;

import android.view.View;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public class ExListHolder {
    public abstract static class Child<CHILD> implements IExListHolder.Child<CHILD> {
        public Child(View convertView) {
        }
    }

    public abstract static class Group<GROUP> implements IExListHolder.Group<GROUP> {
        public Group(View convertView) {
        }
    }
}