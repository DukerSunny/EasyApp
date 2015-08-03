package com.harreke.easyapp.requests.androidasynchttp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;

/**
 * Created by 启圣 on 2015/6/29.
 */
public class AndroidAsyncHttpClient {
    private static AndroidAsyncHttpClient mInstance = null;
    private AsyncHttpClient mAsyncHttpClient;

    private AndroidAsyncHttpClient() {
        mAsyncHttpClient = new AsyncHttpClient();
        mAsyncHttpClient.setMaxRetriesAndTimeout(3, 15000);
    }

    public static AndroidAsyncHttpClient getInstance() {
        if (mInstance == null) {
            synchronized (AndroidAsyncHttpClient.class) {
                if (mInstance == null) {
                    mInstance = new AndroidAsyncHttpClient();
                }
            }
        }

        return mInstance;
    }

    public void cancel(@NonNull Context context) {
        mAsyncHttpClient.cancelRequests(context, true);
    }

    public void cancelAll() {
        mAsyncHttpClient.cancelAllRequests(true);
    }

    public RequestHandle get(@NonNull Context context, @NonNull String requestUrl,
            @NonNull ResponseHandlerInterface handlerInterface) {
        return get(context, requestUrl, null, handlerInterface);
    }

    public RequestHandle get(@NonNull Context context, @NonNull String requestUrl, @Nullable Header[] headers,
            @NonNull ResponseHandlerInterface handlerInterface) {
        return mAsyncHttpClient.get(context, requestUrl, headers, null, handlerInterface);
    }

    public RequestHandle post(@NonNull Context context, @NonNull String requestUrl,
            @NonNull ResponseHandlerInterface handlerInterface) {
        return post(context, requestUrl, null, null, handlerInterface);
    }

    public RequestHandle post(@NonNull Context context, @NonNull String requestUrl, @Nullable Header[] headers,
            @Nullable RequestParams requestParams, @NonNull ResponseHandlerInterface handlerInterface) {
        return mAsyncHttpClient.post(context, requestUrl, requestParams, handlerInterface);
    }
}