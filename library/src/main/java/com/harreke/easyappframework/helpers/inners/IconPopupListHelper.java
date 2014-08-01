package com.harreke.easyappframework.helpers.inners;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.harreke.easyappframework.R;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * 带有图标的弹出列表
 */
public class IconPopupListHelper {
    private Adapter adapter;
    private ArrayList<Integer> iconIds = new ArrayList<Integer>();
    private ListPopupWindow popupWindow;
    private ArrayList<String> titles = new ArrayList<String>();

    public IconPopupListHelper(Context context, View anchor) {
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
        adapter = new Adapter();
        popupWindow.setAdapter(adapter);
    }

    public final void clear() {
        iconIds.clear();
        titles.clear();
    }

    public final void from(Context context, int iconArrayId, int titleArrayId) {
        TypedArray iconArray = null;
        String[] titleArray;
        int i;

        if (iconArrayId > 0) {
            iconArray = context.getResources().obtainTypedArray(iconArrayId);
        }
        titleArray = context.getResources().getStringArray(titleArrayId);
        if (titleArray != null) {
            if (iconArray != null) {
                for (i = 0; i < iconArray.length(); i++) {
                    add(iconArray.getResourceId(i, 0), titleArray[i]);
                }
                iconArray.recycle();
            } else {
                for (i = 0; i < titleArray.length; i++) {
                    add(0, titleArray[i]);
                }
            }
        }
    }

    public final void add(int iconId, String title) {
        iconIds.add(iconId);
        titles.add(title);
    }

    public final String getItem(int position) {
        return adapter.getItem(position);
    }

    public final void hide() {
        popupWindow.dismiss();
    }

    public final void refresh() {
        adapter.notifyDataSetChanged();
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

    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public String getItem(int position) {
            return titles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return iconIds.get(position);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IconPopupListHolder holder;

            if (convertView != null) {
                holder = (IconPopupListHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iconpopuplist, parent, false);
                holder = new IconPopupListHolder(convertView);
                convertView.setTag(holder);
            }
            holder.setData(iconIds.get(position), titles.get(position));

            return convertView;
        }
    }

    private class IconPopupListHolder {
        private ImageView iconpopuplist_icon;
        private TextView iconpopuplist_title;

        public IconPopupListHolder(View convertView) {
            iconpopuplist_icon = (ImageView) convertView.findViewById(R.id.iconpopuplist_icon);
            iconpopuplist_title = (TextView) convertView.findViewById(R.id.iconpopuplist_title);
        }

        public final void setData(int iconId, String titleId) {
            if (iconId == 0) {
                iconpopuplist_icon.setVisibility(View.GONE);
            } else {
                iconpopuplist_icon.setVisibility(View.VISIBLE);
                iconpopuplist_icon.setImageResource(iconId);
            }
            iconpopuplist_title.setText(titleId);
        }
    }
}