package com.lookup.dynamic.task.imp;

/**
 * 能重载的TaskVO
 * 
 * @author:Administrator
 * @time:2016-4-7 下午03:53:37
 * @version:
 */
public class TaskVO {
	private int pagesFirst; /*开始爬取的页数*/
	private int pagesSecond;/*结束爬取的页数*/
	private int sleepFirst;/*随机休眠的开始秒数*/
	private int sleepSecond;/*随机休眠的结束秒数*/

	public int getPagesFirst() {
		return pagesFirst;
	}

	public void setPagesFirst(int pagesFirst) {
		this.pagesFirst = pagesFirst;
	}

	public int getPagesSecond() {
		return pagesSecond;
	}

	public void setPagesSecond(int pagesSecond) {
		this.pagesSecond = pagesSecond;
	}

	public int getSleepFirst() {
		return sleepFirst;
	}

	public void setSleepFirst(int sleepFirst) {
		this.sleepFirst = sleepFirst;
	}

	public int getSleepSecond() {
		return sleepSecond;
	}

	public void setSleepSecond(int sleepSecond) {
		this.sleepSecond = sleepSecond;
	}

}
