package com.ruoyi.camunda.service;


import com.ruoyi.camunda.domain.TaskVo;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;
import java.util.Map;

/**
 * @author 一只闲鹿
 */
public interface IProcessService {
    /**
     * 提交申请
     */
    <T> void submitApply(T entity, String key) throws Exception;

    <T> void submitApply(T entity, String key, Map<String, Object> variables) throws Exception;

    /**
     * 填充流程相关字段
     */
    <T> void richProcessField(T entity) throws Exception;


    /**
     * 我的待办
     */
    TableDataInfo findTodoTasks(TaskVo taskVo);

    /**
     * 办理任务
     */
    void complete(String taskId, String instanceId, String variables);

    /**
     * 我的已办
     */
    TableDataInfo findDoneTasks(TaskVo taskVo);

}
