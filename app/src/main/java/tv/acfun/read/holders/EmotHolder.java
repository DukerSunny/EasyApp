package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/11
 */
public class EmotHolder implements IAbsListHolder<String> {
    private ImageView mEmot;

    public EmotHolder(View convertView) {
        mEmot = (ImageView) convertView;
    }

    @Override
    public void setItem(int position, String emotName) {
        ImageLoaderHelper.loadImage(mEmot,
                "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/" + emotName + "/" + (position < 9 ? "0" : "") + (position + 1));
    }
}