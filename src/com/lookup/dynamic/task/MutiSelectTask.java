/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.task;

import com.lookup.dynamic.request.TaskRequestMeta;

import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public abstract class MutiSelectTask extends BaseTask {
    protected List<TaskRequestMeta> requestMetaList;

    public List<TaskRequestMeta> getRequestMetaList() {
        return requestMetaList;
    }

    public void setRequestMetaList(List<TaskRequestMeta> requestMetaList) {
        this.requestMetaList = requestMetaList;
    }
}
