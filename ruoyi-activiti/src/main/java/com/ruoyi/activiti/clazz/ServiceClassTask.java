package com.ruoyi.activiti.clazz;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @author 一只闲鹿
 */
@Component
public class ServiceClassTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("执行了服务任务");
    }
}
