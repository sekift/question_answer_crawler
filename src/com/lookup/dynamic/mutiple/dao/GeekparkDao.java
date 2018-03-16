package com.lookup.dynamic.mutiple.dao;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lookup.dynamic.Constants;

/**
 * 
 * @author:luyz
 * @time:2016-6-24 下午05:04:06
 * @version:
 */
public class GeekparkDao {
	private Logger logger = LoggerFactory.getLogger("deadletter");

	/**
	 * 插入或更新数据 到文件
	 * 
	 * @param args
	 */
	public void save(String url, String name, String context) {
		File file = new File(Constants.geekparkFilePath + File.separatorChar + url+ "----" + name + ".txt");
		Iterator<File> files = FileUtils.iterateFiles(new File(Constants.geekparkFilePath), new String[]{"txt"} , false);
		while(files.hasNext()){
			System.out.println(files.next().getName());
		}
		
		try {
			if (!file.exists()) {
				FileUtils.writeStringToFile(file, context, "utf-8", false);
				System.out.println("geekpark写入文件，" + file + "，成功");
			}else{
			    System.out.println("geekpark写入文件，" + file + "，已存在。");
			}
		} catch (IOException e) {
			logger.error("geekpark写入文件错误，路径为=" + file, e);
			e.printStackTrace();
		}
	}

}
