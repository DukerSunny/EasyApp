package com.harreke.easyapp.requests.ion;

import android.content.Context;

import com.harreke.easyapp.requests.RequestBuilder;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.util.Iterator;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class IonBuilder {
    public static Builders.Any.B build(Context context, RequestBuilder requestBuilder) {
        Builders.Any.B builder;
        Map<String, String> header;
        Map<String, String> body;
        Iterator<String> iterator;
        String requestUrl;
        String key;

        requestUrl = requestBuilder.getUrl();
        builder = Ion.with(context).load(requestBuilder.getMethod().toString(), requestUrl).setTimeout(15000);
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
            builder.setBodyParameter(key, body.get(key));
        }

        return builder;
    }
}
