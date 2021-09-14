package com.ruoyi.camunda.service;

import com.ruoyi.camunda.domain.BizLeave;

import java.util.List;

/**
 * @author yaohuan
 * @version 1.0
 * @date 2021/09/07 22:28
 **/
public interface CamundaLeaveService {
    public BizLeave selectBizLeaveById(Long id);
    public int insertBizLeave(BizLeave bizLeave);
    public List<BizLeave> selectBizLeaveList(BizLeave bizLeave);
    public int updateBizLeave(BizLeave bizLeave);
}
