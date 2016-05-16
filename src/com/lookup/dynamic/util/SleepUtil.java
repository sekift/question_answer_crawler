package com.lookup.dynamic.util;

/**
 * @author 作者:sekift
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2015-10-25 上午02:17:28 类说明:[停止的时间，随机为秒]
 */
public class SleepUtil {
	/**
	 * 睡眠，时间单位为s,两个数min、max不论顺序
	 * 
	 * @param min
	 * @param max
	 */
	public static void sleep(int min, int max) {
		try {
			//由于不必准确，故使用左移（1024）
			Thread.sleep(randomInt(min, max)<<10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 产生一个a到b的随机数,int
	 * @param a
	 * @param b
	 * @return
	 */
	public static int randomInt(double a, double b) {
		return new Double(Math.abs(b - a) * Math.random() + Math.min(a, b)).intValue();
	}

}
