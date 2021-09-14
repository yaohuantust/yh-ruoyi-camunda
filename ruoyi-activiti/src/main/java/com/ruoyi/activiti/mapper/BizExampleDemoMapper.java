package com.ruoyi.activiti.mapper;

import java.util.List;
import com.ruoyi.activiti.domain.BizExampleDemo;

/**
 * 示例DemoMapper接口
 * 
 * @author 一只闲鹿
 * @date 2020-11-25
 */
public interface BizExampleDemoMapper 
{
    /**
     * 查询示例Demo
     * 
     * @param id 示例DemoID
     * @return 示例Demo
     */
    public BizExampleDemo selectBizExampleDemoById(Long id);

    /**
     * 查询示例Demo列表
     * 
     * @param bizExampleDemo 示例Demo
     * @return 示例Demo集合
     */
    public List<BizExampleDemo> selectBizExampleDemoList(BizExampleDemo bizExampleDemo);

    /**
     * 新增示例Demo
     * 
     * @param bizExampleDemo 示例Demo
     * @return 结果
     */
    public int insertBizExampleDemo(BizExampleDemo bizExampleDemo);

    /**
     * 修改示例Demo
     * 
     * @param bizExampleDemo 示例Demo
     * @return 结果
     */
    public int updateBizExampleDemo(BizExampleDemo bizExampleDemo);

    /**
     * 删除示例Demo
     * 
     * @param id 示例DemoID
     * @return 结果
     */
    public int deleteBizExampleDemoById(Long id);

    /**
     * 批量删除示例Demo
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizExampleDemoByIds(Long[] ids);
}
