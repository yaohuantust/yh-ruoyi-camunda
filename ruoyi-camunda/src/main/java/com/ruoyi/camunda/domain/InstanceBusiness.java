package com.ruoyi.camunda.domain;

/**
 * 流程实例业务关系表
 * @author 一只闲鹿
 */
//@Data
public class InstanceBusiness {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    private Long id;

    private String instanceId;

    private String businessKey;

    private String module;

}
