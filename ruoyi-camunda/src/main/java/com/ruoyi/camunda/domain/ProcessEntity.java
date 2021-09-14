package com.ruoyi.camunda.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author 一只闲鹿
 */
//@Data
public class ProcessEntity extends BaseEntity {

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSuspendState() {
        return suspendState;
    }

    public void setSuspendState(String suspendState) {
        this.suspendState = suspendState;
    }

    public String getSuspendStateName() {
        return suspendStateName;
    }

    public void setSuspendStateName(String suspendStateName) {
        this.suspendStateName = suspendStateName;
    }

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 实例状态 1 激活 2 挂起
     */
    private String suspendState;

    /**
     * 已激活/已挂起
     */
    private String suspendStateName;

}
