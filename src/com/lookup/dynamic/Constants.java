package com.lookup.dynamic;

/**
 * 常量表，默认线下环境
 * 
 * @author:luyz
 * @time:2016-6-24 下午03:10:46
 * @version:
 */
public class Constants {
	/**
	 * 声明：线上、线下环境切换除了这里外，首先需要切换config/log4j文件的路径
	 */
	// 是否线上环境
	public static boolean isOnline = false; // false:否,true:是

	
	// 配置文件路径，因为调试需要在两者之间切换  /*ClientConfigService*/
	// 因为要加URL 所以使用file协议
	public static String filePath = "file:D:\\workspace\\question_answer_crawler\\config\\client.xml"; // 否 /config/client.xml 

	// 配置文件，代理IP路径
	public static String proxyFilePath = "D:\\workspace\\question_answer_crawler\\config";// 否
	
	// 配置文件，User-Agent路径
	public static String userAgentsPath = "D:\\workspace\\question_answer_crawler\\config";// 否
	
	//zhidx 保存文件的目录
	public static String zhidxFilePath = "D:\\workspace\\question_answer_crawler\\zhidx";// 否
	
	//geekpark 保存文件的目录
	public static String geekparkFilePath = "D:\\workspace\\question_answer_crawler\\geekpark";// 否
	
	static {
		if (isOnline) { //线上环境
			filePath = "file:/www/client/crawler/config/client.xml";
			proxyFilePath = "/www/client/crawler/config";
			userAgentsPath = "/www/client/crawler/config";
			zhidxFilePath = "/www/client/crawler/zhidx";
			geekparkFilePath = "/www/client/crawler/zhidx";
		}
	}
	
	//zhidx网站首页
	public static final String ZHIDX_INDEX = "http://zhidx.com/p/category/daily";
	
	//极客网站首页
	public static final String GEEKPARK_INDEX = "http://www.geekpark.net";

}
