/*
 * developer spirit_demon  at 2015.
 */
package com.lookup.dynamic;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lookup.dynamic.task.imp.StackOverFlowTask;

/**
 * A main class to start up the application.
 */
public class Main {
	public static ApplicationContext context;

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure("config/log4j.properties");
		context = new ClassPathXmlApplicationContext("applicationContext-main.xml");

		StackOverFlowTask task = (StackOverFlowTask) context.getBean("stackOverFlowTask");
		task.execute();

//		ZhidxTask zhidxTask = (ZhidxTask) context.getBean("zhidxTask");
//		zhidxTask.execute();
//		
//		GeekparkTask geekparkTask = (GeekparkTask) context.getBean("geekparkTask");
//		geekparkTask.execute();
		
//		GoodListTask goodListTask = (GoodListTask) context.getBean("goodListTask");
//		goodListTask.execute();

		// System.exit(0);
	}
}
