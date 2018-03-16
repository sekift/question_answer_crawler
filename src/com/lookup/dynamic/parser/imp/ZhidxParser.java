package com.lookup.dynamic.parser.imp;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import com.lookup.dynamic.actor.base.BaseActor;
import com.lookup.dynamic.mutiple.dao.ZhidxDao;
import com.lookup.dynamic.response.TaskResponse;

/**
 * 
 * @author:luyz
 * @time:2016-6-24 下午05:02:36
 * @version:
 */
public class ZhidxParser extends BaseActor {
	private Logger logger = LoggerFactory.getLogger("deadletter");

	@Inject
	private ZhidxDao zhidxDao;

	@Override
	public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
		if (message instanceof TaskResponse) {
			TaskResponse response = (TaskResponse) message;
			String html = response.getResponseMeta().getBody();
			try {
				Document doc = Jsoup.parse(html);
				//日期-题目
				Elements eles = doc.getElementsByClass("finTit");
				String date = null;
				String title = null;
				for(Element ele : eles){
					title = ele.select("h1").text();
					date = ele.getElementsByClass("time").text().replace("/", "-");
					break;
				}
				
				//内容
				String content = doc.getElementsByClass("finCnt").toString();
				zhidxDao.save(date, title, content);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("内容转换出错了：" + e);
			}
		}

	}

}
