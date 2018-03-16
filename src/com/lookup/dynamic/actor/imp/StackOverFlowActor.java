/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.actor.imp;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.ext.Actor;
import com.lookup.dynamic.proxy.FileProxyPool;
import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.request.TaskRequestMeta;
import com.lookup.dynamic.response.TaskResponse;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
@Actor
public class StackOverFlowActor extends BaseActor {

    @Inject
    @Named("parallelHttpActor")
    private ActorRef parallelHttpActor;
    @Inject
    @Named("stackOverFlowParser")
    private ActorRef stackOverFlowParser;
    @Inject
    @Named("parallelHttpActorUser")
    private ActorRef parallelHttpActorUser;

    @Override
    public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
        //处理 task 请求消息
		if (message instanceof TaskRequest) {
			// 可以对当前request 的数据进行处理:对是否有代理使用不同的策略，
			// 因为无代理parallelHttpActor不能工作
			FileProxyPool fpp = new FileProxyPool();
			if (null==fpp||fpp.getIdleNum() < 1) {
				parallelHttpActorUser.tell(message, self());
			} else {
				parallelHttpActor.tell(message, self());
			}
		}
        //处理 http返回消息
        else if (message instanceof TaskResponse) {
            /*TaskResponse response = (TaskResponse) message;
            System.out.println("parallelProcess WIKIIATALISTACTOR rep");
            stackOverFlowParser.tell(response, self());*/ //第一版的设计
        	
        	TaskResponse response =(TaskResponse)message;
        	
        	//流程处理方法
        	handlerResponse(response);
        } else {
            unhandled(message);
        }
    }
    
    /**
     * 用路径去判断处理流程
     * 策略是url路径带不带参数
     */
    private void handlerResponse(TaskResponse response){
    	String reqUrl = response.getRequestMeta().getUrl();
    	try{
    		URL resultUrl=new URL(reqUrl);
    		/*
    		 * 如果是详细页-->直接发送到parse处理
    		 * 如果不是-->获取每一条问题继续爬取
    		 */
    		if(null == resultUrl.getQuery()){//使用是否有查询header来判断：可以优化
    			 stackOverFlowParser.tell(response, self());
    		}else{
    			String html = response.getResponseMeta().getBody();
                Document doc = Jsoup.parse(html);
                Elements elements = doc.getElementsByClass("question-hyperlink");
				for (Element e : elements) {
					String href = e.attributes().get("href");
					if (!href.contains("stackoverflow.com/") && !href.contains("stackoverflow.blog/")) {//过滤掉不是title的项，避免脏数据
						String currentUrl = "http://stackoverflow.com" + href;
						TaskRequest request = new TaskRequest();
						TaskRequestMeta requestMetaPage = response.getRequestMeta().clone();
						requestMetaPage.setUrl(currentUrl);
						request.setRequestMeta(requestMetaPage);
						// logger.info("当前请求的页面 " + currentUrl);
						parallelHttpActorUser.tell(request, self());
						// 防止请求过快 ，配置延迟/s
						request.setDelayTime(13);
					}
				}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
}
