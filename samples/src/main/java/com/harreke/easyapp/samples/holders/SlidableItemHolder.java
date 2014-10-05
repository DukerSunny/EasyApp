package com.harreke.easyapp.samples.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.entities.beans.AbsListItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class SlidableItemHolder implements IAbsListHolder<AbsListItem> {
    private TextView mDesc;
    private TextView mTitle;

    public SlidableItemHolder(View convertView) {
        mTitle = (TextView) convertView.findViewById(R.id.title);
        mDesc = (TextView) convertView.findViewById(R.id.desc);
    }

    @Override
    public void setItem(int position, AbsListItem item) {
        mTitle.setText(String.valueOf(item.getTitle()));
        mDesc.setText(item.getDesc());
    }
}