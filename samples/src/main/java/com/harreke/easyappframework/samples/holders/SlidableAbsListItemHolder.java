package com.harreke.easyappframework.samples.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyappframework.holders.abslistview.IAbsListHolder;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.AbsListItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class SlidableAbsListItemHolder implements IAbsListHolder<AbsListItem> {
    private TextView mDesc;
    private TextView mTitle;

    public SlidableAbsListItemHolder(View convertView) {
        mTitle = (TextView) convertView.findViewById(R.id.title);
        mDesc = (TextView) convertView.findViewById(R.id.desc);
    }

    @Override
    public void setItem(AbsListItem item) {
        mTitle.setText(String.valueOf(item.getTitle()));
        mDesc.setText(item.getDesc());
    }
}