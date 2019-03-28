package com.simon.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author robot
 */
@Data
public class Table1DO {

    private Long id;
    /**
     * 数据来源组，外键关联lk_config_group
     */
    private String group;
    /**
     * 任务name
     */
    private String name;
    /**
     * 修改人名字
     */
    private String userName;

    /**
     * 关注
     * @author robot
     */
    @Getter
    @AllArgsConstructor
    public enum EnumDemo {

        /**
         * 可用
         */
        Y("Y"),
        /**
         * 不可用
         */
        N("N");

        private String val;
    }
}
