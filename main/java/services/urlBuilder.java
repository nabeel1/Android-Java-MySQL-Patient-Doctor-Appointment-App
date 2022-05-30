package com.example.shahidhussain.assignemnt2.services;

import android.net.Uri;

import java.util.Map;

public class urlBuilder {
    public String buildURI(String url, Map<String, String> params) {

        // build url with parameters.
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build().toString();
    }
}
