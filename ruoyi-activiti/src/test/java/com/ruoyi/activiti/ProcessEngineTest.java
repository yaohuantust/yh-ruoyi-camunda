package com.ruoyi.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * @author yaohuan
 * @version 1.0
 **/
public class ProcessEngineTest {

    @Test
    public void testProcessEngine(){
        ProcessEngineConfiguration processEngineConfiguration=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti-demo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("yh199210");
        // public static final String DB_SCHEMA_UPDATE_FALSE = "false"; // 不能自动创建表，需要表存在
        // public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop"; // 先删除表，再创建表
        // public static final String DB_SCHEMA_UPDATE_TRUE = "true"; // 如果表不存在，自动创建表
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine=processEngineConfiguration.buildProcessEngine();
        assertNotNull(processEngine);
    }
}
