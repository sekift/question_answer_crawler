package com.lookup.dynamic.config.imp;

import java.net.URL;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lookup.dynamic.config.base.Config;
import com.lookup.dynamic.config.base.ConfigService;
import com.lookup.dynamic.config.common.XmlProperties;

public final class ClientConfigService implements ConfigService {
	
	private static Logger logger = LoggerFactory.getLogger("proxy");

	static XmlProperties client = null;
	
	private static ClientConfigService instance = new ClientConfigService();
	
	static {
		initialize();
	}
	
	private static void initialize() {
		//文件路径
		String filePath = "file:/www/client/crawler/config/client.xml";
		
		try {
			//设置域名查询缓存的有效时间，JDK默认是永远有效，这样一来域名IP重定向必须重启JVM，这里修改为3S有效
			Security.setProperty("networkaddress.cache.ttl", "3");
			
			client = new XmlProperties();
			URL url = new URL(filePath);
//			URL url = ClientConfigService.class.getResource("/config/client.xml");
			client.setSourceURL(url);
			client.setTimingReload(true);
			client.initialize();
	
			logger.info("配置服务初始化成功.");
		} catch (Exception ex) { 
			logger.error("配置服务初始化错误."+ex);
		}
	}

	public static ClientConfigService getInstance() {		
		return instance;
	}

	public Config getClientProperty() {
		return client;
	}
	
}
