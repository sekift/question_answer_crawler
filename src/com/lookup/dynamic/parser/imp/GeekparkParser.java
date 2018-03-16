package com.lookup.dynamic.parser.imp;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.mutiple.dao.GeekparkDao;
import com.lookup.dynamic.response.TaskResponse;

/**
 * 
 * @author:luyz
 * @time:2016-6-24 下午05:02:36
 * @version:
 */
public class GeekparkParser extends BaseActor {
	private Logger logger = LoggerFactory.getLogger("deadletter");

	@Inject
	private GeekparkDao geekparkDao;

	@Override
	public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
		if (message instanceof TaskResponse) {
			TaskResponse response = (TaskResponse) message;
			String html = response.getResponseMeta().getBody();
			try {
				Document doc = Jsoup.parse(html);
				String name = doc.getElementsByClass("topic-cover").select("img").attr("alt").toString();
				if(name.length() > 30){
					name = name.substring(0, 30);
				}
				String fincnt = doc.getElementsByClass("article-content").toString();
				String url = response.getResponseMeta().getUrl();
				String[] split = url.split("/");
				url = split[split.length-1];
				geekparkDao.save(url, name ,fincnt);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("内容转换出错了：" + e);
			}
		}

	}

}
