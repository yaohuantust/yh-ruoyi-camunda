package com.ruoyi.activiti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;

/**
 * 示例Demo对象 biz_example_demo
 *
 * @author 一只闲鹿
 * @date 2020-11-25
 */
public class BizExampleDemo extends ProcessEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 申请编号 */
    @Excel(name = "申请编号")
    private String applyNum;

    /** 是否高压停电（0否1是） */
    @Excel(name = "是否高压停电", readConverterExp = "0=否1是")
    private String highVoltage;

    /** 附件地址（多个逗号隔开） */
    @Excel(name = "附件地址", readConverterExp = "多=个逗号隔开")
    private String attachment;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applyUserId;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applyUserName;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 流程实例ID */
    @Excel(name = "流程实例ID")
    private String instanceId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String processKey;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setApplyNum(String applyNum)
    {
        this.applyNum = applyNum;
    }

    public String getApplyNum()
    {
        return applyNum;
    }
    public void setHighVoltage(String highVoltage)
    {
        this.highVoltage = highVoltage;
    }

    public String getHighVoltage()
    {
        return highVoltage;
    }
    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    public String getAttachment()
    {
        return attachment;
    }
    public void setApplyUserId(String applyUserId)
    {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserId()
    {
        return applyUserId;
    }
    public void setApplyUserName(String applyUserName)
    {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserName()
    {
        return applyUserName;
    }
    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId()
    {
        return instanceId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("applyNum", getApplyNum())
            .append("highVoltage", getHighVoltage())
            .append("attachment", getAttachment())
            .append("applyUserId", getApplyUserId())
            .append("applyUserName", getApplyUserName())
            .append("applyTime", getApplyTime())
            .append("instanceId", getInstanceId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("processKey", getProcessKey())
            .toString();
    }
}
