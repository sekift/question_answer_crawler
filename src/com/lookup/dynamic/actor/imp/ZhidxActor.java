package com.lookup.dynamic.actor.imp;

import java.util.Date;
import java.util.List;

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
import com.lookup.dynamic.util.DateUtil;
import com.lookup.dynamic.util.RegexTool;

/**
 * 
 * @author:luyz
 * @time:2016-6-24 下午05:01:14
 * @version:
 */

@Actor
public class ZhidxActor extends BaseActor {

	@Inject
	@Named("parallelHttpActor")
	private ActorRef parallelHttpActor;
	@Inject
	@Named("zhidxParser")
	private ActorRef zhidxParser;
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
			if (reqUrl.endsWith(".html")) {
				zhidxParser.tell(response, self());
			} else {
				// 处理每一条记录
				String html = response.getResponseMeta().getBody();
				Document doc = Jsoup.parse(html);
				Elements elements = doc.getElementsByClass("tabCont").select("ul li");
				
				//获取第一项即可
				if(null!=elements && !elements.isEmpty()){
					Element e = elements.get(0);
					Elements eles = e.getElementsByClass("ugc");
					String text = eles.text();
					String href = eles.select("a").attr("href");
					/**
					 * text:智东西（公众号：zhidxcom） 编 | 四月 智东西早报 第379期 2016.6.25 周六 ① Ocu […]详细
					 * href:http://zhidx.com/p/51608.html
					 */
					List<String> sourceDateList = RegexTool.getResult(text, "\\d{4}.\\d+.\\d+");
					String sDate = null;
					if(!sourceDateList.isEmpty()){
						sDate = sourceDateList.get(0).replace(".", "-");
					}
					
					Date date = DateUtil.stringToDate(sDate, "yyyy-MM-dd");
					int diff = DateUtil.daysBetween(date, new Date(System.currentTimeMillis()));
					//判断条件：等于今天的才爬
					if (diff == 0) {
						String currentUrl = href;
						TaskRequest request = new TaskRequest();
						TaskRequestMeta requestMetaPage = response.getRequestMeta().clone();
						requestMetaPage.setUrl(currentUrl);
						request.setRequestMeta(requestMetaPage);
						parallelHttpActorUser.tell(request, self());

						// 防止请求过快 ，配置延迟/s
						request.setDelayTime(2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
