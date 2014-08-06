package com.harreke.easyappframework.requests.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.harreke.easyappframework.requests.IRequestCallback;
import com.harreke.easyappframework.requests.IRequestExecutor;
import com.harreke.easyappframework.requests.RequestBuilder;

/**
 * Volley Http请求执行器
 */
public class VolleyRequestExecutor implements IRequestExecutor, Response.Listener<String>, Response.ErrorListener {
    private IRequestCallback<String> mCallback = null;
    private Request mRequest = null;

    public VolleyRequestExecutor(Context context, RequestBuilder builder, IRequestCallback<String> callback) {
        VolleyInstance instance = VolleyInstance.getInstance(context);
        int volleyMethod = 0;

        if (RequestBuilder.METHOD_GET.equals(builder.getMethod())) {
            volleyMethod = Request.Method.GET;
        } else if (RequestBuilder.METHOD_POST.equals(builder.getMethod())) {
            volleyMethod = Request.Method.POST;
        }
        mCallback = callback;

        mRequest = instance.add(new VolleyRequest(volleyMethod, builder.getUrl(), this, this, builder.getHeader(), builder.getBody()));
    }

    @Override
    public void cancel() {
        if (isExecuting()) {
            mRequest.cancel();
        }
    }

    @Override
    public boolean isExecuting() {
        return mRequest != null && !mRequest.isCanceled();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        mRequest = null;
        if (mCallback != null) {
            mCallback.onFailure();
        }
    }

    @Override
    public void onResponse(String result) {
        mRequest = null;
        if (mCallback != null) {
            if (result == null) {
                mCallback.onFailure();
            } else {
                mCallback.onSuccess(result);
            }
        }
    }
}