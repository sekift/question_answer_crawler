package com.lookup.dynamic.config.common;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import com.lookup.dynamic.config.base.Config;
import com.lookup.dynamic.util.XmlUtil;


/**
 * xml配置属性类
 */
@SuppressWarnings({"unchecked"})
public class XmlProperties extends ReloadableProperties implements Config {
	 
	private URL srcUrl = null;
	
	public void setSourceURL(URL url) {
		this.srcUrl = url;
	}
	
	@Override
	protected URL getSourceURL() {
		return srcUrl;
	} 
	
	@Override
	protected Map reload(InputStream in, Map tarStorage) {
		try {
			Map rtv = XmlUtil.toMap(in);
			return rtv;
		} catch (Exception ex) {
			ex.printStackTrace();
			return tarStorage;
		}
	}

	public <T> T getItem(String name) { 
		return (T) props.get(name);
	}

	public <T> T getItem(String name, T defaultValue) {
		
		T rtv = (T) props.get(name);
		return (null != rtv) ? rtv : defaultValue;
	}

}
