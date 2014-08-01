package com.harreke.easyappframework.adapters.viewpager;

import android.util.SparseBooleanArray;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ImagePage的Adapter
 *
 * @param <VIEW>
 *         Image的类型
 */
public abstract class ImagePageAdapter<VIEW extends ImageView> extends PageAdapter<String, VIEW> {
    private SparseBooleanArray loadedList = new SparseBooleanArray();

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        loadedList.put(position, false);
    }

    public final boolean isLoaded(int position) {
        return loadedList.get(position);
    }

    public final void setLoaded(int position, boolean loaded) {
        loadedList.put(position, loaded);
    }
}