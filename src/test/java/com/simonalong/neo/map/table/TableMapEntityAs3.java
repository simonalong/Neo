package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午6:35
 */
@Data
@Table("table1")
@Accessors(chain = true)
public class TableMapEntityAs3 {

    @Column("name")
    private String name;
    private Integer age;
    @Column("user_user")
    private String userName;
}
