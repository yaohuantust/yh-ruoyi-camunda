package com.ruoyi.camunda.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.camunda.domain.InstanceBusiness;
import com.ruoyi.camunda.domain.TaskVo;
import com.ruoyi.camunda.mapper.TaskMapper;
import com.ruoyi.camunda.service.IProcessService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yaohuan
 * @version 1.0
 **/
@Service
@Transactional
public class ProcessServiceImpl implements IProcessService {
    protected final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public <T> void submitApply(T entity, String key) throws Exception {
        this.submitApply(entity, key, null);
    }

    @Override
    public <T> void submitApply(T entity, String key, Map<String, Object> variables) throws Exception {
        Class clazz = entity.getClass();

        Method getId = clazz.getDeclaredMethod("getId"); // 18
        Long id = (Long) getId.invoke(entity);
        System.out.println("????????????id???"+18);

        Method setApplyUserId = clazz.getDeclaredMethod("setApplyUserId", String.class);
        Method setApplyUserName = clazz.getDeclaredMethod("setApplyUserName", String.class);
        Method setApplyTime = clazz.getDeclaredMethod("setApplyTime", Date.class);
        Method setProcessKey = clazz.getDeclaredMethod("setProcessKey", String.class);

        Method setUpdateBy = clazz.getSuperclass().getSuperclass().getDeclaredMethod("setUpdateBy", String.class);
        Method setUpdateTime = clazz.getSuperclass().getSuperclass().getDeclaredMethod("setUpdateTime", Date.class);

        Method setInstanceId = clazz.getDeclaredMethod("setInstanceId", String.class);

        String username = SecurityUtils.getUsername();
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        Date now = new Date();

        // ????????????????????????
        setApplyUserId.invoke(entity, username);
        setApplyUserName.invoke(entity, nickName);
        setApplyTime.invoke(entity, now);
        setProcessKey.invoke(entity, key);
        setUpdateBy.invoke(entity, username);
        setUpdateTime.invoke(entity, now);

        // ?????????????????????????????????ID???????????????????????????ID?????????activiti:initiator???
        identityService.setAuthenticatedUserId(username);
        // ??????????????????????????? key
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(key, id + "", variables);

        // ???????????????????????????id??????
        setInstanceId.invoke(entity, instance.getId());

        // ??????????????????????????????
        InstanceBusiness ib = new InstanceBusiness();
        ib.setInstanceId(instance.getId());
        ib.setBusinessKey(id + "");
        ib.setModule(humpToLine(entity.getClass().getSimpleName()).substring(1));
        taskMapper.insertInstanceBusiness(ib);
    }

    /** ?????????????????? */
    private String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** ?????????????????? */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Pattern linePattern = Pattern.compile("_(\\w)");
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    @Override
    public <T> void richProcessField(T entity) throws Exception {
        Class clazz = entity.getClass();

        Method getInstanceId = clazz.getDeclaredMethod("getInstanceId");
        String instanceId = (String) getInstanceId.invoke(entity);

        Method setTaskId = clazz.getSuperclass().getDeclaredMethod("setTaskId", String.class);
        Method setTaskName = clazz.getSuperclass().getDeclaredMethod("setTaskName", String.class);
        Method setSuspendState = clazz.getSuperclass().getDeclaredMethod("setSuspendState", String.class);
        Method setSuspendStateName = clazz.getSuperclass().getDeclaredMethod("setSuspendStateName", String.class);

        // ????????????
        if (StringUtils.isNotBlank(instanceId)) {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(instanceId)
                    .list();    // ????????????????????????????????????????????????
            if (!CollectionUtils.isEmpty(taskList)) {
                TaskEntity task = (TaskEntity)taskList.get(0);
                setTaskId.invoke(entity, task.getId());
                if (task.getSuspensionState() == 2) {
                    setTaskName.invoke(entity, "?????????");
                    setSuspendState.invoke(entity, "2");
                    setSuspendStateName.invoke(entity, "?????????");
                } else {
                    setTaskName.invoke(entity, task.getName());
                    setSuspendState.invoke(entity, "1");
                    setSuspendStateName.invoke(entity, "?????????");
                }
            } else {
                // ????????????????????????
                List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(instanceId)
//                        .orderByTaskCreateTime()
                        .orderByHistoricActivityInstanceStartTime()
                        .desc()
                        .list();
                if (!CollectionUtils.isEmpty(list)) {
                    HistoricTaskInstance lastTask = list.get(0); // ?????????????????????????????????
                    if (StringUtils.isNotBlank(lastTask.getDeleteReason())) {
                        setTaskName.invoke(entity, "?????????");
                    } else {
                        setTaskName.invoke(entity, "?????????");
                    }
                    setTaskId.invoke(entity, "-1"); // ??????????????????????????????id???????????????-1
                } else {
                    // ????????????????????????????????????????????????instanceId?????????????????????
                    setTaskName.invoke(entity, "???????????????");
                    setTaskId.invoke(entity, "-2"); // ?????????????????????????????????????????????????????????
                }
            }
        } else {
            setTaskName.invoke(entity, "?????????");
        }
    }

