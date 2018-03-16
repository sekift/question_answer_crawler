/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.request;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRequest {

    private TaskRequestMeta requestMeta;

    private String proxyHost;    //代理 主机
    private int proxyPort;     // 代理端口

    private Map<String, String> context;  //扩展参数

    private Long crawlerId;               //抓取任务的ID

    private int maxReTryNum = 3;     //最大失败次数 50

    private AtomicInteger currentTryNum = new AtomicInteger();   // 当前重试次数

    private int delayTime;        // 延时请求发送时间 s为单位

    private boolean discard = false;   //是否丢弃

    private boolean retry = false;

    public boolean isFixUserAgent() {
        return fixUserAgent;
    }

    public void setFixUserAgent(boolean fixUserAgent) {
        this.fixUserAgent = fixUserAgent;
    }

    private boolean fixUserAgent;

    public boolean isSync() {
        return sync;
    }

    private boolean paged = false;

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public Long getCrawlerId() {
        return crawlerId;
    }

    public void setCrawlerId(Long crawlerId) {
        this.crawlerId = crawlerId;
    }

    public int getMaxReTryNum() {
        return maxReTryNum;
    }

    public void setMaxReTryNum(int maxReTryNum) {
        this.maxReTryNum = maxReTryNum;
    }

    /**
     * 是否同步  当前使用同步抓取，可以修改OkHttpTool  使用异步抓取
     */
    private boolean sync = false;

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return crawlerId + " req {" +
                "requestMeta =" + requestMeta +
                ", currentTryNum=" + currentTryNum.intValue() +
                ", maxReTryNum=" + maxReTryNum +
                ", delayTime=" + delayTime +
                ", fixUserAgent=" + fixUserAgent +
                ", sync=" + sync +
                ", retry=" + retry +
                ", paged=" + paged +
                ", context=" + context +
                ", proxy=[" + proxyHost + ":" + proxyPort + "]" +
                '}';
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getCurrentTryNum() {
        return currentTryNum.get();
    }

    public void incrementCurrentTryNum() {
        currentTryNum.getAndIncrement();
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }
}
