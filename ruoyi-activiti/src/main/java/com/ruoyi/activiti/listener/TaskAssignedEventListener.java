package com.ruoyi.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.stereotype.Component;

/**
 * 任务被分配事件监听器 demo
 * @author 一只闲鹿
 */
@Component
public class TaskAssignedEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        System.out.println("执行了事件监听器");
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
