package com.harreke.easyapp.requests.ion;

import android.content.Context;
import android.widget.ImageView;

import com.harreke.easyapp.R;
import com.harreke.easyapp.requests.RequestBuilder;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ImageViewFuture;

import java.util.Iterator;
import java.util.Map;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class IonBuilder {
    public static ImageViewFuture build(ImageView imageView, String imageUrl, int loadingImageId, int retryImageId) {
        Builders.IV.F<? extends Builders.IV.F<?>> builder;

        builder = Ion.with(imageView);
        if (loadingImageId > 0) {
            builder.placeholder(loadingImageId);
        }
        if (retryImageId > 0) {
            builder.error(retryImageId);
        }

        return builder.animateIn(R.anim.fade_in).smartSize(true).deepZoom().load(imageUrl);
    }

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

    public static ImageViewFuture build(ImageView imageView, RequestBuilder requestBuilder) {
        return build(imageView.getContext(), requestBuilder).intoImageView(imageView);
    }
}