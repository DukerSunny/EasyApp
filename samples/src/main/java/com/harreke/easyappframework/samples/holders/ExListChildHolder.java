package com.harreke.easyappframework.samples.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyappframework.holders.abslistview.ExListHolder;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.ExListChildItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public class ExListChildHolder extends ExListHolder.Child<ExListChildItem> {
    private ImageView mChildNode;
    private TextView mChildTitle;
    private int mColorGray;
    private int mColorTheme;

    public ExListChildHolder(View convertView) {
        super(convertView);

        mChildNode = (ImageView) convertView.findViewById(R.id.child_node);
        mChildTitle = (TextView) convertView.findViewById(R.id.child_title);
        mColorGray = convertView.getResources().getColor(R.color.Gray);
        mColorTheme = convertView.getResources().getColor(R.color.Theme);
    }

    @Override
    public void setItem(ExListChildItem childItem) {
        if (childItem.isChecked()) {
            mChildNode.setImageResource(R.drawable.node_pressed);
            mChildTitle.setTextColor(mColorTheme);
        } else {
            mChildNode.setImageResource(R.drawable.node);
            mChildTitle.setTextColor(mColorGray);
        }
        mChildTitle.setText(childItem.getTitle());
    }
}