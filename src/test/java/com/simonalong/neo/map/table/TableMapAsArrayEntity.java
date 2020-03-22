package com.simonalong.neo.map.table;

import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/3/22 下午6:03
 */
@Data
@Table("table1")
@Accessors(chain = true)
public class TableMapAsArrayEntity {

    private String name;
    private Integer age;
    private String userName;
}
