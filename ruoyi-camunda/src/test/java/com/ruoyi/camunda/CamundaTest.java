package com.ruoyi.camunda;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaohuan
 * @version 1.0
 **/
public class CamundaTest {
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private IdentityService identityService;
    private FormService formService;
    private HistoryService historyService;
    private ManagementService managementService;
    private FilterService filterService;
    private ExternalTaskService externalTaskService;
    private CaseService caseService;
    private DecisionService decisionService;

    @Test
    public void engineTest() {
        processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/ruoyi-vue-camunda?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("yh199210")
                .setJobExecutorActivate(true)
                .buildProcessEngine();

//        repositoryService = processEngine.getRepositoryService();
//        runtimeService = processEngine.getRuntimeService();
//        taskService = processEngine.getTaskService();
//        identityService = processEngine.getIdentityService();
//        formService = processEngine.getFormService();
//        historyService = processEngine.getHistoryService();
//        managementService = processEngine.getManagementService();
//        filterService = processEngine.getFilterService();
//        externalTaskService = processEngine.getExternalTaskService();
//        caseService = processEngine.getCaseService();
//        decisionService = processEngine.getDecisionService();
//
//        // 查询流程定义
//        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
//                .processDefinitionKey("invoice")
//                .orderByProcessDefinitionVersion()
//                .asc()
//                .list();
//
//        // 启动流程实例
//        Map<String, Object> variables = new HashMap<String,Object>();
//        variables.put("creditor", "Nice Pizza Inc.");
//        ProcessInstance instance = runtimeService.startProcessInstanceByKey("invoice", variables);
    }

}
