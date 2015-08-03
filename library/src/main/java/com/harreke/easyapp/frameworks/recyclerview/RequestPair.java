package com.harreke.easyapp.frameworks.recyclerview;

import android.support.annotation.NonNull;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.IObjectParser;
import com.harreke.easyapp.requests.RequestBuilder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/29
 */
public class RequestPair<ITEM> {
    public IListParser<ITEM> listParser;
    public IObjectParser<ITEM> objectParser;
    public RequestBuilder requestBuilder;

    public RequestPair(@NonNull RequestBuilder requestBuilder, @NonNull IObjectParser<ITEM> objectParser) {
        this.requestBuilder = requestBuilder;
        this.objectParser = objectParser;
    }

    public RequestPair(@NonNull RequestBuilder requestBuilder, @NonNull IListParser<ITEM> listParser) {
        this.requestBuilder = requestBuilder;
        this.listParser = listParser;
    }
}