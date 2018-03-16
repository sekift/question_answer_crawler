/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.task.imp;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;

import akka.actor.ActorRef;

import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.request.TaskRequestMeta;
import com.lookup.dynamic.task.SingleTask;
import com.lookup.dynamic.util.DateUtil;
import com.lookup.dynamic.util.SleepUtil;

/**
 * Created by Administrator on 2015/6/17.
 */
@Qualifier
public class StackOverFlowTask extends SingleTask {
	@Inject
	private ActorRef stackOverFlowActor;

	public StackOverFlowTask() {

	}

	TaskVO vo = ReloadValue.getTaskVO();

	// @Scheduled(cron="0 0/1 *  * * ? ") //每一分钟执行一次
	public void execute() {
		int i = 0;
		//初始化参数
		ReloadValue.reSetPages();
		ReloadValue.reSetSleepTime();
		
		for (int index = vo.getPagesFirst(); index >= vo.getPagesSecond(); index--) {
			TaskRequest request = new TaskRequest();
			request.setPaged(true);
			TaskRequestMeta requestMetaPage = requestMeta.clone();
			String currentUrl = requestMeta.getUrl() + "?pagesize=50&sort=newest&page=" + index;
			requestMetaPage.setUrl(currentUrl);
			request.setRequestMeta(requestMetaPage);
			System.out.println(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss") + " : " + currentUrl);
			
			stackOverFlowActor.tell(request, ActorRef.noSender()); 
			// 睡眠策略：每一页休息20-40秒；每64页再休息8-10分钟；每256页再休息8-10分钟
			i++; 
			SleepUtil.sleepBySecond(vo.getSleepFirst(), vo.getSleepSecond());
			
			/*if (i > 0 && i % 64 == 0) {
				System.out.println("------------sleeping for 8-10 minute------------");
				SleepUtil.sleepByMinute(8, 10);
				index += 1;
			}
			if (i > 0 && i % 256 == 0) {
				System.out.println("------------sleeping for 8-10 minute------------");
				SleepUtil.sleepByMinute(8, 10);
				index += 1;
			}*/

			// 重置参数
			ReloadValue.reSetPages();
			ReloadValue.reSetSleepTime();
		}
	}

}
