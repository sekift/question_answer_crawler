package com.lookup.dynamic.config.base;

public interface Config {
	
	<T> T getItem(String name); 
	
	<T> T getItem(String name, T defaultValue);
	
}
