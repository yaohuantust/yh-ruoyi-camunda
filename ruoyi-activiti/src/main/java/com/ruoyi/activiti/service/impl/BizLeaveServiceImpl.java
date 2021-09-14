package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.BizLeaveMapper;
import com.ruoyi.activiti.domain.BizLeave;
import com.ruoyi.activiti.service.IBizLeaveService;
import org.springframework.util.CollectionUtils;

/**
 * 请假Service业务层处理
 *
 * @author 一只闲鹿
 * @date 2020-11-29
 */
@Service
//@AllArgsConstructor
public class BizLeaveServiceImpl implements IBizLeaveService
{
    @Autowired
    private BizLeaveMapper bizLeaveMapper;
    @Autowired
    private IProcessService processService;

    /**
     * 查询请假
     *
     * @param id 请假ID
     * @return 请假
     */
    @Override
    public BizLeave selectBizLeaveById(Long id)
    {
        return bizLeaveMapper.selectBizLeaveById(id);
    }

    /**
     * 查询请假列表
     *
     * @param bizLeave 请假
     * @return 请假
     */
    @Override
    public List<BizLeave> selectBizLeaveList(BizLeave bizLeave)
    {
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

    /**
     * 新增请假
     *
     * @param bizLeave 请假
     * @return 结果
     */
    @Override
    public int insertBizLeave(BizLeave bizLeave)
    {
        bizLeave.setCreateBy(SecurityUtils.getUsername());
        bizLeave.setCreateTime(DateUtils.getNowDate());
        return bizLeaveMapper.insertBizLeave(bizLeave);
    }

    /**
     * 修改请假
     *
     * @param bizLeave 请假
     * @return 结果
     */
    @Override
    public int updateBizLeave(BizLeave bizLeave)
    {
        bizLeave.setUpdateTime(DateUtils.getNowDate());
        return bizLeaveMapper.updateBizLeave(bizLeave);
    }

    /**
     * 批量删除请假
     *
     * @param ids 需要删除的请假ID
     * @return 结果
     */
    @Override
    public int deleteBizLeaveByIds(Long[] ids)
    {
        return bizLeaveMapper.deleteBizLeaveByIds(ids);
    }

    /**
     * 删除请假信息
     *
     * @param id 请假ID
     * @return 结果
     */
    @Override
    public int deleteBizLeaveById(Long id)
    {
        return bizLeaveMapper.deleteBizLeaveById(id);
    }
}
