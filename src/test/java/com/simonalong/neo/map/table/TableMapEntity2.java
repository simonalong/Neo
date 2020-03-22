package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午3:48
 */
@Data
@Table("table1")
@Accessors(chain = true)
public class TableMapEntity2 {

    private String name;
    private Integer age;
    private String userName;
}
