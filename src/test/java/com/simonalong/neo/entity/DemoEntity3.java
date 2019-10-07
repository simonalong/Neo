package com.simonalong.neo.entity;

import com.simonalong.neo.annotation.Column;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/9/8 上午1:03
 */
@Data
@Accessors(chain = true)
public class DemoEntity3 implements Serializable {

    private Integer id;
    private String group;
    @Column("user_name")
    private String usName;
}
