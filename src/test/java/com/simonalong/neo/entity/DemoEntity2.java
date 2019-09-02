package com.simonalong.neo.entity;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/9/3 上午12:29
 */
@Data
@Accessors(chain = true)
public class DemoEntity2 {

    @Column("t_group")
    private String group;
    @Column("t_name")
    private String name;
    private String userName;
    private Long id;
    private String dataBaseName;
}
