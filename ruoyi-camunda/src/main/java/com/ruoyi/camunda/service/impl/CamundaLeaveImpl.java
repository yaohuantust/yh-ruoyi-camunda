package com.ruoyi.camunda.service.impl;

import com.ruoyi.camunda.domain.BizLeave;
import com.ruoyi.camunda.mapper.BizLeaveMapper;
import com.ruoyi.camunda.service.CamundaLeaveService;
import com.ruoyi.camunda.service.IProcessService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yaohuan
 * @version 1.0
 **/
@Service
public class CamundaLeaveImpl implements CamundaLeaveService {
    @Autowired
    private BizLeaveMapper bizLeaveMapper;

    @Autowired
    private IProcessService processService;

    @Override
    public BizLeave selectBizLeaveById(Long id) {
        return bizLeaveMapper.selectBizLeaveById(id);
    }

    @Override
    public int insertBizLeave(BizLeave bizLeave) {
        bizLeave.setCreateBy(SecurityUtils.getUsername());
        bizLeave.setCreateTime(DateUtils.getNowDate());
        return bizLeaveMapper.insertBizLeave(bizLeave);
    }

    @Override
    public List<BizLeave> selectBizLeaveList(BizLeave bizLeave) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())) {
            bizLeave.setCreateBy(SecurityUtils.getUsername());
        }
        List<BizLeave> list = bizLeaveMapper.selectBizLeaveList(bizLeave);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                try {
                    processService.richProcessField(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return list;
    }

    @Override
    public int updateBizLeave(BizLeave bizLeave) {
        bizLeave.setUpdateTime(DateUtils.getNowDate());
        return bizLeaveMapper.updateBizLeave(bizLeave);
    }
}
