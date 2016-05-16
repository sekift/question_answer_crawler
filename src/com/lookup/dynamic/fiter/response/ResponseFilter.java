/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.fiter.response;

import com.lookup.dynamic.fiter.FilterChain;
import com.lookup.dynamic.response.TaskResponse;

/**
 * Created by Administrator on 2015/6/17.
 */
public interface ResponseFilter {

    void doFilter(TaskResponse resp, FilterChain chain);

}
