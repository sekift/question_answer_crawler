package com.lookup.dynamic.config.imp;

import org.apache.log4j.Logger;

import com.lookup.dynamic.config.common.XmlProperties;

public class ConfigUtil {

	private static final Logger logger = Logger.getLogger(ConfigUtil.class);

	private static XmlProperties appConfig = null;

	static {

		// 加载应用配置数据
		appConfig = new XmlProperties();
		appConfig.setSourceURL(ConfigUtil.class
				.getResource("/config/client.xml"));
		appConfig.setTimingReload(false);
		appConfig.initialize();
	}

	public static Object getConfigValue(String key) {
		Object result = null;
		try {
			if (appConfig != null) {
				result = appConfig.getValue(key);
			}
		} catch (Exception e) {
			logger.error("获取应用配置值出错！", e);
		}
		return result;
	}

}


