package com.harreke.utils.requests.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.harreke.utils.requests.IRequestCallback;
import com.harreke.utils.requests.IRequestExecutor;
import com.harreke.utils.requests.RequestBuilder;
import com.harreke.utils.tools.GsonUtil;

/**
 使用Volley封装的Http请求执行器

 @param <RESULT>
 目标类型，可以为自定义Java bean或String
 */
public class VolleyRequestExecutor<RESULT> implements IRequestExecutor, Response.Listener<String>, Response.ErrorListener {
    private IRequestCallback<RESULT> mCallback = null;
    private Request<String> mRequest = null;

    public VolleyRequestExecutor(Context context, RequestBuilder builder, IRequestCallback<RESULT> callback) {
        VolleyInstance instance = VolleyInstance.getInstance(context);
        int volleyMethod = 0;

        switch (builder.getMethod()) {
            case METHOD_GET:
                volleyMethod = Request.Method.GET;
                break;
            case METHOD_POST:
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
        RESULT bean;

        mRequest = null;
        if (mCallback != null) {
            bean = GsonUtil.toBean(result, new TypeToken<RESULT>() {
            }.getType());
            if (bean == null) {
                mCallback.onFailure();
            } else {
                mCallback.onSuccess(bean);
            }
        }
    }
}