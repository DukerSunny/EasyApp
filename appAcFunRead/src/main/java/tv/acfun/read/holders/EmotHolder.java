package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/11
 */
public class EmotHolder extends RecyclerHolder<String> {
    private ImageView mEmot;

    public EmotHolder(View itemView) {
        super(itemView);

        mEmot = (ImageView) itemView;
    }

    @Override
    public void setItem(String emotName) {
        ImageLoaderHelper.loadImage(mEmot, "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/" + emotName + "/" +
                (getPosition() < 9 ? "0" : "") +
                (getPosition() + 1), R.drawable.image_loading, R.drawable.image_idle);
    }
}