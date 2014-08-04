package com.harreke.easyappframework.samples.entities.loaders;

import com.harreke.easyappframework.loaders.Loader;
import com.harreke.easyappframework.samples.entities.beans.AbsListItem;
import com.harreke.easyappframework.samples.entities.beans.Page;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class AbsListItemLoader extends Loader<AbsListItem, Page<AbsListItem>> {
    @Override
    public void parse() {
        Page<AbsListItem> page = getData("page");

        if (page != null) {
            setList(page.getList());
        }
    }
}