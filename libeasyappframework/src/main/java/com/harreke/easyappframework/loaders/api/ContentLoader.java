package com.harreke.easyappframework.loaders.api;

import com.harreke.easyappframework.beans.Content;
import com.harreke.easyappframework.beans.Page;
import com.harreke.easyappframework.loaders.Loader;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * dummy
 */
public class ContentLoader extends Loader<Content, Page<Content>> {
    @Override
    public void parse() {
        Page<Content> recommmendPage;
        Page<Content> page;
        int i;

        recommmendPage = data.get("recommmendPage");
        if (recommmendPage != null && recommmendPage.getList() != null) {
            mList = recommmendPage.getList();
            for (i = 0; i < mList.size(); i++) {
                mList.get(i).setTopLevel(1);
            }
        } else {
            mList = new ArrayList<Content>();
        }
        page = data.get("page");
        if (page != null && page.getList() != null) {
            mTotalPage = (page.getTotalCount() - 1) / page.getPageSize() + 1;
            mList.addAll(page.getList());
        }
        if (mList.size() > 0 && mTotalPage == 0) {
            mTotalPage = 1;
        }
    }
}