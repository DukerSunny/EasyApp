package com.harreke.easyapp.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import com.harreke.easyapp.adapters.abslistview.AbsListAdapter;
import com.harreke.easyapp.frameworks.lists.abslistview.IAbsList;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 自定义样式的的PopupList
 *
 * @param <ITEM>
 *         列表条目类型
 * @param <HOLDER>
 *         列表条目容器类型
 */
public abstract class PopupAbsListHelper<ITEM, HOLDER extends IAbsListHolder<ITEM>> implements IAbsList<ITEM, HOLDER> {
    private Adapter mAdapter;
    private ListPopupWindow popupWindow;

    public PopupAbsListHelper(Context context, View anchor) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        popupWindow = new ListPopupWindow(context);
        if (width > height) {
            popupWindow.setWidth(height / 2);
        } else {
            popupWindow.setWidth(width / 2);
        }
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(anchor);
        popupWindow.setModal(true);
        mAdapter = new Adapter();
        popupWindow.setAdapter(mAdapter);
    }

    public final void add(int itemId, ITEM item) {
        mAdapter.addItem(itemId, item);
    }

    public final void clear() {
        mAdapter.clear();
    }

    public final void from(ArrayList<ITEM> list) {
        int i;

        if (list != null) {
            for (i = 0; i < list.size(); i++) {
                mAdapter.addItem(-1, list.get(i));
            }
            mAdapter.refresh();
        }
    }

    public final ITEM getItem(int position) {
        return mAdapter.getItem(position);
    }

    public final void hide() {
        popupWindow.dismiss();
    }

    public final boolean isShowing() {
        return popupWindow.isShowing();
    }

    @Override
    public void setItem(int position, HOLDER holder, ITEM item) {
        holder.setItem(position, item);
    }

    public final void setOnDismissListener(PopupWindow.OnDismissListener dismissListener) {
        popupWindow.setOnDismissListener(dismissListener);
    }

    public final void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        popupWindow.setOnItemClickListener(clickListener);
    }

    public final void show() {
        popupWindow.show();
    }

    public final void toggle() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    private class Adapter extends AbsListAdapter<ITEM> {
        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HOLDER holder;
            ITEM item = getItem(position);

            if (convertView != null) {
                holder = (HOLDER) convertView.getTag();
            } else {
                convertView = createView();
                holder = createHolder(convertView);
                convertView.setTag(holder);
            }
            setItem(position, holder, item);

            return convertView;
        }
    }
}