package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午6:14
 */
@Data
@Table("table3")
@Accessors(chain = true)
public class TableMapAsArrayEntity2 {

    @Column(table = "table1")
    private String name;
    @Column(table = "table2")
    private Integer age;
    private String userName;
}
