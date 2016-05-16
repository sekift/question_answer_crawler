/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.actor.common;

import java.security.SecureRandom;

import javax.inject.Inject;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.ext.Actor;
import com.lookup.dynamic.proxy.Proxy;
import com.lookup.dynamic.proxy.ProxyPool;
import com.lookup.dynamic.proxy.UserAgentPool;
import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.request.TaskRequestMeta;
import com.lookup.dynamic.response.TaskResponse;
import com.lookup.dynamic.response.TaskResponseMeta;
import com.lookup.dynamic.util.OkHttpTool;

@Actor
public class ParallelHttpActor extends BaseActor {
    @Inject
    private UserAgentPool userAgentPool;
    @Inject
    private ProxyPool proxyPool;

    @Override
    public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
        TaskRequest request = (TaskRequest) message;
//        System.out.println(request.toString());
        long crawlerId = System.currentTimeMillis() + Math.abs(new SecureRandom().nextInt());
        
        Proxy proxy = proxyPool.getProxyHost();
        request.setProxyHost(proxy.getHttpHost());
        request.setProxyPort(proxy.getPort());
        System.out.println(request.toString());
        //失败重试  crawlerId不变
        if (!request.isRetry()) {
            request.setCrawlerId(crawlerId);
        }
        // 任务丢弃
        if (!request.isDiscard()) {
            if (request.getCurrentTryNum() == request.getMaxReTryNum()) {
                logger.error("任务超过重试次数 准备丢弃请求:" + request);
                request.setDiscard(true);
            }
            int deLaytime = request.getDelayTime();
           /* try {
                Thread.sleep(deLaytime);
            } catch (InterruptedException e) {
                logger.error("deLaytime  error");
            }*/
            
            if (proxy == null) {
                request.setDelayTime(deLaytime + 100);
                logger.info(" get proxy error");
                sender.tell(request, ActorRef.noSender());
            }
            logger.info(request.toString());
            TaskRequestMeta requestMeta = request.getRequestMeta();
            TaskResponseMeta responseMeta;
            String userAgent = " ";
            if (requestMeta.isDynamicUA()) {
                userAgent = userAgentPool.getUserAgent(UserAgentPool.CLIENT);
            } else {
                userAgent = requestMeta.getUserAgent();
            }

            try {
                if (requestMeta.getMethod().equals("get")) {
                    responseMeta = OkHttpTool.httpGet(requestMeta.getUrl(), userAgent, true, proxy.getHttpHost(), proxy.getPort(), requestMeta.getHeaders(), requestMeta.getCookies(), requestMeta.getParams(), requestMeta.getEncoding());
                } else {
                    responseMeta = OkHttpTool.httpPost(requestMeta.getUrl(), userAgent, true, proxy.getHttpHost(), proxy.getPort(), requestMeta.getHeaders(), requestMeta.getCookies(), requestMeta.getParams(), requestMeta.getEncoding());
                }
                if (responseMeta != null) {
                    proxy.setStateCode(responseMeta.getStatusCode());
                    if (responseMeta.getStatusCode() == 200) {
                        if (responseMeta.getBody().indexOf(requestMeta.getSuccessTag()) != -1) {
                            TaskResponse response = new TaskResponse();
                            response.setCrawlerId(request.getCrawlerId());
                            response.setPaged(request.isPaged());
                            response.setResponseMeta(responseMeta);
                            response.setRequestMeta(requestMeta);
                            response.setContext(request.getContext());
                            logger.info(response.toString());
                            sender.tell(response, recipient);
                        } else {
                            request.setDelayTime(deLaytime + 20);
                            logger.warn(request.getCrawlerId() + " " + Proxy.ERROR_403 + " recall proxy");
                            request.setRetry(true);
                            request.incrementCurrentTryNum();
                            sender.tell(request, ActorRef.noSender());
                        }
                    } else {
                        request.setDelayTime(deLaytime + 10);
                        logger.warn(request.getCrawlerId() + " " + responseMeta.getStatusCode() + " recall proxy");
                        request.setRetry(true);
                        request.incrementCurrentTryNum();
                        sender.tell(request, ActorRef.noSender());
                    }
                }

            } catch (Exception e) {
                proxy.setStateCode(500);
                request.setDelayTime(deLaytime + 10);
                logger.error(request.getCrawlerId() + " " + Proxy.ERROR_500 + " server error", e.getLocalizedMessage());
                request.setRetry(true);
                request.incrementCurrentTryNum();
                sender.tell(request, ActorRef.noSender());
            } finally {
                proxyPool.returnProxy(proxy);
            }

        }


    }
}