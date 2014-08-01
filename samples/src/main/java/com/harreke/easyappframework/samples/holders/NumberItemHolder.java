package com.harreke.easyappframework.samples.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyappframework.holders.abslistview.IAbsListHolder;
import com.harreke.easyappframework.samples.R;
import com.harreke.easyappframework.samples.entities.beans.NumberItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class NumberItemHolder implements IAbsListHolder<NumberItem> {
    private TextView mNumber;
    private TextView mNumberDesc;

    public NumberItemHolder(View convertView) {
        mNumber = (TextView) convertView.findViewById(R.id.number);
        mNumberDesc = (TextView) convertView.findViewById(R.id.numberdesc);
    }

    @Override
    public void setItem(NumberItem item) {
        mNumber.setText(String.valueOf(item.getNumber()));
        mNumberDesc.setText(item.getNumberDesc());
    }
}