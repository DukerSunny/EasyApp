package tv.acfun.read.helpers;

import android.widget.ImageView;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/20
 */
public class OfflineImageLoaderHelper {
    public static void loadImage(ImageView image, OfflineImage offlineImage) {
        switch (offlineImage) {
            case Avatar:
                image.setImageResource(R.drawable.image_avatar);
                break;
            case Thumbnail:
                image.setImageResource(R.drawable.image_thumbnail);
        }
    }

    public enum OfflineImage {
        Avatar,
        Thumbnail
    }
}