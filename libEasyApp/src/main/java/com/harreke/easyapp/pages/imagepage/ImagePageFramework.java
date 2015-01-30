package com.harreke.easyapp.pages.imagepage;

import android.util.SparseBooleanArray;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.pages.PageFramework;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public abstract class ImagePageFramework<ITEM> extends PageFramework<ITEM, ImageView> {
    private SparseBooleanArray mLoadedList = new SparseBooleanArray();

    public ImagePageFramework(IFramework framework, int pageId) {
        super(framework, pageId);
    }

    public final boolean isLoaded(int position) {
        return mLoadedList.get(position);
    }

    @Override
    public void onPageDestroy(int position) {
        mLoadedList.put(position, false);
    }

    public final void setLoaded(int position, boolean loaded) {
        mLoadedList.put(position, loaded);
    }
}