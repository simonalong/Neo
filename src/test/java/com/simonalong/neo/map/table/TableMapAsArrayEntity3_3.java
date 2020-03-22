package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午7:04
 */
@Data
@Table("table3")
@Accessors(chain = true)
public class TableMapAsArrayEntity3_3 {

    @Column(table = "table1")
    private String k3;
    @Column(table = "table2")
    private Integer age3;
    private String userName;
}
