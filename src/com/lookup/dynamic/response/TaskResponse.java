/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.response;

import com.lookup.dynamic.request.TaskRequestMeta;

import java.util.Map;

public class TaskResponse implements Cloneable {


    private Map<String, String> context;  //扩展参数
    private Long crawlerId;               //抓取任务的Id
    private TaskResponseMeta responseMeta;
    private boolean paged = false;    //  当前页面是否为分页响应
    private TaskRequestMeta requestMeta;

    @Override
    public String toString() {
        return crawlerId + " res {" +
                "responseMeta=" + responseMeta +
                ", paged=" + paged +
                ", context=" + context +
                '}';
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public TaskResponseMeta getResponseMeta() {
        return responseMeta;
    }

    public void setResponseMeta(TaskResponseMeta responseMeta) {
        this.responseMeta = responseMeta;
    }

    public void setCrawlerId(Long crawlerId) {
        this.crawlerId = crawlerId;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

}
