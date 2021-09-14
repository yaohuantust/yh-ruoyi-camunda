package com.ruoyi.activiti.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.BizLeave;
import com.ruoyi.activiti.service.IBizLeaveService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <b>监听器使用范例</b>：销假后处理器
 * <p>
 * 设置销假时间
 * </p>
 * <p>
 * 使用Spring代理，可以注入Bean，管理事物
 * </p>
 *
 * @author HenryYan
 */
@Component
@Transactional
public class ReportBackEndProcessor implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Autowired
    IBizLeaveService bizLeaveService;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate
     * .DelegateTask)
     */
    public void notify(DelegateTask delegateTask) {
        try {
            System.out.println("执行了监听器 ReportBackEndProcessor");
            BizLeave leave = bizLeaveService.selectBizLeaveById(new Long(delegateTask.getExecution().getProcessInstanceBusinessKey()));
            JSONObject formData = (JSONObject) delegateTask.getVariable("formData");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            leave.setRealityStartTime(sdf.parse(formData.get("realityStartTime").toString() + " 00:00:00"));
            leave.setRealityEndTime((sdf.parse(formData.get("realityEndTime").toString() + " 00:00:00")));
            bizLeaveService.updateBizLeave(leave);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
