package com.ruoyi.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author 一只闲鹿
 */
@Component
public class ServiceTaskExecListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("执行了 ServiceTaskExecListener，这里处理【相关人员知会留存】业务");
    }
}
