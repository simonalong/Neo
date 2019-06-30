package com.simonalong.neo.entity;

import java.sql.Timestamp;
import lombok.Data;

/**
 * 配置项
 * @author robot
 */
@Data
public class ConfigItemDO {


    /**
     * 主键
     */
    private Long id;

    /**
     * 配置键分组，外键关联lk_config_group
     */
    private String groupCode;

    /**
     * 配置键
     */
    private String confKey;

    /**
     * 配置值
     */
    private String confValue;

    /**
     * 值类型:STRING=字符串;JSON=json类型;YML=yaml类型;PROPERTY=配置类型;GROOVY=groovy脚本
     */
    private String valType;

    /**
     * 配置的标签
     */
    private String tag;

    /**
     * 配置备注
     */
    private String remark;
    private Timestamp createTime;
    private Timestamp updateTime;

    /**
     * 实效性:PERM_AVAILABLE=永久有效;ASSIGN_UNAVAILABLE=指定时间失效;SCHEDULE_UNAVAILABLE=调度周期失效
     */
    private String timingType;

    /**
     * 过期时间
     */
    private Timestamp expireTime;

    /**
     * 任务的分组code
     */
    private String taskGroup;

    /**
     * 任务name
     */
    private String taskName;

    /**
     * 状态:Y=启用;N=禁用
     */
    private String status;

}
