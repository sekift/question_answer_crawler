/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.task;

import com.lookup.dynamic.request.TaskRequestMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2015/7/16.
 */
public class BaseTask implements Task {

    protected Logger logger = LoggerFactory.getLogger("task");
    protected String taskName;   //任务名称
    protected String taskType;  //任务类型

    protected TaskRequestMeta requestMeta;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }
}
