package com.harreke.easyapp.samples.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.recyclerview.RecyclerHolder;
import com.harreke.easyapp.samples.R;
import com.harreke.easyapp.samples.entities.beans.RecyclerItem;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/01
 */
@Deprecated
public class ImageRecyclerHolder extends RecyclerHolder<RecyclerItem> {
    private TextView mDesc;
    private ImageView mImage;
    private TextView mTitle;

    public ImageRecyclerHolder(View convertView) {
        super(convertView);

        mImage = (ImageView) convertView.findViewById(R.id.image);
        mTitle = (TextView) convertView.findViewById(R.id.title);
        mDesc = (TextView) convertView.findViewById(R.id.desc);
    }

    @Override
    public void setItem(RecyclerItem item) {
        ImageLoaderHelper.loadImage(mImage, item.getImage());
        mTitle.setText(String.valueOf(item.getTitle()));
        mDesc.setText(item.getDesc());
    }
}