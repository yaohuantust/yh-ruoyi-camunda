package com.ruoyi.camunda.controller;

import com.ruoyi.camunda.domain.TaskVo;
import com.ruoyi.camunda.service.IProcessService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaohuan
 * @version 1.0
 **/
@RestController
public class CamundaDoneController extends BaseController {

    @Autowired
    private IProcessService processService;

    /**
     * 我的已办列表
     */
    @GetMapping("/camunda/process/taskDoneList")
    public TableDataInfo taskDoneList(TaskVo taskVo) {
        return processService.findDoneTasks(taskVo);
    }
}
