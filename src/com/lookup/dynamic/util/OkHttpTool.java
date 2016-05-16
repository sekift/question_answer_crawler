/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.util;

import com.lookup.dynamic.response.TaskResponseMeta;
import com.squareup.okhttp.*;
import com.squareup.okhttp.Request.Builder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/26 0026.
 */
public class OkHttpTool {

    static OkHttpClient client = new OkHttpClient();
    ;

    public static TaskResponseMeta httpGet(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                                           Map<String, String> headers, Map<String, String> cookies, Map<String, String> params, String encoding) throws IOException {
        OkHttpClient getClient = client.clone();
        getClient.setConnectTimeout(10, TimeUnit.SECONDS);
        getClient.setWriteTimeout(10, TimeUnit.SECONDS);
        getClient.setReadTimeout(30, TimeUnit.SECONDS);
        Request.Builder builder = new Builder();
        if (isProxy) {
            getClient.setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }
        if (userAgent != null) {
            builder.header("User-Agent", userAgent);
        }
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder.header(key, headers.get(key));
            }
        }
        String cookie = "";
        if (cookies != null) {
            StringBuffer buffer = new StringBuffer();
            for (String key : cookies.keySet()) {
                buffer.append(key + "=" + cookies.get(key) + ",");
            }
            cookie = buffer.toString();
        }
        builder.header("cookie", cookie);

        if (params != null) {
            StringBuffer sb = new StringBuffer();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    sb.append(key + "=" + params.get(key) + "&");
                }
                url = url + "?" + sb.substring(0, sb.length() - 1);
            }
        }
        builder.url(url);
        Request httpRequest = builder.build();

        TaskResponseMeta taskResponseMeta = new TaskResponseMeta();
        Response response = getClient.newCall(httpRequest).execute();
        taskResponseMeta.setUrl(url);
        taskResponseMeta.setStatusCode(response.code());
        taskResponseMeta.setHeader(response.headers().toMultimap());
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            byte[] context = responseBody.bytes();
            taskResponseMeta.setBody(new String(context, encoding));
        }

        return taskResponseMeta;
    }

    public static TaskResponseMeta httpPost(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                                            Map<String, String> headers, Map<String, String> cookies, Map<String, String> params, String encoding) throws ExecutionException, InterruptedException, IOException {
        OkHttpClient postClient = client.clone();
        postClient.setConnectTimeout(10, TimeUnit.SECONDS);
        postClient.setWriteTimeout(10, TimeUnit.SECONDS);
        postClient.setReadTimeout(30, TimeUnit.SECONDS);
        Request.Builder builder = new Builder();
        if (isProxy) {
            postClient.setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }
        if (userAgent != null) {
            builder.header("User-Agent", userAgent);
        }
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder.header(key, headers.get(key));
            }
        }
        String cookie = "";
        if (cookies != null) {
            StringBuffer buffer = new StringBuffer();
            for (String key : cookies.keySet()) {
                buffer.append(key + "=" + cookies.get(key) + ",");
            }
            cookie = buffer.toString();
        }
        builder.header("cookie", cookie);

        if (params != null) {
            if (params != null) {
                FormEncodingBuilder formBuilder = new FormEncodingBuilder();
                for (String key : params.keySet()) {
                    formBuilder.add(key, params.get(key));
                }
                RequestBody formBody = formBuilder.build();
                builder.post(formBody);
            }
        }
        builder.url(url);
        Request httpRequest = builder.build();
        TaskResponseMeta taskResponseMeta = new TaskResponseMeta();
        Response response = postClient.newCall(httpRequest).execute();
        taskResponseMeta.setUrl(url);
        taskResponseMeta.setStatusCode(response.code());
        taskResponseMeta.setHeader(response.headers().toMultimap());
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            byte[] context = responseBody.bytes();
            taskResponseMeta.setBody(new String(context, encoding));
        }
        return taskResponseMeta;
    }
}
