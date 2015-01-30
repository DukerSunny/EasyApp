package com.harreke.easyapp.requests.ion;

import android.util.Log;
import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/31
 */
public class IonImageExecutor implements IRequestExecutor, FutureCallback<ImageView> {
    private final static String TAG = "IonImageExecutor";
    private IRequestCallback<ImageView> mImageCallback = null;
    private Future<ImageView> mImageFuture = null;
    private String mImageUrl = null;

    public IonImageExecutor(ImageView imageView, RequestBuilder requestBuilder, IRequestCallback<ImageView> imageCallback) {
        mImageUrl = requestBuilder.getUrl();
        mImageCallback = imageCallback;
        requestBuilder.print();
        mImageFuture = IonBuilder.build(imageView.getContext(), requestBuilder).intoImageView(imageView).setCallback(this);
    }

    public IonImageExecutor(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId,
            IRequestCallback<ImageView> imageCallback) {
        mImageUrl = imageUrl;
        mImageCallback = imageCallback;
        mImageFuture =
                Ion.with(imageView).placeholder(loadingImageId).error(retryImageId).animateIn(R.anim.fade_in).smartSize(true)
                        .deepZoom().load(mImageUrl).setCallback(this);
    }

    @Override
    public void cancel() {
        if (mImageFuture != null) {
            mImageFuture.cancel();
        }
        clear();
    }

    private void clear() {
        mImageUrl = null;
        mImageCallback = null;
        mImageFuture = null;
    }

    @Override
    public boolean isExecuting() {
        return mImageFuture != null;
    }

    @Override
    public void onCompleted(Exception e, ImageView result) {
        if (mImageCallback != null) {
            if (e != null) {
                Log.e(TAG, "Image " + mImageUrl + " failure: " + e.getMessage());
                mImageCallback.onFailure(mImageUrl);
            } else {
                mImageCallback.onSuccess(mImageUrl, result);
            }
        }
        clear();
    }
}