    @Override
    public TableDataInfo findTodoTasks(TaskVo taskVo) {
        taskVo.setUserId(SecurityUtils.getUsername());
        taskVo.setOffset((taskVo.getPageNum() - 1) * taskVo.getPageSize());
        List<Map> tasks = taskMapper.findTodoList(taskVo);
        Integer count = taskMapper.findTodoCount(taskVo);

        List<TaskVo> taskVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tasks)) {
            tasks.forEach(task -> {
                TaskVo newTaskVo = new TaskVo();
                newTaskVo.setType("todo");
                newTaskVo.setUserId(SecurityUtils.getUsername());
                newTaskVo.setTaskId(task.get("ID_").toString());
                newTaskVo.setTaskName(task.get("NAME_").toString());
                newTaskVo.setInstanceId(task.get("PROC_INST_ID_").toString());
                newTaskVo.setSuspendState(task.get("SUSPENSION_STATE_").toString());
                newTaskVo.setCreateTime((Date) task.get("CREATE_TIME_"));
                if ("2".equals(newTaskVo.getSuspendState())) {
                    newTaskVo.setSuspendStateName("?????????");
                } else {
                    newTaskVo.setSuspendStateName("?????????");
                }
                newTaskVo.setAssigneeName(userMapper.selectUserByUserName(newTaskVo.getUserId()).getNickName());

                // ????????????????????????????????? map ???
                Map ibMap = taskMapper.selectInstanceBusinessByInstanceId(task.get("PROC_INST_ID_").toString());
                if (!CollectionUtils.isEmpty(ibMap)) {
                    Map<String, Object> formData = taskMapper.selectBusinessByBusinessKeyAndModule(ibMap.get("business_key").toString(), ibMap.get("module").toString());
                    if (!CollectionUtils.isEmpty(formData)) {
                        newTaskVo.setFormData(getLine2HumpMap(formData));
                    }
                }

                taskVos.add(newTaskVo);
            });
        }

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("????????????");
        rspData.setRows(taskVos);
        rspData.setTotal(count);

        return rspData;
    }

    @Override
    public void complete(String taskId, String instanceId, String variablesStr) {
        System.out.println("variables: " + variablesStr);
        Map<String, Object> variables = (Map<String, Object>) JSON.parse(variablesStr);
        String comment = variables.get("comment").toString();
        String pass = variables.get("pass").toString();
        try {
            variables.put("pass", "true".equals(pass));
            // ??????????????????????????????
            // p.s. ??????????????????????????? resolved ????????????????????????
            // ????????? complete ??????????????? resolved

            // ????????????????????????????????????????????????
            TaskEntity task = (TaskEntity) taskService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
            // DELEGATION_ ??? PENDING ??????????????????????????????
            if (task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)) {
                taskService.resolveTask(taskId, variables);
                // ?????????????????????
                String delegateUserName = userMapper.selectUserByUserName(SecurityUtils.getUsername()).getNickName();
                comment += "??????" + delegateUserName + "?????????";

                // ????????? OWNER_ ??? null ??????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isBlank(task.getOwner())) {
                    taskService.claim(taskId, SecurityUtils.getUsername());
                }
            } else {
                // ?????????????????????act_hi_taskinst ?????? assignee ??????????????? null
                taskService.claim(taskId, SecurityUtils.getUsername());
            }

            if (StringUtils.isNotEmpty(comment)) {
                identityService.setAuthenticatedUserId(SecurityUtils.getUsername());
                taskService.addComment(taskId, instanceId, comment);
            }

            taskService.complete(taskId, variables);
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
        }
    }

    @Override
    public TableDataInfo findDoneTasks(TaskVo taskVo) {
        taskVo.setUserId(SecurityUtils.getUsername());
        taskVo.setOffset((taskVo.getPageNum() - 1) * taskVo.getPageSize());
        List<Map> tasks = taskMapper.findDoneList(taskVo);
        Integer count = taskMapper.findDoneCount(taskVo);

        List<TaskVo> taskVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tasks)) {
            tasks.forEach(task -> {
                TaskVo newTaskVo = new TaskVo();
                newTaskVo.setType("done");
                newTaskVo.setUserId(SecurityUtils.getUsername());
                newTaskVo.setTaskId(task.get("ID_").toString());
                newTaskVo.setTaskName(task.get("NAME_").toString());
                newTaskVo.setInstanceId(task.get("PROC_INST_ID_").toString());
                newTaskVo.setAssignee(task.get("ASSIGNEE_").toString());
                newTaskVo.setStartTime((Date) task.get("START_TIME_"));
                newTaskVo.setEndTime((Date) task.get("END_TIME_"));
                newTaskVo.setAssigneeName(userMapper.selectUserByUserName(newTaskVo.getAssignee()).getNickName());

                // ????????????????????????????????? map ???
                Map ibMap = taskMapper.selectInstanceBusinessByInstanceId(task.get("PROC_INST_ID_").toString());
                if (!CollectionUtils.isEmpty(ibMap)) {
                    Map<String, Object> formData = taskMapper.selectBusinessByBusinessKeyAndModule(ibMap.get("business_key").toString(), ibMap.get("module").toString());
                    if (!CollectionUtils.isEmpty(formData)) {
                        newTaskVo.setFormData(getLine2HumpMap(formData));
                    }
                }

                taskVos.add(newTaskVo);
            });
        }

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("????????????");
        rspData.setRows(taskVos);
        rspData.setTotal(count);

        return rspData;
    }

    private Map<String, Object> getLine2HumpMap(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // key ?????????????????? apply_user_id ????????? applyUserId
            key = lineToHump(key).substring(0, 1).toLowerCase() + lineToHump(key).substring(1);
            newMap.put(key, value);
        }
        return newMap;
    }
}
