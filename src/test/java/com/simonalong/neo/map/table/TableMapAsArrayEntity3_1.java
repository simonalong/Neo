package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午7:03
 */
@Data
@Table("table1")
@Accessors(chain = true)
public class TableMapAsArrayEntity3_1 {

    @Column(table = "table1")
    private String k1;
    @Column(table = "table2")
    private Integer age1;
    private String userName;
}
