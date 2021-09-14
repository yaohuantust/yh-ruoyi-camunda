package com.ruoyi.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaohuan
 * @version 1.0
 **/
public class ProcessTest {
    private ProcessEngine processEngine;
    private IdentityService identityService;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private HistoryService historyService;


    @Before
    public void initProcessEngine() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/mybatis-demo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("yh199210");
        // public static final String DB_SCHEMA_UPDATE_FALSE = "false"; // 不能自动创建表，需要表存在
        // public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop"; // 先删除表，再创建表
        // public static final String DB_SCHEMA_UPDATE_TRUE = "true"; // 如果表不存在，自动创建表
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
    }

    /**
     * 流程启动
     * act_hi_procinst
     */
    @Test
    public void flowStart(){
        String applyUserId = "cxy";
        identityService.setAuthenticatedUserId(applyUserId);
        runtimeService.startProcessInstanceByKey("leave");
    }

    /**
     * 获取我的待办
     * act_ru_task
     */
    @Test
    public void taskToDo(){
        // 根据当前人的id查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("yao")
                .active()
                .list();

        // 根据当前人未签收的任务
        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("yao")
                .active()
                .list();

        List<Task>allTaskList = new ArrayList<>();
        allTaskList.addAll(list);
        allTaskList.addAll(list1);
    }

    /**
     * 完成我的待办
     * act_ru_task
     * act_hi_comment
     */
    @Test
    public void completeTask(){
        // 根据当前人的id查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("yao")
                .active()
                .list();

        // 根据当前人未签收的任务
        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("yao")
                .active()
                .list();

        List<Task>allTaskList = new ArrayList<>();
        allTaskList.addAll(list);
        allTaskList.addAll(list1);

        Task task = allTaskList.get(0);

        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .singleResult();

        // 审批人
        identityService.setAuthenticatedUserId("yao");
        taskService.addComment(task.getId(),processInstance.getId(),"【同意】");

        Map<String,Object> variables=new HashMap<>();
        variables.put("deptLeaderApproved",true);//根据leave.bpmn流程变量

        taskService.complete(task.getId(),variables);
    }

    /**
     * 我的已办
     * act_hi_actinst
     */
    @Test
    public void complete(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee("yao")
                .finished()
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            String name = historicTaskInstance.getName();
            String assignee = historicTaskInstance.getAssignee();
            System.out.println(assignee); // 办理人
            System.out.println(name); // 在办任务
        }
    }

    /**
     * 审批历史批注
     */
    @Test
    public void historyComment(){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .singleResult();

        List<HistoricActivityInstance> userTask = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .activityType("userTask")
                .finished()
                .list();

        for (HistoricActivityInstance historicActivityInstance : userTask) {
            List<Comment> comment = taskService.getTaskComments(historicActivityInstance.getTaskId(), "comment");
            System.out.println("历史批注->"+historicActivityInstance.getAssignee()+"comment->"+comment.get(0).getFullMessage());
        }
    }
}
