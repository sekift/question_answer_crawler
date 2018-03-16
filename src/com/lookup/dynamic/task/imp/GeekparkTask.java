package com.lookup.dynamic.task.imp;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;

import akka.actor.ActorRef;

import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.request.TaskRequestMeta;
import com.lookup.dynamic.task.SingleTask;
import com.lookup.dynamic.util.DateUtil;

/**
 * zhidx任务
 * 
 * @author:luyz
 * @time:2016-6-24 下午04:51:46
 * @version:
 */
@Qualifier
public class GeekparkTask extends SingleTask{
	@Inject
	private ActorRef geekparkActor;

	public GeekparkTask() {

	}

	//@Scheduled(cron="0 0 0 0 * ? ") //每1小时执行一次
	public void execute() {
		TaskRequest request = new TaskRequest();
		request.setPaged(true);
		TaskRequestMeta requestMetaPage = requestMeta.clone();
		String currentUrl = requestMeta.getUrl();
		requestMetaPage.setUrl(currentUrl);
		request.setRequestMeta(requestMetaPage);
		System.out.println(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + " : " + currentUrl);

		geekparkActor.tell(request, ActorRef.noSender());
	}
}
