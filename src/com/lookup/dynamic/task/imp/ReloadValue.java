package com.lookup.dynamic.task.imp;

import java.util.Map;

import com.lookup.dynamic.config.imp.ClientConfigService;

/**
 * 
 * @author:Administrator
 * @time:2016-4-7 下午04:43:36
 * @version:
 */
public class ReloadValue {
	private static TaskVO vo = new TaskVO();

	public static TaskVO getTaskVO() {
		return vo;
	}

	/**
	 * 更新页数
	 */
	public static void reSetPages() {
		Map<String, Object> configSOF = ClientConfigService.getInstance().getClientProperty().getItem("stackoverflow");
		String pages = (String) configSOF.get("pages");
		int first = Integer.valueOf(pages.split(":")[0]);
		int second = Integer.valueOf(pages.split(":")[1]);
		if (first != vo.getPagesFirst()) {
			vo.setPagesFirst(first);
			System.out.println("pagefirst change,value="+first);
		}
		if (second != vo.getPagesSecond()) {
			vo.setPagesSecond(second);
			System.out.println("pagesecond change,value="+second);
		}
	}

	/**
	 * 更新时间
	 */
	public static void reSetSleepTime() {
		Map<String, Object> configSleep = ClientConfigService.getInstance().getClientProperty().getItem("sleeptime");
		String sleep = (String) configSleep.get("sleep");
		int first = Integer.valueOf(sleep.split(":")[0]);
		int second = Integer.valueOf(sleep.split(":")[1]);
		if (first != vo.getSleepFirst()) {
			vo.setSleepFirst(first);
			System.out.println("sleepfirst change,value="+first);
		}
		if (second != vo.getSleepSecond()) {
			vo.setSleepSecond(second);
			System.out.println("sleepsecond change,value="+second);
		}
	}
}
