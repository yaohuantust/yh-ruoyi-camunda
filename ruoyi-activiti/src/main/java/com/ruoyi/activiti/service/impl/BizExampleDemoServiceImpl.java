package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.BizExampleDemoMapper;
import com.ruoyi.activiti.domain.BizExampleDemo;
import com.ruoyi.activiti.service.IBizExampleDemoService;
import org.springframework.util.CollectionUtils;

/**
 * 示例DemoService业务层处理
 *
 * @author 一只闲鹿
 * @date 2020-11-25
 */
@Service
//@AllArgsConstructor
public class BizExampleDemoServiceImpl implements IBizExampleDemoService
{
    @Autowired
    private BizExampleDemoMapper bizExampleDemoMapper;

    @Autowired
    private IProcessService processService;

    /**
     * 查询示例Demo
     *
     * @param id 示例DemoID
     * @return 示例Demo
     */
    @Override
    public BizExampleDemo selectBizExampleDemoById(Long id)
    {
        return bizExampleDemoMapper.selectBizExampleDemoById(id);
    }

    /**
     * 查询示例Demo列表
     *
     * @param bizExampleDemo 示例Demo
     * @return 示例Demo
     */
    @Override
    public List<BizExampleDemo> selectBizExampleDemoList(BizExampleDemo bizExampleDemo)
    {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getUser().getUserId())) {
            bizExampleDemo.setCreateBy(SecurityUtils.getUsername());
        }
        List<BizExampleDemo> list = bizExampleDemoMapper.selectBizExampleDemoList(bizExampleDemo);
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
     * 新增示例Demo
     *
     * @param bizExampleDemo 示例Demo
     * @return 结果
     */
    @Override
    public int insertBizExampleDemo(BizExampleDemo bizExampleDemo)
    {
        bizExampleDemo.setCreateBy(SecurityUtils.getUsername());
        bizExampleDemo.setCreateTime(DateUtils.getNowDate());
        return bizExampleDemoMapper.insertBizExampleDemo(bizExampleDemo);
    }

    /**
     * 修改示例Demo
     *
     * @param bizExampleDemo 示例Demo
     * @return 结果
     */
    @Override
    public int updateBizExampleDemo(BizExampleDemo bizExampleDemo)
    {
        bizExampleDemo.setUpdateTime(DateUtils.getNowDate());
        return bizExampleDemoMapper.updateBizExampleDemo(bizExampleDemo);
    }

    /**
     * 批量删除示例Demo
     *
     * @param ids 需要删除的示例DemoID
     * @return 结果
     */
    @Override
    public int deleteBizExampleDemoByIds(Long[] ids)
    {
        return bizExampleDemoMapper.deleteBizExampleDemoByIds(ids);
    }

    /**
     * 删除示例Demo信息
     *
     * @param id 示例DemoID
     * @return 结果
     */
    @Override
    public int deleteBizExampleDemoById(Long id)
    {
        return bizExampleDemoMapper.deleteBizExampleDemoById(id);
    }
}
