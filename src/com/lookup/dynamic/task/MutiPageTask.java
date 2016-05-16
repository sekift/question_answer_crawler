/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.task;

import com.lookup.dynamic.request.TaskRequestMeta;

/**
 * Created by Administrator on 2015/7/16.
 */
public abstract class MutiPageTask extends BaseTask {
    protected String pageParam;       //分页参数

    protected TaskRequestMeta requestMeta;
    protected boolean isPaged = false;
    protected boolean isFixPageSize = false;
    protected long delay = 10;     //延时
    protected long period = 10;   //运行周期  s

    public boolean isFixPageSize() {
        return isFixPageSize;
    }

    public void setFixPageSize(boolean isFixPageSize) {
        this.isFixPageSize = isFixPageSize;
    }

    public boolean isPaged() {
        return isPaged;
    }

    public void setPaged(boolean isPaged) {
        this.isPaged = isPaged;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }
}
