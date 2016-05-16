/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.fiter.request;

import com.lookup.dynamic.fiter.FilterChain;
import com.lookup.dynamic.request.TaskRequest;

/**
 * Created by Administrator on 2015/6/17.
 */
public interface RequestFilter {
    void doFilter(TaskRequest req, FilterChain chain);
}
