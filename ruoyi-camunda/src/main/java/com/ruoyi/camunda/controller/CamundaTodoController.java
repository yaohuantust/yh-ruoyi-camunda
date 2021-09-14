package com.ruoyi.camunda.controller;

import com.ruoyi.camunda.domain.TaskVo;
import com.ruoyi.camunda.service.IProcessService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * @author yaohuan
 * @version 1.0
 **/
@RestController
public class CamundaTodoController extends BaseController {
    @Autowired
    private IProcessService processService;

    @GetMapping("/camunda/process/taskList")
    public TableDataInfo taskList(TaskVo taskVo) {
        return processService.findTodoTasks(taskVo);
    }

    /**
     * 办理任务
     */
    @PostMapping( "/camunda/process/complete")
    public AjaxResult complete(String taskId, String instanceId, String variables) {
        processService.complete(taskId, instanceId, variables);
        return success();
    }
}
