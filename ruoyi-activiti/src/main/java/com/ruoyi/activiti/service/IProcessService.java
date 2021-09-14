package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.HistoricActivity;
import com.ruoyi.activiti.domain.TaskVo;
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
     * 查询审批历史列表
     */
    List<HistoricActivity> selectHistoryList(HistoricActivity historicActivity);

    /**
     * 我的待办
     */
    TableDataInfo findTodoTasks(TaskVo taskVo);

    /**
     * 我的已办
     */
    TableDataInfo findDoneTasks(TaskVo taskVo);

    /**
     * 办理任务
     */
    void complete(String taskId, String instanceId, String variables);

    /**
     * 转办任务
     */
    void delegate(String taskId, String fromUser, String delegateToUser);

    void cancelApply(String instanceId, String deleteReason);

    /**
     * 激活/挂起流程实例
     */
    void suspendOrActiveApply(String instanceId, String suspendState);

}
