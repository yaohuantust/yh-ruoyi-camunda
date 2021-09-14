package com.ruoyi.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author yaohuan
 * @version 1.0
 **/
public class ProcessDeploymentTest {

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
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/ruoyi-vue-process-test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true");
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
     * act_id_user
     */
    @Test
    public void initUser() {
        User user = identityService.newUser("zhangsan");
        user.setFirstName("张三");
        identityService.saveUser(user);

        User user1 = identityService.newUser("lisi");
        user1.setFirstName("李四");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("wagnwu");
        user2.setFirstName("王五");
        identityService.saveUser(user2);

        assertEquals(3, identityService.createUserQuery().count());
    }

    /**
     * act_id_group
     */
    @Test
    public void initGroup() {
        Group group = identityService.newGroup("employee");
        group.setName("普通员工");
        group.setType("assigment");
        identityService.saveGroup(group);

        Group group1 = identityService.newGroup("deptLeader");
        group1.setName("部门领导");
        group1.setType("assigment");
        identityService.saveGroup(group1);

        Group group2 = identityService.newGroup("hr");
        group2.setName("人事");
        group2.setType("assigment");
        identityService.saveGroup(group2);

        assertEquals(3, identityService.createGroupQuery().count());
    }

    /**
     * act_id_membership
     */
    @Test
    public void membershipTest() {
        identityService.createMembership("zhangsan", "employee");
        identityService.createMembership("lisi", "deptLeader");
        identityService.createMembership("wagnwu", "hr");
    }

    /**
     * 流程部署
     * act_re_deployment
     * act_ge_bytearray
     * act_re-procdef
     */
    @Test
    public void deployTest() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave_1606921250782.bpmn")
                .deploy();
        assertNotNull(deploy);
    }


    /**
     * 流程启动
     * act_hi_actinst
     * act_hi_procinst
     * act_hi_identitylink
     * act_hi_taskinst
     * act_hi_varinst
     * <p>
     * act_ru_excution
     * act_ru_identitylink
     * act_ru_task
     * act_ru_variable
     */
    @Test
    public void flowStart() {
        String applyUserId = "zhangsan";
        identityService.setAuthenticatedUserId(applyUserId);
        runtimeService.startProcessInstanceByKey("leave");
    }


    /**
     * 获取我的待办
     * act_ru_task
     */
    @Test
    public void taskToDo() {
        // 根据当前人的id查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("wagnwu")
                .active()
                .list();

        // 根据当前人未签收的任务
        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("wagnwu")
                .active()
                .list();

        List<Task> allTaskList = new ArrayList<>();
        allTaskList.addAll(list);
        allTaskList.addAll(list1);
        for (Task task : allTaskList) {
            System.out.println("任务名称："+task.getName());
        }
    }

    /**
     * 完成我的待办
     * act_ru_task
     * act_hi_comment
     */
    @Test
    public void completeTask() {
        // 根据当前人的id查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskAssignee("wagnwu")
                .active()
                .list();

        // 根据当前人未签收的任务
        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("wagnwu")
                .active()
                .list();

        List<Task> allTaskList = new ArrayList<>();
        allTaskList.addAll(list);
        allTaskList.addAll(list1);

        Task task = allTaskList.get(0);

        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .singleResult();

        // 审批人
        identityService.setAuthenticatedUserId("wagnwu");
        taskService.addComment(task.getId(), processInstance.getId(), "【不同意】");

        Map<String, Object> variables = new HashMap<>();
        variables.put("pass", true);//根据leave.bpmn流程变量

        // 只有签收任务，act_hi_taskinst 表的assignee 字段才不为空
        taskService.claim(task.getId(), "wagnwu");
        taskService.complete(task.getId(), variables);
    }

    /**
     * 我的已办
     * act_hi_actinst
     */
    @Test
    public void complete() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee("wagnwu")
                .finished()
                .list();

        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskCandidateUser("wagnwu")
                .finished()
                .list();

        List<HistoricTaskInstance> list2 = new ArrayList<>();
        list2.addAll(list);
        list2.addAll(list1);
        for (HistoricTaskInstance historicTaskInstance : list2) {
            String taskName = historicTaskInstance.getName();
            System.out.println("已办任务:---------->>>>>>>" + taskName);
        }
    }


    /**
     * 审批历史批注
     */
    @Test
    public void historyComment() {
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
            System.out.println("历史批注->" + historicActivityInstance.getAssignee() + "comment->" + comment.get(0).getFullMessage());
        }
    }
}
