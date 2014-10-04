package com.harreke.easyapp.samples.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IExListHolder;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.entities.beans.ExListGroupItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public class ExListGroupHolder implements IExListHolder.Group<ExListGroupItem> {
    private TextView mGroupTag;

    public ExListGroupHolder(View convertView) {
        mGroupTag = (TextView) convertView.findViewById(R.id.group_tag);
    }

    @Override
    public void setItem(ExListGroupItem groupItem) {
        mGroupTag.setText(groupItem.getTag());
        if (groupItem.isChecked()) {
            mGroupTag.setBackgroundResource(R.drawable.shape_corner_theme);
        } else {
            mGroupTag.setBackgroundResource(R.drawable.shape_corner_gray);
        }
    }
}