package com.harreke.utils.requests.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest extends StringRequest {
    private HashMap<String, String> mBodyMap = new HashMap<String, String>();
    private HashMap<String, String> mHeaderMap = new HashMap<String, String>();

    public VolleyRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, HashMap<String, String> headerMap,
            HashMap<String, String> bodyMap) {
        super(method, url, listener, errorListener);
        mHeaderMap.putAll(headerMap);
        mBodyMap.putAll(bodyMap);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaderMap;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mBodyMap;
    }
}