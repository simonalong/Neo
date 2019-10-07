package com.simonalong.neo.column;

import com.simonalong.neo.annotation.Column;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/9/5 上午1:42
 */
@Data
@Accessors(chain = true)
public class ColumnEntity {

    @Column("neo_user_name")
    private String userName;
    private String group;
    private String name;
    private Long id;
    private String dataBaseName;
}
