package com.harreke.utils.helpers;

import android.widget.ImageView;

import com.harreke.utils.imageloaders.volley.VolleyImageLoader;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.IRequestExecutor;

public class ImageLoadHelper {
    //    public static Bitmap getBitmap(Context context, String imageUrl) {
    //        return getBitmap(context, imageUrl, -1, -1);
    //    }
    //
    //    public static Bitmap getBitmap(Context context, String imageUrl, int width, int height) {
    //        Bitmap bitmap;
    //        Matrix matrix;
    //
    //        if (imageUrl != null) {
    //            try {
    //                bitmap = Ion.with(context).load(imageUrl).asBitmap().get();
    //                if (width > 0 && height > 0) {
    //                    matrix = new Matrix();
    //                    matrix.postScale((float) width / (float) bitmap.getWidth(), (float) height / (float) bitmap.getHeight());
    //                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    //                }
    //                return bitmap;
    //            } catch (Exception ignored) {
    //            }
    //        }
    //        return null;
    //    }

    public static IRequestExecutor loadImage(ImageView image, String imageUrl, IRequestCallback<ImageView> callback) {
        return new VolleyImageLoader().load(image, imageUrl, callback);
    }

    /**
     从网络获取图片

     @param image
     指定的图片视图
     @param imageUrl
     图片url
     @param callback
     图片加载回调
     */
    public static IRequestExecutor loadImage(ImageView image, String imageUrl, int loadingImageId, int retryImageId, IRequestCallback<ImageView> callback) {
        return new VolleyImageLoader(loadingImageId, retryImageId).load(image, imageUrl, callback);
    }

    public static IRequestExecutor loadImage(ImageView image, String imageUrl) {
        return new VolleyImageLoader().load(image, imageUrl, null);
    }
}