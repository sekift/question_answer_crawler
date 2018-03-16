package com.lookup.dynamic.actor.imp;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.Constants;
import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.ext.Actor;
import com.lookup.dynamic.proxy.FileProxyPool;
import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.request.TaskRequestMeta;
import com.lookup.dynamic.response.TaskResponse;

/**
 * 
 * @author:luyz
 * @time:2016-6-24 下午05:01:14
 * @version:
 */

@Actor
public class GeekparkActor extends BaseActor {

	@Inject
	@Named("parallelHttpActor")
	private ActorRef parallelHttpActor;
	@Inject
	@Named("geekparkParser")
	private ActorRef geekparkParser;
	@Inject
	@Named("parallelHttpActorUser")
	private ActorRef parallelHttpActorUser;

	@Override
	public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
		// 处理 task 请求消息
		if (message instanceof TaskRequest) {
			// 可以对当前request 的数据进行处理:对是否有代理使用不同的策略，
			// 因为无代理parallelHttpActor不能工作
			FileProxyPool fpp = new FileProxyPool();
			if (null == fpp || fpp.getIdleNum() < 1) {
				parallelHttpActorUser.tell(message, self());
			} else {
				parallelHttpActor.tell(message, self());
			}
		}
		// 处理 http返回消息
		else if (message instanceof TaskResponse) {
			TaskResponse response = (TaskResponse) message;

			// 流程处理方法
			handlerResponse(response);
		} else {
			unhandled(message);
		}
	}

	/**
	 * 用路径去判断处理流程 策略是url路径带不带参数
	 */
	private void handlerResponse(TaskResponse response) {
		String reqUrl = response.getRequestMeta().getUrl();
		try {
			/*
			 * 判断如果不是首页 http://zhidx.com/p/category/daily 就parser， 如果是则继续抓取
			 */
			if (reqUrl.contains("/topics/")) {
				geekparkParser.tell(response, self());
			} else {
				// 处理每一条记录
				String html = response.getResponseMeta().getBody();
				Document doc = Jsoup.parse(html);
				
				Elements elements = doc.getElementsByClass("article-title");//.select("ul li")
				if(null!=elements && !elements.isEmpty()){
					int i = 0;
					for(Element ele : elements){
						String href = ele.select("a").attr("href");
						/**
						 * data-event-label="互联网换装游戏：从理想到现实" href="/topics/215933" target="_blank"
						 */
						//判断条件：爬前五条
						if (i < 5) {
							String currentUrl = Constants.GEEKPARK_INDEX + href;
							TaskRequest request = new TaskRequest();
							TaskRequestMeta requestMetaPage = response.getRequestMeta().clone();
							requestMetaPage.setUrl(currentUrl);
							request.setRequestMeta(requestMetaPage);
							parallelHttpActorUser.tell(request, self());

							// 防止请求过快 ，配置延迟/s
							request.setDelayTime(2);
						}
						i++;
					}
					i = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
