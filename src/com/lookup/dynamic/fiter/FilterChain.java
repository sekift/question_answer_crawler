/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.fiter;

import com.lookup.dynamic.fiter.request.RequestFilter;
import com.lookup.dynamic.fiter.response.ResponseFilter;
import com.lookup.dynamic.request.TaskRequest;
import com.lookup.dynamic.response.TaskResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/17.
 */
public class FilterChain {

    private List<RequestFilter> upFilters = new ArrayList<RequestFilter>();

    private List<ResponseFilter> downFilters = new ArrayList<ResponseFilter>();
    //调用链上的过滤器时，记录过滤器的位置用
    private int index = 0;

    public FilterChain addUpFilter(RequestFilter f) {
        upFilters.add(f);
        return this;
    }

    public FilterChain addDownFilter(ResponseFilter f) {
        downFilters.add(f);
        return this;
    }

    public void doFilter(TaskRequest req) {
        if (index == upFilters.size()) return;
        //得到当前过滤器
        RequestFilter f = upFilters.get(index);
        index++;

        f.doFilter(req, this);
    }

    public void doFilter(TaskResponse res) {
        if (index == downFilters.size()) return;
        //得到当前过滤器
        ResponseFilter f = downFilters.get(index);
        index++;

        f.doFilter(res, this);
    }


}
