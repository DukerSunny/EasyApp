package com.harreke.easyapp.requests.ion;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/31
 */
public class IonBitmapExecutor implements IRequestExecutor, FutureCallback<Bitmap> {
    private final static String TAG = "IonBitmapExecutor";
    private IRequestCallback<Bitmap> mBitmapCallback = null;
    private Future<Bitmap> mBitmapFuture = null;
    private String mBitmapUrl = null;

    public IonBitmapExecutor(Context context, String bitmapUrl, IRequestCallback<Bitmap> bitmapCallback) {
        Log.e(TAG, "BITMAP " + bitmapUrl);
        mBitmapUrl = bitmapUrl;
        mBitmapCallback = bitmapCallback;
        mBitmapFuture = Ion.with(context).load(mBitmapUrl).asBitmap().setCallback(this);
    }

    @Override
    public void cancel() {
        if (mBitmapFuture != null) {
            mBitmapFuture.cancel();
        }
        clear();
    }

    private void clear() {
        mBitmapUrl = null;
        mBitmapCallback = null;
        mBitmapFuture = null;
    }

    @Override
    public boolean isExecuting() {
        return mBitmapFuture != null;
    }

    @Override
    public void onCompleted(Exception e, Bitmap result) {
        if (mBitmapCallback != null) {
            if (e != null) {
                mBitmapCallback.onFailure(mBitmapUrl);
            } else {
                mBitmapCallback.onSuccess(mBitmapUrl, result);
            }
        }
        clear();
    }
}