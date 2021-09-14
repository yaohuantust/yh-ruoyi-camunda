package com.ruoyi.camunda.controller;

import com.ruoyi.camunda.domain.BizLeave;
import com.ruoyi.camunda.service.CamundaLeaveService;
import com.ruoyi.camunda.service.IProcessService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * @author yaohuan
 * @version 1.0
 **/
@RestController
public class CamundaLeaveController  extends BaseController  {

    @Autowired
    private CamundaLeaveService camundaLeaveService;

    @Autowired
    private IProcessService processService;

    @PostMapping("/camunda/leave/add")
    public AjaxResult add(@RequestBody BizLeave bizLeave)
    {
        return toAjax(camundaLeaveService.insertBizLeave(bizLeave));
    }

    @GetMapping("/camunda/leave/list")
    public TableDataInfo list(BizLeave bizLeave)
    {
        startPage();
        List<BizLeave> list = camundaLeaveService.selectBizLeaveList(bizLeave);
        return getDataTable(list);
    }

    @PostMapping( "/camunda/leave/submitApply/{id}")
    public AjaxResult submitApply(@PathVariable Long id) {
        try {
            BizLeave bizLeave = camundaLeaveService.selectBizLeaveById(id);
            processService.submitApply(bizLeave, "leave_demo");
            camundaLeaveService.updateBizLeave(bizLeave);
        } catch (Exception e) {
            e.printStackTrace();
            return error("提交申请出错：" + e.getMessage());
        }
        return success();
    }

}
