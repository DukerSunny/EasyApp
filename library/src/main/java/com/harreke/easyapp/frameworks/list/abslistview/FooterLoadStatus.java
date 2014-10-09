package com.harreke.easyapp.frameworks.list.abslistview;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.frameworks.list.ILoadStatus;
import com.harreke.easyapp.frameworks.list.LoadStatus;
import com.harreke.easyapp.frameworks.list.OnLoadListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class FooterLoadStatus implements ILoadStatus, View.OnClickListener {
    private ImageView loadmore_icon;
    private TextView loadmore_text;
    private OnLoadListener mLoadListener = null;
    private LoadStatus mLoadStatus;
    private View mRoot;

    public FooterLoadStatus(View root) {
        mRoot = root;
        loadmore_icon = (ImageView) root.findViewById(R.id.loadmore_icon);
        loadmore_text = (TextView) root.findViewById(R.id.loadmore_text);

        mRoot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mLoadListener != null) {
            if (mLoadStatus == LoadStatus.Idle || mLoadStatus == LoadStatus.Retry) {
                setLoadStatus(LoadStatus.Loading);
                mLoadListener.onLoad();
            }
        }
    }

    private void setIcon(int iconId) {
        Drawable drawable;

        loadmore_icon.setImageResource(iconId);
        drawable = loadmore_icon.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    @Override
    public void setLoadStatus(LoadStatus loadStatus) {
        if (mLoadStatus != loadStatus) {
            mLoadStatus = loadStatus;
            switch (loadStatus) {
                case Idle:
                    setIcon(R.drawable.info_empty);
                    loadmore_text.setText(R.string.loadmore_idle);
                    break;
                case Loading:
                    setIcon(R.drawable.anim_progress_radiant);
                    loadmore_text.setText(R.string.loadmore_loading);
                    break;
                case Last:
                    setIcon(R.drawable.info_empty);
                    loadmore_text.setText(R.string.loadmore_last);
                    break;
                case Retry:
                    setIcon(R.drawable.info_retry);
                    loadmore_text.setText(R.string.loadmore_retry);
            }
        }
    }

    @Override
    public void setOnLoadListener(OnLoadListener loadListener) {
        mLoadListener = loadListener;
    }

    @Override
    public void setVisibility(int visibility) {
        mRoot.setVisibility(visibility);
    }
}