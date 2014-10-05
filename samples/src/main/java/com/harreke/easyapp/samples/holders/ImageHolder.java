package com.harreke.easyapp.samples.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.entities.beans.AbsListItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
public class ImageHolder implements IAbsListHolder<AbsListItem> {
    private TextView mDesc;
    private ImageView mImage;
    private TextView mTitle;

    public ImageHolder(View convertView) {
        mImage = (ImageView) convertView.findViewById(R.id.image);
        mTitle = (TextView) convertView.findViewById(R.id.title);
        mDesc = (TextView) convertView.findViewById(R.id.desc);
    }

    @Override
    public void setItem(int position, AbsListItem item) {
        ImageLoaderHelper.loadImage(mImage, item.getImage());
        mTitle.setText(String.valueOf(item.getTitle()));
        mDesc.setText(item.getDesc());
    }
}