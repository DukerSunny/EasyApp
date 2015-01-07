package com.harreke.easyapp.requests.ion;

import android.content.Context;
import android.util.Log;

import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.requests.RequestBuilder;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/31
 */
public class IonHttpExecutor implements IRequestExecutor, FutureCallback<String> {
    private final static String TAG = "IonHttpExecutor";
    private IRequestCallback<String> mRequestCallback = null;
    private Future<String> mRequestFuture = null;
    private String mRequestUrl = null;

    public IonHttpExecutor(Context context, RequestBuilder requestBuilder, IRequestCallback<String> requestCallback) {
        Builders.Any.B builder;
        Map<String, String> header;
        Map<String, String> body;
        Iterator<String> iterator;
        String key;

        mRequestUrl = requestBuilder.getUrl();
        mRequestCallback = requestCallback;
        builder = Ion.with(context).load(mRequestUrl).setTimeout(15000);
        header = requestBuilder.getHeader();
        body = requestBuilder.getBody();
        iterator = header.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            builder.addHeader(key, header.get(key));
        }
        iterator = body.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            builder.setBodyParameter(key, header.get(key));
        }
        mRequestFuture = builder.asString().setCallback(this);
    }

    @Override
    public void cancel() {
        if (mRequestFuture != null) {
            mRequestFuture.cancel(true);
        }
        clear();
    }

    private void clear() {
        mRequestUrl = null;
        mRequestCallback = null;
        mRequestFuture = null;
    }

    @Override
    public boolean isExecuting() {
        return mRequestFuture != null;
    }

    @Override
    public void onCompleted(Exception e, String result) {
        if (mRequestCallback != null) {
            if (e != null) {
                Log.e(TAG, "Http " + mRequestUrl + " failure: " + e.getMessage());
                mRequestCallback.onFailure(mRequestUrl);
            } else {
                mRequestCallback.onSuccess(mRequestUrl, result);
            }
        }
        clear();
    }
}