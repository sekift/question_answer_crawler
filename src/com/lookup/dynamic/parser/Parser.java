/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.parser;

import java.util.List;

/**
 * Created by Administrator on 2015/9/1 0001.
 */
public interface Parser<T> {
	public List<T> parse(String targetString);
}
