/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.response;

//import HtmlCompressor;

import com.lookup.dynamic.util.HtmlCompressor;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/15.
 */
public class TaskResponseMeta {

    private List<Map<String, String>> cookies;

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    private Map<String, List<String>> header;

    private int statusCode;

    private String url;

    private String body;

    public boolean isLogBody() {
        return isLogBody;
    }

    public void setLogBody(boolean isLogBody) {
        this.isLogBody = isLogBody;
    }

    private boolean isLogBody = false;        //显示html日志  方便调试,生产环境不建议使用

    public List<Map<String, String>> getCookies() {
        return cookies;
    }

    public void setCookies(List<Map<String, String>> cookies) {
        this.cookies = cookies;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", cookies=" + cookies +
                ", header=" + header +
                ", statusCode=" + statusCode +
                ", body=" + (isLogBody == true ? HtmlCompressor.compress(body) : "") +
                // ", body=" +HtmlCompressor.compress(body) +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
